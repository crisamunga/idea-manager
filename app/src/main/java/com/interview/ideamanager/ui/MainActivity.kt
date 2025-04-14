package com.interview.ideamanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.interview.ideamanager.R
import com.interview.ideamanager.data.SharedPrefUtils
import com.interview.ideamanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var blockProgress: Boolean = true
    private lateinit var sharedPrefUtils: SharedPrefUtils
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        sharedPrefUtils = SharedPrefUtils(this)
        splashScreen.setKeepOnScreenCondition()
            {
                // Check if the user is authenticated
                if (!sharedPrefUtils.biometrics) {
                    false
                } else {
                    // Show the splash screen until the user is authenticated
                    blockProgress
                }
            }


        super.onCreate(savedInstanceState)

        if (sharedPrefUtils.biometrics) {
            authenticate()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
    }

    override fun onResume() {
        if (sharedPrefUtils.biometrics) {
            authenticate()
        }
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (navController.currentDestination?.id != R.id.homeFragment) {
            return false
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun authenticate() {
        blockProgress = true
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Snackbar.make(binding.root, getString(R.string.authentication_failed), Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.retry)) {
                            authenticate()
                        }
                        .show()
                    blockProgress = true
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    blockProgress = false
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Snackbar.make(binding.root, getString(R.string.authentication_failed), Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.retry)) {
                            authenticate()
                        }
                        .show()
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

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, _, _ ->
            invalidateOptionsMenu()
        }
    }
}