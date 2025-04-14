package com.interview.ideamanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar
import com.interview.ideamanager.R
import com.interview.ideamanager.data.SharedPrefUtils
import com.interview.ideamanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var blockProgress: Boolean = true
    private lateinit var sharedPrefUtils: SharedPrefUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        sharedPrefUtils = SharedPrefUtils(this)
        splashScreen.setKeepOnScreenCondition(
            // Return true if you want to keep the splash screen on the screen until your app is ready
            {
                // Check if the user is authenticated
                if (!sharedPrefUtils.biometrics) {
                    false
                } else {
                    // Show the splash screen until the user is authenticated
                    blockProgress
                }
            }
        )

        super.onCreate(savedInstanceState)

        if (sharedPrefUtils.biometrics) {
            authenticate()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun authenticate() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showSnackbar(getString(R.string.authentication_error, errString))
                    blockProgress = true
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    blockProgress = false
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showSnackbar(getString(R.string.authentication_failed))
                    blockProgress = true
                }
            })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.unlock))
            .setSubtitle(getString(R.string.log_in_using_your_biometric_credential))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}