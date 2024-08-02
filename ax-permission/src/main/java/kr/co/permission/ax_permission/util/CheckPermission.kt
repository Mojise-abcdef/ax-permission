package kr.co.permission.ax_permission.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kr.co.permission.ax_permission.AxPermission.Companion.accessibilityServiceClass
import kr.co.permission.ax_permission.model.AxPermissionModel


class CheckPermission {

    /**
     * 권한을 확인하고 상태를 업데이트하는 메서드
     *
     * @param context 애플리케이션 컨텍스트
     * @param currentPerList 현재 권한 목록
     */
    fun checkSelfPermission(context: Context ,currentPerList :List<AxPermissionModel>? ){
        currentPerList?.forEach {
            when(it.permission){
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION -> it.perState = isOverlayPermissionGranted(context)
                Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS -> it.perState = isIgnoringBatteryOptimizations(context)
                Settings.ACTION_NFC_SETTINGS -> it.perState = isNfcPermissionGranted(context)
                Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS -> it.perState = isNotificationListenerSettingsPermissionGranted(context)
                Settings.ACTION_ACCESSIBILITY_SETTINGS -> it.perState = isAccessibilityServiceEnabled(context , accessibilityServiceClass)
                Manifest.permission.CHANGE_WIFI_STATE ->it.perState = isWifiEnabled(context)
                else ->{
                    if (ContextCompat.checkSelfPermission(context, it.permission) == PackageManager.PERMISSION_GRANTED) {
                        it.perState = true // 알림 팝업 권한이 허용된 경우 상태 설정
                    }
                }
            }
        }
    }

    /**
     * 오버레이 권한이 부여되었는지 확인하는 메서드.
     * API 23 이하 버전에서는 오버레이 권한이 자동으로 부여됩니다.
     *
     * @param context 애플리케이션 컨텍스트.
     * @return 오버레이 권한이 부여된 경우 true, 그렇지 않은 경우 false.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun isOverlayPermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            // API 23 이하 버전에서는 오버레이 권한이 자동으로 부여됩니다.
            true
        }
    }

    /**
     * 애플리케이션에 알림 리스너 설정 권한이 부여되었는지 확인하는 메서드.
     * 이 메서드는 모든 Android 버전에서 적용됩니다.
     *
     * @param context 애플리케이션 컨텍스트.
     * @return 알림 리스너 설정 권한이 부여된 경우 true, 그렇지 않은 경우 false.
     */fun isNotificationListenerSettingsPermissionGranted(context: Context): Boolean {
        val packageName = context.packageName
        return NotificationManagerCompat.getEnabledListenerPackages(context).contains(packageName)
    }

    /**
     * 배터리 최적화 무시 권한이 부여되었는지 확인하는 메서드.
     * 이 메서드는 Android M (API 레벨 23) 이상에서만 적용됩니다.
     *
     * @param context 애플리케이션 컨텍스트.
     * @return 배터리 최적화 무시 권한이 부여된 경우 true, 그렇지 않은 경우 false.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val packageName = context.packageName
        return context.getSystemService(PowerManager::class.java)
            .isIgnoringBatteryOptimizations(packageName)
    }

    /**
     * NFC 권한이 부여되었는지 확인하는 메서드입니다.
     *
     * @param context 애플리케이션 컨텍스트.
     * @return NFC가 지원되고 활성화된 경우 true, 그렇지 않으면 false.
     */
    fun isNfcPermissionGranted(context: Context): Boolean {
        val packageManager = context.packageManager
        val hasNfcFeature = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

        if (!hasNfcFeature) {
            return false // 이 기기는 NFC를 지원하지 않습니다.
        }

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

        return nfcAdapter?.isEnabled == true
    }


    /**
     * 접근성 서비스 활성화 여부 확인 메서드
     *
     * @param context 애플리케이션 컨텍스트
     * @param serviceClass 접근성 서비스 클래스
     * @return 접근성 서비스가 활성화된 경우 true, 그렇지 않은 경우 false
     */
    fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<*>): Boolean {
        val accessibilityEnabled: Int = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }

        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            if (settingValue != null) {
                colonSplitter.setString(settingValue)
                while (colonSplitter.hasNext()) {
                    val componentName = colonSplitter.next()
                    if (componentName.equals("${context.packageName}/${serviceClass.canonicalName}", ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }
    /**
     * WiFi 권한이 부여되었는지 확인하는 메서드
     *
     * @param context 애플리케이션 컨텍스트
     * @return WiFi가 활성화된 경우 true, 그렇지 않은 경우 false
     */
    fun isWifiEnabled(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
}