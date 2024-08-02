package dev.mahdidroid.compose_lock.utils

import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.theme.darkThemePinEntryData
import dev.mahdidroid.compose_lock.theme.lightThemePinEntryData

internal class LockViewModel(private val authManager: AuthStateManager) :
    MVIBaseViewModel<LockViewState, LockIntent, LockAction>() {

    val state = authManager.currentState

    override val initialState: LockViewState
        get() = LockViewState()

    override fun onIntent(intent: LockIntent) {
        when (intent) {
            is LockIntent.OnTwoTheme -> {
                publishViewState(
                    viewState.value.copy(
                        lightTheme = intent.lightTheme, darkTheme = intent.darkTheme
                    )
                )
            }

            is LockIntent.OnSingleTheme -> {
                publishViewState(
                    viewState.value.copy(
                        lightTheme = intent.theme, isSingleTheme = true
                    )
                )
            }

            LockIntent.OnHandleAuthSuccess -> authManager.updateScreen(AuthState.Main)

            is LockIntent.OnSwitchScreen -> authManager.updateScreen(intent.value)
            is LockIntent.OnLockMessagesChange -> publishViewState(viewState.value.copy(messages = intent.messages))
        }
    }
}

internal data class LockViewState(
    val isSingleTheme: Boolean = false,
    val lightTheme: LockTheme = LockTheme(pinTheme = lightThemePinEntryData),
    val darkTheme: LockTheme = LockTheme(
        pinTheme = darkThemePinEntryData
    ),
    val messages: LockMessages = LockMessages()
) : UiViewState

internal sealed class LockIntent : UiIntent {
    data class OnTwoTheme(
        val lightTheme: LockTheme, val darkTheme: LockTheme
    ) : LockIntent()

    data class OnSingleTheme(
        val theme: LockTheme
    ) : LockIntent()

    data class OnSwitchScreen(val value: AuthState) : LockIntent()
    data object OnHandleAuthSuccess : LockIntent()
    data class OnLockMessagesChange(val messages: LockMessages) : LockIntent()
}

internal sealed class LockAction : UiAction