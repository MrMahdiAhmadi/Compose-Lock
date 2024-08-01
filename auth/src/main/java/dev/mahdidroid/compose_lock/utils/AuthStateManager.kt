package dev.mahdidroid.compose_lock.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AuthStateManager {
    private val _currentState = MutableStateFlow<AuthState>(AuthState.Pin)
    val currentState: StateFlow<AuthState> = _currentState.asStateFlow()

    fun updateScreen(value: AuthState) {
        _currentState.value = value
    }

    fun navigateToMainScreen() {
        _currentState.value = AuthState.Main
    }
}