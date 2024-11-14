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
    val showPinOnResume: Boolean = true,
    val isFingerprintsEnabled: Boolean = true
) : UiViewState

internal sealed class LockIntent : UiIntent {

    /**
     * Represents an intent to update the day and night theme settings.
     * - `lightTheme`: The theme to be applied during the day or light mode.
     * - `darkTheme`: The theme to be applied during the night or dark mode.
     */
    data class OnDayNightTheme(
        val lightTheme: LockTheme, val darkTheme: LockTheme
    ) : LockIntent()

    /**
     * Represents an intent to switch to a single theme mode.
     * - `theme`: The theme to be used as the single (uniform) theme throughout the app.
     */
    data class OnSingleTheme(
        val theme: LockTheme
    ) : LockIntent()

    /**
     * Represents an intent to update lock messages.
     * - `messages`: The new set of messages to be displayed for authentication-related feedback.
     */
    data class OnLockMessagesChange(val messages: LockMessages) : LockIntent()

    /**
     * Represents an intent to update the screen's current authentication state.
     * - `value`: The new authentication state to be applied (e.g., PIN, password, no authentication).
     */
    data class OnUpdateScreenState(val value: AuthState) : LockIntent()

    /**
     * Represents an intent to set a new default authentication method.
     * - `value`: The authentication state to be set as the default (e.g., PIN, password, no authentication).
     */
    data class OnSetDefaultAuth(val value: AuthState) : LockIntent()

    /**
     * Represents an intent to update the retry policy for authentication attempts.
     * - `composeLockRetryPolicy`: The new retry policy settings.
     */
    data class OnRetryPolicyChange(val composeLockRetryPolicy: ComposeLockRetryPolicy) :
        LockIntent()

    /**
     * Represents an intent to handle the result of an authentication attempt.
     * - `authResult`: The result of the authentication process (e.g., success, failure, error).
     */
    data class OnReceiveAuthResult(val authResult: AuthResult) : LockIntent()

    /**
     * Represents an intent to navigate directly to the main screen, typically used after
     * confirming successful authentication.
     */
    data object NavigateToMainScreen : LockIntent()

    /**
     * Represents an intent triggered when an authentication attempt fails.
     * It increments the failed attempt count and possibly triggers delay mechanisms based on retry policies.
     */
    data object OnFailed : LockIntent()

    data object OnShowPinOnResume : LockIntent()

    data class OnFingerprintStateChange(val value: Boolean) : LockIntent()
}

internal sealed class LockAction : UiAction {
    data object FinishAuthActivity : LockAction()
}