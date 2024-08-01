package dev.mahdidroid.compose_lock.utils

import androidx.compose.material.lightColors
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class LockViewModel(private val authManager: AuthStateManager) : ViewModel() {

    val state = authManager.currentState
    private val _theme =
        MutableStateFlow<ThemeConfiguration>(ThemeConfiguration.Material2Config(lightColors()))
    val theme: StateFlow<ThemeConfiguration> = _theme.asStateFlow()

    fun handleAuthSuccess() {
        authManager.updateScreen(AuthState.Main)
    }

    fun switchScreen(value: AuthState) {
        authManager.updateScreen(value)
    }

    fun setTheme(themeConfiguration: ThemeConfiguration) {
        viewModelScope.launch {
            _theme.emit(themeConfiguration)
        }
    }
}