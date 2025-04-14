package com.interview.ideamanager.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPrefUtils(private val context: Context) {
    val sharedPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var locale: String
        get() = sharedPrefs.getString("locale", "en") ?: "en"
        set(value) = sharedPrefs.edit().putString("locale", value).apply()

    var biometrics: Boolean
        get() = sharedPrefs.getBoolean("biometrics", false)
        set(value) = sharedPrefs.edit().putBoolean("biometrics", value).apply()
}