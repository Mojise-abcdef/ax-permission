package kr.co.permission.permission

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap.Config
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kr.co.permission.ax_permission.AxPermission.Companion.create
import kr.co.permission.ax_permission.listener.AxPermissionListener
import kr.co.permission.ax_permission.util.AxPermissionList




class MainActivity : AppCompatActivity() {


    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*필수 권한 리스트*/
        val requiredPermissions = AxPermissionList()

        /*선택 권한 리스트*/
        val optionalPermissions = AxPermissionList()

        //requiredPermissions.add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,"앱 위에 그리기 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
        requiredPermissions.add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,"앱 위에 그리기 권한이 필요합니다. 설정에서 권한을 허용해주세요. TEST 입니다.")
        requiredPermissions.add(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        requiredPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        // 버전별 권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            optionalPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
            optionalPermissions.add(Manifest.permission.READ_MEDIA_IMAGES , "이미지 읽기 입니다.")
            optionalPermissions.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            optionalPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            optionalPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            optionalPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        create(this)
            .setPermissionListener(permissionListener)
            .setRequiredPermissions(requiredPermissions)
            .setOptionalPermissions(optionalPermissions)
            .setSubmitButtonColors(
                buttonBackgroundColor = R.color.purple_200 ,
                textColor = R.color.black
            )
            .check()

        testButton = findViewById(R.id.testButton)
        testButton.setOnClickListener {
            startActivity(Intent(this , ConfigActivity::class.java))
        }
    }


    private var permissionListener: AxPermissionListener = object : AxPermissionListener {
        override fun onPermissionGranted() {
            /*성공 콜백 리스너*/
        }

        override fun onPermissionDenied() {
            /*실패 콜백 리스너*/
            finish()
        }
    }
}