package dev.mahdidroid.compose_lock.activities.vm

import dev.mahdidroid.compose_lock.auth.ComposeLockRetryPolicy
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.theme.darkThemePinEntryData
import dev.mahdidroid.compose_lock.theme.lightThemePinEntryData
import dev.mahdidroid.compose_lock.utils.AuthResult
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockMessages
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState

internal data class LockViewState(
    val isSingleTheme: Boolean = false,
    val lightTheme: LockTheme = LockTheme(pinTheme = lightThemePinEntryData),
    val darkTheme: LockTheme = LockTheme(
        pinTheme = darkThemePinEntryData
    ),
    val messages: LockMessages = LockMessages(),
    val retryPolicy: ComposeLockRetryPolicy = ComposeLockRetryPolicy(),
    val currentAuthState: AuthState = AuthState.NoAuth,
    val defaultAuthState: AuthState = AuthState.NoAuth,
    val failedCount: Int = 0,
    val lockDuration: Long = 0,
    val lockTitle: String = ""
) : UiViewState

internal sealed class LockIntent : UiIntent {
    data class OnDayNightTheme(
        val lightTheme: LockTheme, val darkTheme: LockTheme
    ) : LockIntent()

    data class OnSingleTheme(
        val theme: LockTheme
    ) : LockIntent()

    data class OnLockMessagesChange(val messages: LockMessages) : LockIntent()

    data class OnNavigateToMainScreen(val resultCode: Int) : LockIntent()
    data class OnUpdateScreenState(val value: AuthState) : LockIntent()
    data object OnStop : LockIntent()
    data class OnSetDefaultAuth(val value: AuthState) : LockIntent()

    data class OnRetryPolicyChange(val composeLockRetryPolicy: ComposeLockRetryPolicy) :
        LockIntent()

    data class OnReceiveAuthResult(val authResult: AuthResult) : LockIntent()
    data object NavigateToMainScreen : LockIntent()
    data object OnFailed : LockIntent()
}

internal sealed class LockAction : UiAction