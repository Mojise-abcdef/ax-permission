package kr.co.permission.ax_permission

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.permission.ax_permission.AxPermission.Companion.listener
import kr.co.permission.ax_permission.listener.AxPermissionItemClickListener
import kr.co.permission.ax_permission.listener.AxPermissionListener
import kr.co.permission.ax_permission.model.AxPermissionModel
import kr.co.permission.ax_permission.util.ActivityResultHandler
import kr.co.permission.ax_permission.util.AxPermissionSettings
import kr.co.permission.ax_permission.util.CheckPermission

class AxPermissionActivity : AppCompatActivity(), AxPermissionItemClickListener ,
    ActivityResultHandler.PermissionResultListener {

    private lateinit var perMissionRecyclerView: RecyclerView
    private lateinit var permissionBt: TextView
    private lateinit var toolbar_arrowLayout: ConstraintLayout
    private val perMissionAdapter: AxPermissionAdapter by lazy { AxPermissionAdapter(this, this) }
    private lateinit var axPermissionSettings: AxPermissionSettings
    private var essentialPermissionItemList: List<AxPermissionModel>? = listOf()
    private var choicePermissionItemList: List<AxPermissionModel>? = listOf()
    private var currentPermissionModel: AxPermissionModel? = null
    private val activityResultHandler by lazy { ActivityResultHandler(this, this) }
    private var permissionActionLauncher: ActivityResultLauncher<Intent>? = null
    private var axPermissionListener: AxPermissionListener? = null

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    private val settingsLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(axPermissionListener == null){
                finish()
            }else{
                currentPermissionModel?.let {
                    if (ContextCompat.checkSelfPermission(this, it.permission) == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted()
                    } else {
                        onPermissionDenied()
                    }
                }
            }
        }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        perMissionRecyclerView = findViewById(R.id.permissionRv)
        permissionBt = findViewById(R.id.permissionBt)
        toolbar_arrowLayout = findViewById(R.id.toolbar_arrowLayout)
        perMissionRecyclerView.adapter = perMissionAdapter
        axPermissionSettings = AxPermissionSettings()

        axPermissionListener = listener

        /*필수 권한*/
        val essentialPermissionList =
            intent.getStringArrayListExtra("essentialPermission")?.toMutableList()
        /*선택 권한*/
        val choicePermissionList =
            intent.getStringArrayListExtra("choicePermission")?.toMutableList()

        essentialPermissionItemList = essentialPermissionList?.let { list ->
            axPermissionSettings.setPermission(list.toMutableList())
        }

        choicePermissionItemList = choicePermissionList?.let { list ->
            axPermissionSettings.setPermission(list.toMutableList())
        }

        val perItemMap = HashMap<String, MutableList<AxPermissionModel>>()

        essentialPermissionItemList?.let {
            perItemMap["* 필수 권한 *"] = it.toMutableList()
        }

        choicePermissionItemList?.let {
            perItemMap["* 선택 권한 *"] = it.toMutableList()
        }

        perMissionAdapter.setPerItemMap(perItemMap)
        permissionActionLauncher = activityResultHandler.permissionActionLauncher()

        /*확인버튼*/
        permissionBt.setOnClickListener {
            if (areAllPermissionsGranted()) {
                /*여기에 성공 콜백 들어가야함*/
                permissionGranted()
                finish()
            } else {
                Toast.makeText(this, "필수 권한을 허용 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        toolbar_arrowLayout.setOnClickListener {
            if (areAllPermissionsGranted()) {
                if (areAllPermissionsGranted()) {
                    /*여기에 콜백 들어가야함*/
                    permissionGranted()
                }else{
                    permissionDenied()
                }
                finish()
            } else {
                showBackPressedDialog()
            }
        }

        //뒤로 가기
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (areAllPermissionsGranted()) {
                        /*여기에 콜백 들어가야함*/
                        permissionGranted()
                        finish()
                    } else {
                        showBackPressedDialog()
                    }
                }
            }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showBackPressedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("권한 필요")
        builder.setMessage("필수 권한을 허용하지 않으셨습니다. 앱을 종료 하시겠습니까??")

        builder.setPositiveButton("예") { dialog, _ ->
            if (areAllPermissionsGranted()) {
                permissionGranted()
            }else{
                permissionDenied()
            }
            finish()
            dialog.dismiss()
        }

        builder.setNegativeButton("아니요") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /*성공 콜백*/
    private fun permissionGranted(){
        axPermissionListener?.onPermissionGranted()
    }
    /*실패 콜백*/
    private fun permissionDenied(){
        axPermissionListener?.onPermissionDenied()
    }

    override fun onPerClick(permissionModel: AxPermissionModel?) {
        currentPermissionModel = permissionModel
        when (permissionModel?.perType) {
            "action" -> {
                activityResultHandler.requestPermissionWithPackageName(permissionActionLauncher , permissionModel)
            }
            "access" -> {
                if (ContextCompat.checkSelfPermission(this@AxPermissionActivity, permissionModel.permission) == PackageManager.PERMISSION_GRANTED) {
                    showGrantedPermissionDialog(this)
                } else {
                    requestPermission(permissionModel)
                }
            }
        }
    }

    private fun requestPermission(permissionModel: AxPermissionModel?) {
        permissionModel?.let {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(it.permission),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }

    private fun onPermissionGranted() {
        // 필수 권한 리스트 업데이트 및 RecyclerView 갱신
        essentialPermissionItemList?.forEachIndexed { index, essentialModel ->
            if (ContextCompat.checkSelfPermission(this@AxPermissionActivity, essentialModel.permission) == PackageManager.PERMISSION_GRANTED) {
                essentialModel.perState = true
            }
            // 헤더가 있는 경우 인덱스에 +1을 해야 정확한 위치가 됩니다
            perMissionAdapter.notifyItemChanged(index + 1)
        }

        // 선택 권한 리스트 업데이트 및 RecyclerView 갱신
        choicePermissionItemList?.forEachIndexed { index, choicePerModel ->
            if (ContextCompat.checkSelfPermission(this@AxPermissionActivity, choicePerModel.permission) == PackageManager.PERMISSION_GRANTED) {
                choicePerModel.perState = true
            }
            // 필수 권한과 필수 권한의 헤더 수를 더한 인덱스를 사용해야 합니다
            val position = (essentialPermissionItemList?.size ?: 0) + 1 + index + 1 // 필수 권한 헤더 + 필수 권한 아이템 + 선택 권한 헤더
            perMissionAdapter.notifyItemChanged(position)
        }
    }

    private fun onPermissionDenied() {
        showDeniedPermissionDialog()
    }

    private fun showDeniedPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("권한 필요")
        builder.setMessage("다음 권한이 거부되었습니다: ${currentPermissionModel?.perTitle}\n권한을 다시 요청하시겠습니까?")

        builder.setPositiveButton("설정으로 이동") { dialog, _ ->
            startPerSettingActivity()
            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun startPerSettingActivity() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        try {
            settingsLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun showGrantedPermissionDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("권한 이미 부여됨")
        builder.setMessage("권한이 이미 설정되었습니다: \n다시 권한을 설정하시겠습니까?")

        builder.setPositiveButton("예") { dialog, _ ->
            startPerSettingActivity()
            dialog.dismiss()
        }

        builder.setNegativeButton("아니오") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun updatePermissionState(permission:String , isGranted: Boolean){
        // 필수 권한 리스트 업데이트 및 RecyclerView 갱신
        essentialPermissionItemList?.forEachIndexed { index, essentialModel ->
            if (essentialModel.permission == permission) {
                essentialModel.perState = isGranted
            }
            // 헤더가 있는 경우 인덱스에 +1을 해야 정확한 위치가 됩니다
            perMissionAdapter.notifyItemChanged(index + 1)
        }

        // 선택 권한 리스트 업데이트 및 RecyclerView 갱신
        choicePermissionItemList?.forEachIndexed { index, choicePerModel ->
            if (choicePerModel.permission == permission) {
                choicePerModel.perState = isGranted
            }
            // 필수 권한과 필수 권한의 헤더 수를 더한 인덱스를 사용해야 합니다
            val position = (essentialPermissionItemList?.size ?: 0) + 1 + index + 1 // 필수 권한 헤더 + 필수 권한 아이템 + 선택 권한 헤더
            perMissionAdapter.notifyItemChanged(position)
        }
    }

    // 모든 권한이 부여되었는지 확인하는 메서드
    private fun areAllPermissionsGranted(): Boolean {
        essentialPermissionItemList?.forEach {
            if (!it.perState) {
                return false
            }
        }
        return true
    }

    private fun checkPermission(){
        CheckPermission().checkSelfPermission(this , essentialPermissionItemList)
        CheckPermission().checkSelfPermission(this , choicePermissionItemList)

        permissionBt.isVisible = areAllPermissionsGranted()

        if(areAllPermissionsGranted()){
            permissionGranted()
        }
    }

    override fun onPermissionResult(permission: String?, isGranted: Boolean) {
        if (permission != null) {
            updatePermissionState(permission , isGranted)
        }
    }
}