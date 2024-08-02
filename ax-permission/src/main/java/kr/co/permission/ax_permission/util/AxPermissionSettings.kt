package kr.co.permission.ax_permission.util

import android.Manifest
import android.annotation.SuppressLint
import android.provider.Settings
import kr.co.permission.ax_permission.model.AxPermissionModel

class AxPermissionSettings {
    
    private var perMap: HashMap<String, AxPermissionModel> = hashMapOf()

    @SuppressLint("BatteryLife")
    fun setPermission(perList: MutableList<String>): List<AxPermissionModel> {
        val perData = mutableListOf<AxPermissionModel>()

        perList.forEach { data ->
            val permission = perMap[data]
            if (permission != null) {
                perData.add(permission)
            }
        }
        return perData
    }

    init {
        /*접근 권한*/
        perMap[Settings.ACTION_MANAGE_OVERLAY_PERMISSION] = AxPermissionModel(
            "앱 위에 그리기",
            "다른 앱 위에 오버레이를 설정할 수 있는 권한입니다.",
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            false,"action"
        )
        perMap[Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS] = AxPermissionModel(
            "배터리 최적화 무시 설정",
            "배터리 최적화 무시 설정 권한입니다.",
            Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS,
            false,
            "action"
        )
        perMap[Settings.ACTION_NFC_SETTINGS] = AxPermissionModel(
            "NFC 설정",
            "NFC 설정을 변경할 수 있는 권한입니다.",
            Settings.ACTION_NFC_SETTINGS,
            false,
            "action"
        )
        perMap[Settings.ACTION_ACCESSIBILITY_SETTINGS] = AxPermissionModel(
            "접근성 설정",
            "접근성 설정을 변경할 수 있는 권한입니다.",
            Settings.ACTION_ACCESSIBILITY_SETTINGS,
            false,
            "action"
        )
        perMap[Manifest.permission.CHANGE_WIFI_STATE] = AxPermissionModel(
            "WiFi 상태 변경",
            "WiFi 상태를 변경하기 위해 필요한 권한입니다.",
            Manifest.permission.CHANGE_WIFI_STATE,
            false,
            "action" // 권한 허용 화면으로 이동
        )





        /*팝업 알림*/
        perMap[Manifest.permission.CALL_PHONE] = AxPermissionModel(
            "전화 걸기",
            "전화 걸기 권한입니다.",
            Manifest.permission.CALL_PHONE,
            false,"access"
        )
        perMap[Manifest.permission.POST_NOTIFICATIONS] = AxPermissionModel(
            "알림",
            "알림 권한입니다.",
            Manifest.permission.POST_NOTIFICATIONS,
            false,"access"
        )
        perMap[Manifest.permission.PACKAGE_USAGE_STATS] = AxPermissionModel(
            "사용자 기기 상태 접근",
            "사용자 기기 상태 접근 권한입니다.",
            Manifest.permission.PACKAGE_USAGE_STATS,
            false,"access"
        )
        perMap[Manifest.permission.CAMERA] = AxPermissionModel(
            "카메라",
            "카메라 사용 권한입니다.",
            Manifest.permission.CAMERA,
            false,"access"
        )
        perMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = AxPermissionModel(
            "저장소 쓰기",
            "저장소 데이터를 쓸 수 있는 권한입니다.",
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            false,"access"
        )
        perMap[Manifest.permission.READ_EXTERNAL_STORAGE] = AxPermissionModel(
            "저장소 읽기",
            "저장소 데이터를 읽을 수 있는 권한입니다.",
            Manifest.permission.READ_EXTERNAL_STORAGE,
            false,"access"
        )
        perMap[Manifest.permission.READ_MEDIA_IMAGES] = AxPermissionModel(
            "이미지 읽기",
            "이미지 파일을 읽을 수 있는 권한입니다.",
            Manifest.permission.READ_MEDIA_IMAGES,
            false,"access"
        )
        perMap[Manifest.permission.READ_MEDIA_VIDEO] = AxPermissionModel(
            "비디오 읽기",
            "비디오 파일을 읽을 수 있는 권한입니다.",
            Manifest.permission.READ_MEDIA_VIDEO,
            false,"access"
        )
        perMap[Manifest.permission.VIBRATE] = AxPermissionModel(
            "진동 사용",
            "진동 기능 사용 권한입니다.",
            Manifest.permission.VIBRATE,
            false,"access"
        )
        perMap[Manifest.permission.RECORD_AUDIO] = AxPermissionModel(
            "오디오",
            "오디오 사용에 필요한 권한입니다.",
            Manifest.permission.RECORD_AUDIO,
            false,"access"
        )
        perMap[Manifest.permission.READ_PHONE_NUMBERS] = AxPermissionModel(
            "전화번호 가져오기",
            "전화번호 가져오기에 필요한 권한입니다.",
            Manifest.permission.READ_PHONE_NUMBERS,
            false,"access"
        )
        perMap[Manifest.permission.READ_PHONE_STATE] = AxPermissionModel(
            "전화번호",
            "전화번호 가져오기에 필요한 권한입니다.",
            Manifest.permission.READ_PHONE_STATE,
            false,"access"
        )
        perMap[Manifest.permission.ACCESS_MEDIA_LOCATION] = AxPermissionModel(
            "미디어 위치 접근",
            "미디어 파일의 위치 정보를 가져오기 위해 필요한 권한입니다.",
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            false,"access"
        )
        perMap[Manifest.permission.BLUETOOTH_CONNECT] = AxPermissionModel(
            "블루투스 연결",
            "블루투스 장치에 연결하기 위해 필요한 권한입니다.",
            Manifest.permission.BLUETOOTH_CONNECT,
            false,"access"
        )
        perMap[Manifest.permission.BLUETOOTH_SCAN] = AxPermissionModel(
            "블루투스 스캔",
            "블루투스 장치를 스캔하기 위해 필요한 권한입니다.",
            Manifest.permission.BLUETOOTH_SCAN,
            false,"access"
        )
        // Bluetooth 권한
        perMap[Manifest.permission.BLUETOOTH_ADMIN] = AxPermissionModel(
            "블루투스 연결",
            "블루투스 장치에 연결하고 관리하기 위해 필요한 권한입니다.",
            Manifest.permission.BLUETOOTH_ADMIN,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.BLUETOOTH] = AxPermissionModel(
            "블루투스 스캔",
            "블루투스 장치를 스캔하기 위해 필요한 권한입니다.",
            Manifest.permission.BLUETOOTH,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.READ_CONTACTS] = AxPermissionModel(
            "연락처 읽기",
            "연락처 정보를 읽기 위해 필요한 권한입니다.",
            Manifest.permission.READ_CONTACTS,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.SEND_SMS] = AxPermissionModel(
            "SMS 전송",
            "SMS를 전송하기 위해 필요한 권한입니다.",
            Manifest.permission.SEND_SMS,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.MODIFY_AUDIO_SETTINGS] = AxPermissionModel(
            "오디오 설정 수정",
            "오디오 설정을 수정하기 위해 필요한 권한입니다.",
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.ACCESS_NOTIFICATION_POLICY] = AxPermissionModel(
            "알림 정책 접근",
            "알림 정책에 접근하기 위해 필요한 권한입니다.",
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.WRITE_SETTINGS] = AxPermissionModel(
            "설정 쓰기",
            "시스템 설정을 쓰기 위해 필요한 권한입니다.",
            Manifest.permission.WRITE_SETTINGS,
            false,
            "action" // 권한 허용 화면으로 이동
        )
        perMap[Manifest.permission.ACCESS_WIFI_STATE] = AxPermissionModel(
            "WiFi 상태 접근",
            "WiFi 상태에 접근하기 위해 필요한 권한입니다.",
            Manifest.permission.ACCESS_WIFI_STATE,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.ACTIVITY_RECOGNITION] = AxPermissionModel(
            "활동 인식",
            "활동을 인식하기 위해 필요한 권한입니다.",
            Manifest.permission.ACTIVITY_RECOGNITION,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.SET_ALARM] = AxPermissionModel(
            "알람 설정",
            "알람을 설정하기 위해 필요한 권한입니다.",
            Manifest.permission.SET_ALARM,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.SCHEDULE_EXACT_ALARM] = AxPermissionModel(
            "정확한 알람 일정",
            "정확한 알람을 일정에 추가하기 위해 필요한 권한입니다.",
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.ACCESS_FINE_LOCATION] = AxPermissionModel(
            "정확한 위치 정보",
            "이 권한은 사용자의 정확한 위치를 얻기 위해 필요합니다.",
            Manifest.permission.ACCESS_FINE_LOCATION,
            false,
            "access" // 접근 권한
        )
        perMap[Manifest.permission.ACCESS_COARSE_LOCATION] = AxPermissionModel(
            "대략적인 위치 정보",
            "이 권한은 사용자의 대략적인 위치를 얻기 위해 필요합니다.",
            Manifest.permission.ACCESS_COARSE_LOCATION,
            false,
            "access" // 접근 권한
        )
    }
}
