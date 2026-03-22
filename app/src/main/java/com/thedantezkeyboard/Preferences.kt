package com.thedantezkeyboard

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val KEY_SHOW_CTRL = "show_ctrl"
    private const val KEY_SHOW_ALT = "show_alt"
    private const val KEY_SHOW_ENRU = "show_enru"
    private const val KEY_SHOW_DEL = "show_del"
    private const val KEY_SHOW_BS = "show_bs"
    fun setShowCtrl(context: Context, show: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_SHOW_CTRL, show).apply()
    }
    fun isShowCtrl(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_CTRL, true) //default
    }
    fun setShowAlt(context: Context, show: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_SHOW_ALT, show).apply()
    }
    fun isShowAlt(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_ALT, true) //default
    }
    fun setShowENRU(context: Context, show: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_SHOW_ENRU, show).apply()
    }
    fun isShowENRU(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_ENRU, true) //default
    }
    fun setShowDEL(context: Context, show: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_SHOW_DEL, show).apply()
    }
    fun isShowDEL(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_DEL, true) //default
    }
    fun setShowBS(context: Context, show: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_SHOW_BS, show).apply()
    }
    fun isShowBS(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_BS, true) //default
    }
    private const val PREF_NAME = "prefs"
    private const val KEY_EMPTY_ROW_ENABLED = "empty_row_enabled"
    private const val KEY_FONT_SIZE = "font_size"
    private const val KEY_SPEED_DELETE = "speed_delete"
    private const val KEY_BIG_SYMBS_ENABLED = "big_symbs_enabled"
    private const val KEY_GESTURE_SENSITIVITY = "gesture_sensitivity"
    private const val KEY_CURSOR_SPEED = "cursor_speed"
    private const val KEY_BUTTON_HEIGHT = "button_height"
    fun getButtonHeight(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_BUTTON_HEIGHT, 150)
    }
    fun setButtonHeight(context: Context, height: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_BUTTON_HEIGHT, height).apply()
    }
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