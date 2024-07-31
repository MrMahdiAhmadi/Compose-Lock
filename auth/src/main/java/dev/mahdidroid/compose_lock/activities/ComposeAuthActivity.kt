package dev.mahdidroid.compose_lock.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.ui.pin.PinEntryScreen
import dev.mahdidroid.compose_lock.utils.AuthScreen
import dev.mahdidroid.compose_lock.utils.AuthViewModel
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

class ComposeAuthActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val vm: AuthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricPrompt = createBiometricPrompt(onSuccess = {
            vm.onAuthSuccess()
        })
        promptInfo = createPromptInfo()
        showBiometricPrompt()

        setContent {
            val state = vm.state.collectAsState()
            when (state.value) {
                AuthScreen.Pin -> PinEntryScreen(onFingerPrintClick = {
                    showBiometricPrompt()
                })

                AuthScreen.Password -> {}
                AuthScreen.ChangePin -> {}
                AuthScreen.ChangePassword -> {}
                AuthScreen.Main -> {
                    finish()
                }
            }
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
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password").build()
}