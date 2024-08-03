package kr.co.permission.ax_permission.util

import android.content.Context
import android.content.SharedPreferences

object PermissionStateManager {
    private const val PREF_NAME = "AxPermissionPrefs"
    private const val KEY_PERMISSION_DENIED = "permissionDenied"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun ensureInitialized() {
        if (!this::sharedPreferences.isInitialized) {
            throw UninitializedPropertyAccessException("PermissionStateManager is not initialized. Call initialize() first.")
        }
    }

    fun setPermissionDenied() {
        ensureInitialized()
        sharedPreferences.edit().putBoolean(KEY_PERMISSION_DENIED, true).apply()
    }

    fun isPermissionDenied(): Boolean {
        ensureInitialized()
        return sharedPreferences.getBoolean(KEY_PERMISSION_DENIED, false)
    }

    fun resetPermissionDenied() {
        ensureInitialized()
        sharedPreferences.edit().remove(KEY_PERMISSION_DENIED).apply()
    }
}