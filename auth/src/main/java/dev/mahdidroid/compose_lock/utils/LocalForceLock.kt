package dev.mahdidroid.compose_lock.utils

import androidx.compose.runtime.compositionLocalOf

 val LocalAuthAction = compositionLocalOf<(LockActions) -> Unit> {
    error("No forceLock function provided")
}