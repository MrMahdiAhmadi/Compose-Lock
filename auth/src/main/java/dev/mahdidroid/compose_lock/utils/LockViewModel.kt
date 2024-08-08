package dev.mahdidroid.compose_lock.utils

import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.theme.darkThemePinEntryData
import dev.mahdidroid.compose_lock.theme.lightThemePinEntryData

internal class LockViewModel(private val dataStore: ComposeLockPreferences) :
    MVIBaseViewModel<LockViewState, LockIntent, LockAction>() {

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

            is LockIntent.OnLockMessagesChange -> publishViewState(viewState.value.copy(messages = intent.messages))

            LockIntent.OnNavigateToMainScreen -> publishViewState(viewState.value.copy(authState = AuthState.Main))

            is LockIntent.OnUpdateScreenState -> publishViewState(viewState.value.copy(authState = intent.value))
        }
    }
}

internal data class LockViewState(
    val isSingleTheme: Boolean = false,
    val lightTheme: LockTheme = LockTheme(pinTheme = lightThemePinEntryData),
    val darkTheme: LockTheme = LockTheme(
        pinTheme = darkThemePinEntryData
    ),
    val messages: LockMessages = LockMessages(),
    val authState: AuthState = AuthState.Pin
) : UiViewState

internal sealed class LockIntent : UiIntent {
    data class OnTwoTheme(
        val lightTheme: LockTheme, val darkTheme: LockTheme
    ) : LockIntent()

    data class OnSingleTheme(
        val theme: LockTheme
    ) : LockIntent()

    data class OnLockMessagesChange(val messages: LockMessages) : LockIntent()
    data object OnNavigateToMainScreen : LockIntent()
    data class OnUpdateScreenState(val value: AuthState) : LockIntent()
}

internal sealed class LockAction : UiAction