package dev.mahdidroid.compose_lock.theme

import dev.mahdidroid.compose_lock.ui.pin.PinEntryData



sealed class ComposeLockTheme {
    data class SingleTheme(val theme: LockTheme) : ComposeLockTheme()
    data class DayNightTheme(val lightTheme: LockTheme, val darkTheme: LockTheme) : ComposeLockTheme()
}

data class LockTheme(
    val pinTheme: PinEntryData
)