package dev.mahdidroid.compose_lock.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationStateManager {
    private val _currentState = MutableStateFlow<AuthScreen>(AuthScreen.Pin)
    val currentState: StateFlow<AuthScreen> = _currentState.asStateFlow()

    fun changeScreen(value: AuthScreen) {
        _currentState.value = value
    }
}