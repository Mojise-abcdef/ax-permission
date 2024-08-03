package kr.co.permission.ax_permission

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.permission.ax_permission.listener.AxPermissionListener
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class AxPermission private constructor(private val context: Context) {
    private lateinit var permissionListener: AxPermissionListener
    private var essentialPermissionList = mutableListOf<String>()
    private var choicePermissionList = mutableListOf<String>()
    private var intent:Intent = Intent(context , AxPermissionActivity::class.java)

    fun setPermissionListener(listener: AxPermissionListener): AxPermission = apply {
        this.permissionListener = listener
    }
    /**
     * registerEssentialPermissionGlobally
     * 필수 권한 한번 등록시 기록이 남음
     ***/
    fun setEssentialPermission(essentialPermissionList: List<String>): AxPermission = apply {
        this.essentialPermissionList.addAll(essentialPermissionList)
        registerEssentialPermissionGloballyList = this.essentialPermissionList
    }
    /**
     * registerChoicePermissionGlobally
     * 선택 권한 한번 등록시 기록이 남음
     ***/
    fun setChoicePermission(choicePermissionList: List<String>): AxPermission = apply {
        this.choicePermissionList.addAll(choicePermissionList)
        registerChoicePermissionGloballyList = this.choicePermissionList
    }

    fun check(): AxPermission {
        intent.putStringArrayListExtra(
            "essentialPermission",
            essentialPermissionList as ArrayList<String>
        )
        intent.putStringArrayListExtra(
            "choicePermission",
            choicePermissionList as ArrayList<String>
        )
        intent.putExtra("state","check")
        listener = permissionListener
        context.startActivity(intent)
        return this
    }

    fun setAccessibilityService(serviceClass:Class<*>){
        accessibilityServiceClass = serviceClass
    }

    fun onReStart(): AxPermission = apply {
        /*this.essentialPermissionList = registerEssentialPermissionGloballyList
        this.choicePermissionList = registerChoicePermissionGloballyList*/

        intent.putStringArrayListExtra(
            "essentialPermission",
            registerEssentialPermissionGloballyList as ArrayList<String>
        )
        intent.putStringArrayListExtra(
            "choicePermission",
            registerChoicePermissionGloballyList as ArrayList<String>
        )
        intent.putExtra("state","restart")
        listener = permissionListener
        context.startActivity(intent)
        this.check()
    }


    companion object {
        var listener: AxPermissionListener? = null
        lateinit var accessibilityServiceClass:Class<*>
        private var registerEssentialPermissionGloballyList = mutableListOf<String>()
        private var registerChoicePermissionGloballyList = mutableListOf<String>()
        fun create(context: Context) = AxPermission(context)
    }
}