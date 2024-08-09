package dev.mahdidroid.compose_lock.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.BiometricMessages
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

internal class AuthenticationActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val vm: LockViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricPrompt = setupBiometricPrompt(onSuccess = {
            vm.sendIntent(LockIntent.OnNavigateToMainScreen)
        })
        promptInfo = buildPromptInfo(vm.viewState.value.messages.biometricMessages)

        setContent {
            val currentTheme =
                if (vm.viewState.value.isSingleTheme) vm.viewState.value.lightTheme else {
                    if (isSystemInDarkTheme()) vm.viewState.value.darkTheme else vm.viewState.value.lightTheme
                }
            AuthenticationContent(modifier = Modifier.fillMaxSize(),
                state = vm.viewState.value.currentAuthState,
                theme = currentTheme,
                pinTitleMessage = vm.viewState.value.messages.pinTitleMessage,
                changePinTitleMessages = vm.viewState.value.messages.changePinTitleMessages,
                onBiometricPrompt = {
                    displayBiometricPrompt()
                },
                onFinishActivity = {
                    vm.sendIntent(LockIntent.OnNavigateToMainScreen)
                    finish()
                })
            // TODO: Refactor this if statement for a cleaner and more efficient approach
            if (vm.viewState.value.currentAuthState == AuthState.Pin || vm.viewState.value.currentAuthState == AuthState.Password) {
                // displayBiometricPrompt()
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

    private fun buildPromptInfo(messages: BiometricMessages): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle(messages.title).setSubtitle(messages.subtitle)
            .setNegativeButtonText(messages.negativeButtonText).build()
}