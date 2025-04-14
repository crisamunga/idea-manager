package com.interview.ideamanager.data

import android.content.SharedPreferences

class SharedPrefUtils(private val sharedPrefs: SharedPreferences) {
    var workJobId: String
        get() = sharedPrefs.getString("WORK_JOB_ID", "") ?: ""
        set(value) = sharedPrefs.edit().putString("WORK_JOB_ID", value).apply()
}