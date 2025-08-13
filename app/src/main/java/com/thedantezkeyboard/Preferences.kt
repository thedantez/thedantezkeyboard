package com.thedantezkeyboard

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material3.pulltorefresh.PullToRefreshState

object Preferences {
    private const val PREF_NAME = "prefs"
    private const val KEY_EMPTY_ROW_ENABLED = "empty_row_enabled"

    fun setEmptyRowEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_EMPTY_ROW_ENABLED, enabled).apply()
    }

    fun isEmptyRowEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_EMPTY_ROW_ENABLED, false)
    }
}