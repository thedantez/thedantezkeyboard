package com.thedantezkeyboard

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val PREF_NAME = "prefs"
    private const val KEY_EMPTY_ROW_ENABLED = "empty_row_enabled"
    private const val KEY_FONT_SIZE = "font_size"
    private const val KEY_SPEED_DELETE = "speed_delete"
    private const val KEY_BIG_SYMBS_ENABLED = "big_symbs_enabled"
    private const val KEY_GESTURE_SENSITIVITY = "gesture_sensitivity"
    private const val KEY_CURSOR_SPEED = "cursor_speed"
    fun setEmptyRowEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_EMPTY_ROW_ENABLED, enabled).apply()
    }

    fun isEmptyRowEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_EMPTY_ROW_ENABLED, false)
    }

    fun setBigSymbsEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_BIG_SYMBS_ENABLED, enabled).apply()
    }

    fun isBigSymbsEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BIG_SYMBS_ENABLED, false)
    }

    fun getFontSize(context: Context): Float {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_FONT_SIZE, 17f)
    }

    fun setFontSize(context: Context, size: Float) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_FONT_SIZE, size).apply()
    }

    fun getSpeedDelete(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_SPEED_DELETE, 100)
    }

    fun setSpeedDelete(context: Context, speed: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_SPEED_DELETE, speed).apply()
    }

    fun getGestureSensitivity(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_GESTURE_SENSITIVITY, 50)
    }

    fun setGestureSensitivity(context: Context, sensitivity: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_GESTURE_SENSITIVITY, sensitivity).apply()
    }

    fun getCursorSpeed(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_CURSOR_SPEED, 30)
    }

    fun setCursorSpeed(context: Context, speed: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_CURSOR_SPEED, speed).apply()
    }
}