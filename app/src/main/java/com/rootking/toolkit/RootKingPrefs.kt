package com.rootking.toolkit

import android.content.Context

class RootKingPrefs(context: Context) {
    private val prefs = context.getSharedPreferences(
        "rootking_settings",
        Context.MODE_PRIVATE
    )

    fun get(key: String, default: Boolean = false): Boolean =
        prefs.getBoolean(key, default)

    fun set(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
}
