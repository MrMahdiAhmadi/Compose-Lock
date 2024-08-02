package dev.mahdidroid.compose_lock.ui.set_pin

import kotlinx.serialization.Serializable

@Serializable
sealed class SetPinScreens {
    @Serializable
    data object EnterCurrentPin : SetPinScreens()

    @Serializable
    data object NewPin : SetPinScreens()

    @Serializable
    data class ConfirmNewPin(val newPin: String) : SetPinScreens()
}