package dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.utils.AuthStateManager
import dev.mahdidroid.compose_lock.utils.MVIBaseViewModel
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class NewPinViewModel(
    private val dataStore: ComposeLockPreferences,
    private val authStateManager: AuthStateManager,
    private val newPin: String,
    private val title: String,
    private val pinNotMatchTitle: String
) : MVIBaseViewModel<SetPinViewState, SetPinIntent, SetPinAction>() {

    override val initialState: SetPinViewState
        get() = SetPinViewState()

    init {
        if (newPin.isNotEmpty()) publishViewState(viewState.value.copy(currentPinMaxLength = newPin.length))
        publishViewState(viewState.value.copy(title = title))
    }

    override fun onIntent(intent: SetPinIntent) {
        when (intent) {
            is SetPinIntent.OnUpdatePin -> {
                if (!viewState.value.acceptPin) {
                    if (viewState.value.pin.length == viewState.value.maxLength) sendAction(
                        SetPinAction.ShakePin
                    )
                    return
                }
                publishViewState(
                    viewState.value.copy(
                        pin = viewState.value.pin + intent.value
                    )
                )
                updatePinLength()
                checkShowNextScreenButton()
                if (newPin.isEmpty()) {
                    if (viewState.value.pin.length == viewState.value.maxLength) publishViewState(
                        viewState.value.copy(acceptPin = false)
                    )
                } else if (viewState.value.pin.length == viewState.value.currentPinMaxLength) {
                    publishViewState(viewState.value.copy(acceptPin = false))
                    checkPin()
                }
            }

            SetPinIntent.OnResetPin -> {
                publishViewState(
                    viewState.value.copy(
                        pin = "", acceptPin = true, showNextScreenButton = false
                    )
                )
                updatePinLength()
            }

            SetPinIntent.OnRemovePin -> {
                if (viewState.value.pin.isNotEmpty()) {
                    publishViewState(
                        viewState.value.copy(
                            pin = viewState.value.pin.dropLast(
                                1
                            ), acceptPin = true
                        )
                    )
                    checkShowNextScreenButton()
                    updatePinLength()
                }
            }

            SetPinIntent.OnNextScreenClick -> sendAction(SetPinAction.NavigateToConfirmPin(viewState.value.pin))
        }
    }

    private fun checkShowNextScreenButton() {
        if (newPin.isEmpty()) publishViewState(viewState.value.copy(showNextScreenButton = viewState.value.pin.length >= viewState.value.minLength))
    }

    private fun updatePinLength() {
        if (newPin.isEmpty()) publishViewState(viewState.value.copy(currentPinMaxLength = viewState.value.pin.length))
    }

    private fun checkPin() {
        viewModelScope.launch {
            if (newPin == viewState.value.pin) {
                dataStore.updatePin(viewState.value.pin)
                dataStore.updatePinLength(viewState.value.pin.length)
                publishViewState(viewState.value.copy(pin = ""))
                authStateManager.navigateToMainScreen()
            } else {
                publishViewState(viewState.value.copy(title = pinNotMatchTitle))
                sendAction(SetPinAction.ShowErrorPin)
                delay(2000)
                publishViewState(viewState.value.copy(title = title))
            }
        }
    }
}

data class SetPinViewState(
    val title: String = "",
    val pin: String = "",
    val minLength: Int = 4,
    val maxLength: Int = 8,
    val currentPinMaxLength: Int = 0,
    val acceptPin: Boolean = true,
    val showNextScreenButton: Boolean = false
) : UiViewState

sealed class SetPinIntent : UiIntent {
    data class OnUpdatePin(val value: String) : SetPinIntent()
    data object OnRemovePin : SetPinIntent()
    data object OnResetPin : SetPinIntent()
    data object OnNextScreenClick : SetPinIntent()
}

sealed class SetPinAction : UiAction {
    data object ShowErrorPin : SetPinAction()
    data object ShakePin : SetPinAction()
    data class NavigateToConfirmPin(val pin: String) : SetPinAction()
}