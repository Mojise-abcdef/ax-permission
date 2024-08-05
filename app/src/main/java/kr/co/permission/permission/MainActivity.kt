package kr.co.permission.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kr.co.permission.ax_permission.AxPermission
import kr.co.permission.ax_permission.AxPermission.Companion.create
import kr.co.permission.ax_permission.listener.AxPermissionListener
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {


    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*필수 권한 리스트*/
        val essentialPermissionsList: MutableList<String> = ArrayList()
        /*선택 권한 리스트*/
        val choicePermissionList: MutableList<String> = ArrayList()

        essentialPermissionsList.add(Settings.ACTION_ACCESSIBILITY_SETTINGS)

        essentialPermissionsList.add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)

        essentialPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        essentialPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        essentialPermissionsList.add(Manifest.permission.CALL_PHONE)

        // 버전별 권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            choicePermissionList.add(Manifest.permission.POST_NOTIFICATIONS)
            choicePermissionList.add(Manifest.permission.READ_MEDIA_IMAGES)
            choicePermissionList.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            choicePermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            choicePermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            choicePermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        create(this)
            .setPermissionListener(permissionListener)
            .setRequiredPermissions(essentialPermissionsList)
            .setOptionalPermissions(choicePermissionList)
            .setSubmitButtonColors(
                buttonColor = R.color.purple_200 ,
                textColor = R.color.black
            )
            .check()
    }

    private var permissionListener: AxPermissionListener = object : AxPermissionListener {
        override fun onPermissionGranted() {
            /*성공 콜백 리스너*/
        }

        override fun onPermissionDenied() {
            /*실패 콜백 리스너*/
        }
    }
}