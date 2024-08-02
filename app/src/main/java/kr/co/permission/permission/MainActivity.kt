package kr.co.permission.permission

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.permission.ax_permission.AxPermission.Companion.create
import kr.co.permission.ax_permission.listener.AxPermissionListener

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val essentialPermissionsList: MutableList<String> = ArrayList()

        essentialPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        essentialPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        essentialPermissionsList.add(Manifest.permission.CALL_PHONE)

        create(this)
            .setPermissionListener(permissionlistener)
            .setEssentialPermission(essentialPermissionsList)
            .check()
    }

    var permissionlistener: AxPermissionListener = object : AxPermissionListener {
        override fun onPermissionGranted() {
            println("@@ 여기를 탑니다 @@@ onPermissionGranted")
        }

        override fun onPermissionDenied() {
            println("@@@ onPermissionDenied @@@")
            finish()
        }
    }
}