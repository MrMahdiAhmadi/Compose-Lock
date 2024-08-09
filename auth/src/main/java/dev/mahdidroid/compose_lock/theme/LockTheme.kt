package dev.mahdidroid.compose_lock.theme

import dev.mahdidroid.compose_lock.ui.pin.PinEntryData

/*data class TestTheme(
    val singleTheme: LockTheme? = null, val twoTheme: TwoTheme? = null
)

data class TwoTheme(
    val lightTheme: LockTheme, val darkTheme: LockTheme
)*/

data class LockTheme(
    val pinTheme: PinEntryData
)