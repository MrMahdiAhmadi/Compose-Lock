package dev.mahdidroid.compose_lock.utils

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.theme.darkThemePinEntryData
import dev.mahdidroid.compose_lock.theme.lightThemePinEntryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class LockViewModel(private val dataStore: ComposeLockPreferences) :
    MVIBaseViewModel<LockViewState, LockIntent, LockAction>() {

    override val initialState: LockViewState
        get() = LockViewState()

    fun loadAuthState(onStateUpdated: () -> Unit) {
        viewModelScope.launch {
            val state = dataStore.getAuthState()
            publishViewState(
                viewState.value.copy(
                    currentAuthState = state, defaultAuthState = state
                )
            )
            onStateUpdated()
        }
    }

    override fun onIntent(intent: LockIntent) {
        when (intent) {
            is LockIntent.OnDayNightTheme -> publishViewState(
                viewState.value.copy(
                    lightTheme = intent.lightTheme, darkTheme = intent.darkTheme
                )
            )

            is LockIntent.OnSingleTheme -> publishViewState(
                viewState.value.copy(
                    lightTheme = intent.theme, isSingleTheme = true
                )
            )

            is LockIntent.OnLockMessagesChange -> publishViewState(viewState.value.copy(messages = intent.messages))

            is LockIntent.OnNavigateToMainScreen -> {
                publishViewState(
                    viewState.value.copy(
                        currentAuthState = AuthState.NoAuth
                    )
                )
                sendAction(LockAction.SendActivityResult(intent.resultCode))
            }

            is LockIntent.OnUpdateScreenState -> publishViewState(
                viewState.value.copy(
                    currentAuthState = intent.value
                )
            )

            LockIntent.OnStop -> {
                if (viewState.value.defaultAuthState != AuthState.NoAuth) publishViewState(
                    viewState.value.copy(
                        currentAuthState = viewState.value.defaultAuthState
                    )
                )
            }

            is LockIntent.OnSetDefaultAuth -> {
                viewModelScope.launch(Dispatchers.Main) {
                    dataStore.updateAuthState(intent.value.name)
                }
                publishViewState(viewState.value.copy(defaultAuthState = intent.value))
            }
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
    val currentAuthState: AuthState = AuthState.NoAuth,
    val defaultAuthState: AuthState = AuthState.NoAuth
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
}

internal sealed class LockAction : UiAction {
    data class SendActivityResult(val code: Int) : LockAction()
}