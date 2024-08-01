package dev.mahdidroid.compose_lock.utils

internal sealed class AuthState(val name: String) {
    data object Pin : AuthState("pin")
    data object Password : AuthState("password")
    data object ChangePin : AuthState("changePin")
    data object ChangePassword : AuthState("changePassword")
    data object Main : AuthState("main")
}