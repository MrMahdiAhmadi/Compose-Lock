package dev.mahdidroid.compose_lock.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.AuthenticationViewModel
import dev.mahdidroid.compose_lock.PinEntryScreen
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

class ComposeAuthActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: AuthenticationViewModel by inject()

        biometricPrompt = createBiometricPrompt(onSuccess = {
            vm.setAuthenticated(true)
        })
        promptInfo = createPromptInfo()
        showBiometricPrompt()

        setContent {
            PinEntryScreen(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
                maxPinLength = 4,
                onFingerPrintClick = {
                    showBiometricPrompt()
                })
        }
    }

    private fun showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun createBiometricPrompt(onSuccess: () -> Unit): BiometricPrompt {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
      /*          Toast.makeText(
                    this@ComposeAuthActivity, "Authentication error: $errString", Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
             /*   Toast.makeText(
                    this@ComposeAuthActivity, "Authentication succeeded", Toast.LENGTH_SHORT
                ).show()*/
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                /*Toast.makeText(
                    this@ComposeAuthActivity, "Authentication failed", Toast.LENGTH_SHORT
                ).show()*/
            }
        })
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password").build()
}