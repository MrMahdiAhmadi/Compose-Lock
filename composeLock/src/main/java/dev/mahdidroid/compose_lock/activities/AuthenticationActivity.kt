package dev.mahdidroid.compose_lock.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.activities.vm.LockAction
import dev.mahdidroid.compose_lock.activities.vm.LockIntent
import dev.mahdidroid.compose_lock.activities.vm.LockViewModel
import dev.mahdidroid.compose_lock.utils.AuthResult
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.BiometricMessages
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

internal class AuthenticationActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val vm: LockViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricPrompt = setupBiometricPrompt(onSuccess = {
            vm.sendIntent(LockIntent.OnReceiveAuthResult(AuthResult.FINGERPRINT_SUCCESS))
        }, onFailed = {
            vm.sendIntent(LockIntent.OnReceiveAuthResult(AuthResult.FINGERPRINT_FAILED))
        }, onError = {
            vm.sendIntent(LockIntent.OnReceiveAuthResult(AuthResult.FINGERPRINT_ERROR))
        })
        promptInfo = buildPromptInfo(vm.viewState.value.messages.biometricMessages)
        if (vm.isAcceptFingerprint()) displayBiometricPrompt()
        setContent {

            LaunchedEffect(Unit) {
                vm.actions.collect { action ->
                    when (action) {
                        LockAction.FinishAuthActivity -> finish()
                    }
                }
            }

            val currentTheme =
                if (vm.viewState.value.isSingleTheme) vm.viewState.value.lightTheme else {
                    if (isSystemInDarkTheme()) vm.viewState.value.darkTheme else vm.viewState.value.lightTheme
                }
            AuthenticationContent(modifier = Modifier.fillMaxSize(),
                state = vm.viewState.value.currentAuthState,
                theme = currentTheme,
                pinTitleMessage = vm.viewState.value.messages.pinTitleMessage,
                changePinTitleMessages = vm.viewState.value.messages.changePinTitleMessages,
                isFingerprintsEnabled = vm.isAcceptFingerprint(),
                onBiometricPrompt = {
                    displayBiometricPrompt()
                },
                onSendResult = {
                    vm.sendIntent(LockIntent.OnReceiveAuthResult(it))
                },
                onNavigateToChangePin = {
                    vm.sendIntent(LockIntent.OnUpdateScreenState(AuthState.ChangePin))
                },
                onSetNewPin = {
                    vm.loadAuthState {
                        vm.sendIntent(LockIntent.NavigateToMainScreen)
                    }
                })
        }
    }

    private fun displayBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun setupBiometricPrompt(
        onSuccess: () -> Unit, onError: () -> Unit, onFailed: () -> Unit
    ): BiometricPrompt {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        })
    }

    private fun buildPromptInfo(messages: BiometricMessages): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle(messages.title).setSubtitle(messages.subtitle)
            .setNegativeButtonText(messages.negativeButtonText).build()
}