package dev.mahdidroid.compose_lock.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.ui.ApplyTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

internal class AuthenticationActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val vm: LockViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Refactor this if statement for a cleaner and more efficient approach
        if (vm.state.value == AuthState.Pin || vm.state.value == AuthState.Password) {
            biometricPrompt = setupBiometricPrompt(onSuccess = {
                vm.handleAuthSuccess()
            })
            promptInfo = buildPromptInfo()
            displayBiometricPrompt()
        }

        setContent {
            val state = vm.state.collectAsState()
            ApplyTheme(vm) { colors ->
                AuthenticationContent(modifier = Modifier
                    .fillMaxSize()
                    .background(colors.primary),
                    state = state.value,
                    onBiometricPrompt = {
                        displayBiometricPrompt()
                    },
                    onFinishActivity = {
                        finish()
                    })
            }
        }
    }

    private fun displayBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun setupBiometricPrompt(onSuccess: () -> Unit): BiometricPrompt {
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

    private fun buildPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password").build()
}