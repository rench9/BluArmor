package com.r4hu7.bluarmor.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object PreferencesUtil {
    private const val PREFS_FILE_NAME = "preferences"
    fun firstTimeAskingPermission(context: Context, permission: String, isFirstTime: Boolean) {
        context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit().putBoolean(permission, isFirstTime).apply()
    }

    fun isFirstTimeAskingPermission(context: Context, permission: String): Boolean {
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true)
    }

}