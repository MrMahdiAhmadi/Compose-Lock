package dev.mahdidroid.compose_lock.ui.pin

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.utils.MVIBaseViewModel
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState
import kotlinx.coroutines.launch

internal class PinViewModel(
    private val dataStore: ComposeLockPreferences, private val isChangePassword: Boolean
) : MVIBaseViewModel<PinViewState, PinIntent, PinAction>() {

    override val initialState: PinViewState
        get() = PinViewState(isChangePassword = isChangePassword)

    init {
        viewModelScope.launch {
            dataStore.getPinLength().collect {
                publishViewState(viewState.value.copy(maxLength = 4))/*if (it == 0) {
                    if (!isChangePassword) sendAction(PinAction.NavigateToChangePassword) else sendAction(
                        PinAction.NavigateToChangePassword
                    )
                }*/
            }
        }
    }

    override fun onIntent(intent: PinIntent) {
        when (intent) {
            is PinIntent.OnUpdatePin -> {
                if (!viewState.value.acceptPin) return
                publishViewState(viewState.value.copy(pin = viewState.value.pin + intent.value))
                if (viewState.value.pin.length == viewState.value.maxLength) {
                    publishViewState(viewState.value.copy(acceptPin = false))
                    checkPin()
                }
            }

            PinIntent.OnResetPin -> publishViewState(
                viewState.value.copy(
                    pin = "", acceptPin = true
                )
            )

            PinIntent.OnRemovePin -> {
                if (viewState.value.pin.isEmpty()) return
                publishViewState(
                    viewState.value.copy(
                        pin = viewState.value.pin.dropLast(
                            1
                        )
                    )
                )
            }
        }
    }

    private fun checkPin() {
        viewModelScope.launch {
            dataStore.getPin().collect {
                if ("1111" == viewState.value.pin) {
                    //todo add success animation
                    if (viewState.value.isChangePassword) {
                        sendAction(PinAction.NavigateToChangePassword)
                        publishViewState(viewState.value.copy(pin = "", acceptPin = true))
                    } else sendAction(PinAction.NavigateToMainScreen)
                } else sendAction(PinAction.ShowErrorPin)
            }
        }
    }
}

data class PinViewState(
    val pin: String = "",
    val maxLength: Int = 4,
    val acceptPin: Boolean = true,
    val isChangePassword: Boolean
) : UiViewState

sealed class PinIntent : UiIntent {
    data class OnUpdatePin(val value: String) : PinIntent()
    data object OnRemovePin : PinIntent()
    data object OnResetPin : PinIntent()
}

sealed class PinAction : UiAction {
    data object ShowErrorPin : PinAction()
    data object NavigateToChangePassword : PinAction()
    data object NavigateToMainScreen : PinAction()
}