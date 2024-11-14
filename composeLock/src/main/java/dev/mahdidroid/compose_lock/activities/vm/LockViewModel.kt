package dev.mahdidroid.compose_lock.activities.vm

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.auth.ComposeLockRetryPolicy
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.theme.darkThemePinEntryData
import dev.mahdidroid.compose_lock.theme.lightThemePinEntryData
import dev.mahdidroid.compose_lock.utils.AuthResult
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockMessages
import dev.mahdidroid.compose_lock.utils.MVIBaseViewModel
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class LockViewModel(private val dataStore: ComposeLockPreferences) :
    MVIBaseViewModel<LockViewState, LockIntent, LockAction>() {

    override val initialState: LockViewState
        get() = LockViewState()

    init {
        viewModelScope.launch {
            dataStore.getUnlockDuration().collect {
                if (it != 0L) {
                    publishViewState(viewState.value.copy(lockDuration = it))
                }
            }
        }
        viewModelScope.launch {
            dataStore.getFingerprintStatus().collect {
                publishViewState(viewState.value.copy(isFingerprintsEnabled = it))
            }
        }
    }

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

            is LockIntent.OnUpdateScreenState -> publishViewState(
                viewState.value.copy(
                    currentAuthState = intent.value
                )
            )

            is LockIntent.OnSetDefaultAuth -> {
                viewModelScope.launch(Dispatchers.Main) {
                    dataStore.updateAuthState(intent.value.name)
                }
                publishViewState(viewState.value.copy(defaultAuthState = intent.value))
            }

            is LockIntent.OnRetryPolicyChange -> {
                viewModelScope.launch {
                    publishViewState(viewState.value.copy())
                }
            }

            is LockIntent.OnReceiveAuthResult -> {
                when (intent.authResult) {
                    AuthResult.FINGERPRINT_SUCCESS -> sendIntent(LockIntent.NavigateToMainScreen)

                    AuthResult.FINGERPRINT_FAILED -> {}
                    AuthResult.FINGERPRINT_ERROR -> {}
                    AuthResult.PIN_SUCCESS -> sendIntent(LockIntent.NavigateToMainScreen)

                    AuthResult.PIN_FAILED -> sendIntent(LockIntent.OnFailed)

                    AuthResult.PASSWORD_SUCCESS -> sendIntent(LockIntent.NavigateToMainScreen)

                    AuthResult.PASSWORD_FAILED -> sendIntent(LockIntent.OnFailed)
                    AuthResult.Error -> {}
                }
            }

            LockIntent.NavigateToMainScreen -> {
                publishViewState(viewState.value.copy(showPinOnResume = false))
                sendAction(LockAction.FinishAuthActivity)
            }

            LockIntent.OnFailed -> {

                val newFailedCount = viewState.value.failedCount + 1
                publishViewState(viewState.value.copy(failedCount = newFailedCount))

                viewState.value.retryPolicy.retryPolicies[newFailedCount]?.let { delayTime ->
                    viewModelScope.launch(Dispatchers.IO) {
                        val a = System.currentTimeMillis() + delayTime
                        dataStore.updateUnlockDuration(a)
                    }
                }
            }

            LockIntent.OnShowPinOnResume -> publishViewState(viewState.value.copy(showPinOnResume = true))

            is LockIntent.OnFingerprintStateChange -> viewModelScope.launch {
                dataStore.updateFingerprintStatus(
                    intent.value
                )
            }
        }
    }

    fun isAcceptFingerprint(): Boolean =
        viewState.value.isFingerprintsEnabled && (viewState.value.currentAuthState == AuthState.Pin || viewState.value.currentAuthState == AuthState.Password)

    fun isShowLockOnResume(): Boolean =
        viewState.value.defaultAuthState != AuthState.NoAuth && viewState.value.showPinOnResume

}