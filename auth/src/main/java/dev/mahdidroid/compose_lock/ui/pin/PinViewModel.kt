package dev.mahdidroid.compose_lock.ui.pin

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.utils.AuthStateManager
import dev.mahdidroid.compose_lock.utils.MVIBaseViewModel
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState
import kotlinx.coroutines.launch

internal class PinViewModel(
    private val dataStore: ComposeLockPreferences, private val authStateManager: AuthStateManager
) : MVIBaseViewModel<PinViewState, PinIntent, PinAction>() {

    override val initialState: PinViewState
        get() = PinViewState()

    init {
        viewModelScope.launch {
            dataStore.getPinLength().collect {
                publishViewState(viewState.value.copy(maxLength = it))
            }
        }
    }

    override fun onIntent(intent: PinIntent) {
        when (intent) {
            is PinIntent.UpdatePin -> {
                publishViewState(viewState.value.copy(pin = intent.value))
                if (viewState.value.pin.length == viewState.value.maxLength) {
                    checkPin()
                }
            }
        }
    }

    private fun checkPin() {
        viewModelScope.launch {
            dataStore.getPin().collect {
                if (it == viewState.value.pin) {
                    authStateManager.navigateToMainScreen()
                }
            }
        }
    }
}

data class PinViewState(
    val pin: String = "",
    val maxLength: Int = 4,
) : UiViewState

sealed class PinIntent : UiIntent {
    data class UpdatePin(val value: String) : PinIntent()
}

sealed class PinAction : UiAction {}