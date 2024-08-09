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

    suspend fun loadAuthState(): Boolean {
        var returnValue = false
        val it = dataStore.getAuthState()
        if (viewState.value.defaultAuthState == AuthState.Loading) {
            publishViewState(
                viewState.value.copy(
                    currentAuthState = it, defaultAuthState = it
                )
            )
            if (it != AuthState.NoAuth) returnValue = true

        } else publishViewState(
            viewState.value.copy(
                defaultAuthState = it
            )
        )
        return returnValue
    }

    override fun onIntent(intent: LockIntent) {
        when (intent) {
            is LockIntent.OnTwoTheme -> publishViewState(
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

            LockIntent.OnNavigateToMainScreen -> publishViewState(
                viewState.value.copy(
                    currentAuthState = AuthState.NoAuth
                )
            )

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

            is LockIntent.OnOpenAuthNow -> {
                publishViewState(viewState.value.copy(currentAuthState = intent.value))
                sendAction(LockAction.StartAuthenticationActivity)
            }

            is LockIntent.OnSetDefaultAuth -> {
                viewModelScope.launch(Dispatchers.Main) {
                    dataStore.updateAuthState(intent.value.name)
                }
                publishViewState(viewState.value.copy(defaultAuthState = intent.value))
            }

            LockIntent.ResetShouldStartAuthActivity -> publishViewState(
                viewState.value.copy(
                    shouldStartAuthActivity = false
                )
            )
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
    val currentAuthState: AuthState = AuthState.Loading,
    val defaultAuthState: AuthState = AuthState.Loading,
    val shouldStartAuthActivity: Boolean = false
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
    data object OnStop : LockIntent()
    data class OnOpenAuthNow(val value: AuthState) : LockIntent()
    data class OnSetDefaultAuth(val value: AuthState) : LockIntent()
    data object ResetShouldStartAuthActivity : LockIntent()
}

internal sealed class LockAction : UiAction {
    data object StartAuthenticationActivity : LockAction()
}