package dev.mahdidroid.compose_lock.ui.pin

import androidx.lifecycle.viewModelScope
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.MVIBaseViewModel
import dev.mahdidroid.compose_lock.utils.UiAction
import dev.mahdidroid.compose_lock.utils.UiIntent
import dev.mahdidroid.compose_lock.utils.UiViewState
import dev.mahdidroid.compose_lock.utils.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class PinViewModel(
    private val dataStore: ComposeLockPreferences, private val isChangePassword: Boolean
) : MVIBaseViewModel<PinViewState, PinIntent, PinAction>() {

    override val initialState: PinViewState
        get() = PinViewState()

    init {
        viewModelScope.launch {
            dataStore.getPinLength().collect {
                if (it == 0) {
                    sendAction(PinAction.NavigateToChangePin)
                } else {
                    publishViewState(viewState.value.copy(maxLength = it))
                }
            }
        }
       /* viewModelScope.launch {
            dataStore.getUnlockDuration().collect {
                if (it != 0L) {
                    sendIntent(PinIntent.OnStartTimer(it))
                }
            }
        }*/
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

            is PinIntent.OnStartTimer -> {
                publishViewState(viewState.value.copy(acceptPin = false))
                viewModelScope.launch {
                    var remainingTime = intent.value - System.currentTimeMillis()
                    while (remainingTime > 0) {
                        publishViewState(viewState.value.copy(timer = remainingTime.formatTime()))
                        delay(1000)
                        remainingTime -= 1000
                    }
                    publishViewState(viewState.value.copy(acceptPin = true, timer = ""))
                }
            }
        }
    }

    private fun checkPin() {
        viewModelScope.launch {
            if (isChangePassword) {
                sendAction(PinAction.NavigateToChangePin)
                publishViewState(viewState.value.copy(pin = "", acceptPin = true))
            } else {
                val currentPin = dataStore.getPin().first()
                if (currentPin == viewState.value.pin) {
                    sendAction(PinAction.NavigateToMainScreen)
                } else {
                    sendAction(PinAction.ShowErrorPin)
                }
            }
        }
    }
}

data class PinViewState(
    val pin: String = "",
    val maxLength: Int = 4,
    val acceptPin: Boolean = true,
    val timer: String = ""
) : UiViewState

sealed class PinIntent : UiIntent {
    data class OnUpdatePin(val value: String) : PinIntent()
    data object OnRemovePin : PinIntent()
    data object OnResetPin : PinIntent()
    data class OnStartTimer(val value: Long) : PinIntent()
}

sealed class PinAction : UiAction {
    data object ShowErrorPin : PinAction()
    data object NavigateToChangePin : PinAction()
    data object NavigateToMainScreen : PinAction()
}