package dev.mahdidroid.compose_lock.utils

import androidx.compose.runtime.compositionLocalOf

/**
 * `LocalAuthAction` is a CompositionLocal that provides a mechanism for handling authentication-related actions
 * across different Composables.
 *
 * It allows Composables to access and invoke authentication actions, such as opening an authentication screen
 * or changing authentication settings, without directly interacting with the `ViewModel` or Activity.
 *
 * The function provided by `LocalAuthAction` will receive a `LockActions` object, which encapsulates
 * specific actions like opening the PIN screen or setting the default authentication method.
 *
 * Example usage:
 * ```
 * val auth = LocalAuthAction.current
 * auth.invoke(LockActions.OnOpenScreenNow(AuthState.Pin))
 * ```
 */
 val LocalAuthAction = compositionLocalOf<(LockActions) -> Unit> {
    error("No AuthAction function provided")
}