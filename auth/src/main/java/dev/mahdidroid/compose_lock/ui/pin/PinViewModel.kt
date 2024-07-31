package dev.mahdidroid.compose_lock.ui.pin

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.utils.AuthenticationStateManager
import dev.mahdidroid.compose_lock.utils.BaseViewModel
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState
import kotlinx.coroutines.launch

class PinViewModel(
    private val dataStore: ComposeLockPreferences,
    private val a: AuthenticationStateManager
) : BaseViewModel<PinViewState, PinIntent, PinAction>(){

    override val initialState: PinViewState
        get() = PinViewState()

    init {
        viewModelScope.launch {
            dataStore.getPinLength().collect {
                if (it == 0) {
                    publishViewState(viewState.value.copy(isShowSetPin = true))
                } else publishViewState(viewState.value.copy(maxLength = it))
            }
        }
    }

    override fun onIntent(intent: PinIntent) {
        when (intent) {
            is PinIntent.UpdateIsAuthenticated -> publishViewState(
                viewState.value.copy(
                    isAuthenticated = intent.value
                )
            )

            is PinIntent.UpdatePin -> {
                publishViewState(viewState.value.copy(pin = intent.value))
                if (viewState.value.pin.length == viewState.value.maxLength) {
                    if (viewState.value.isShowSetPin) {

                    } else {
                        checkPin()
                    }
                }
            }

            is PinIntent.UpdatePinRepeat -> {
                publishViewState(viewState.value.copy(pinRepeat = intent.value))
                if (viewState.value.pinRepeat.length == viewState.value.maxLength) {
                    if (viewState.value.pin == viewState.value.pinRepeat) {
                        viewModelScope.launch {
                            dataStore.updatePin(viewState.value.pin)
                        }
                    }
                }
            }
        }
    }

    private fun checkPin() {

    }
}

 data class PinViewState(

    val isAuthenticated: Boolean = false,
    val pin: String = "",
    val pinRepeat: String = "",
    val maxLength: Int = 4,
    val isShowSetPin: Boolean = false
) : UiViewState

 sealed class PinIntent : UiIntent {
    data class UpdateIsAuthenticated(val value: Boolean) : PinIntent()
    data class UpdatePin(val value: String) : PinIntent()
    data class UpdatePinRepeat(val value: String) : PinIntent()
}

 sealed class PinAction : UiAction {
    object NavigateToHome : PinAction()
}