package com.interview.ideamanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.interview.ideamanager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val biometrics: Preference? = findPreference("biometrics")
        biometrics?.let {
            val biometricManager = BiometricManager.from(requireContext())
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    it.isVisible = true
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // TODO: Prompt user to enrol biometrics and make option visible
                    it.isVisible = false
                }
                else -> {
                    it.isVisible = false
                }
            }
        }

        val locale: ListPreference? = findPreference("locale")
        locale?.let {
            it.setOnPreferenceChangeListener { preference, newValue ->
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(newValue as String)
                AppCompatDelegate.setApplicationLocales(appLocale)
                true
            }
            it.value = it.value ?: "en"
        }
    }
}