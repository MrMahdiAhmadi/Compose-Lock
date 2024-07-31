package dev.mahdidroid.compose_lock.utils

import androidx.lifecycle.ViewModel

class AuthViewModel(private val authManager: AuthenticationStateManager) : ViewModel() {

    val state = authManager.currentState

    fun onAuthSuccess() {
        authManager.changeScreen(AuthScreen.Main)
    }

    fun changeScreen(value: AuthScreen) {
        authManager.changeScreen(value)
    }
}


sealed class AuthScreen(val name: String) {
    object Pin : AuthScreen("pin")
    object Password : AuthScreen("password")
    object ChangePin : AuthScreen("changePin")
    object ChangePassword : AuthScreen("changePassword")
    object Main : AuthScreen("main")
}