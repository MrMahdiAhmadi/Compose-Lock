package dev.mahdidroid.compose_lock.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE : UiViewState, INTENT : UiIntent, ACTION : UiAction> :
    ViewModel() {

    protected abstract val initialState: STATE

    private val _state by lazy { mutableStateOf(initialState) }
    private val _actions by lazy { MutableSharedFlow<ACTION>() }

    val viewState get() = _state
    val actions get() = _actions.asSharedFlow()

    fun sendIntent(intent: INTENT) {
        onIntent(intent)
    }

    protected abstract fun onIntent(intent: INTENT)

    fun sendAction(action: ACTION) {
        viewModelScope.launch {
            _actions.emit(action)
        }
    }

    fun publishViewState(newState: STATE) {
        _state.value = newState
    }

}

// MVI Intent, Use for passing user event to ViewModel
interface UiIntent

// MVI ViewState
interface UiViewState

// MVI Action (or side effect), Use for fire one time events from ViewModel to View
interface UiAction
