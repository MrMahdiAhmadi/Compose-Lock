package dev.mahdidroid.compose_lock.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class MVIBaseViewModel<STATE : UiViewState, INTENT : UiIntent, ACTION : UiAction> :
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

    protected fun sendAction(action: ACTION) {
        viewModelScope.launch {
            _actions.emit(action)
        }
    }

    protected fun publishViewState(newState: STATE) {
        _state.value = newState
    }

}

interface UiIntent
interface UiViewState
interface UiAction