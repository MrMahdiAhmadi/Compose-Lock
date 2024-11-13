package dev.mahdidroid.compose_lock.utils

import dev.mahdidroid.compose_lock.R

sealed class AuthState(val name: Int) {
    data object Pin : AuthState(R.string.AuthStatePin)
    data object Password : AuthState(R.string.AuthStatePassword)
    data object ChangePin : AuthState(R.string.AuthStateChangePin)
    data object ChangePassword : AuthState(R.string.AuthStateChangePassword)
    data object NoAuth : AuthState(R.string.AuthStateNoAuth)
}

sealed class LockActions {
    data class OnOpenScreenNow(val value: AuthState) : LockActions()
    data class OnSetDefaultValue(val value: AuthState) : LockActions()
}