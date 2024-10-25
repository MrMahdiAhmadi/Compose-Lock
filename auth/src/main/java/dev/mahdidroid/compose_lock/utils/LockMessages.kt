package dev.mahdidroid.compose_lock.utils

data class LockMessages(
    val pinTitleMessage: String = "Enter your pin",
    val changePinTitleMessages: ChangePinTitleMessages = ChangePinTitleMessages(),
    val biometricMessages: BiometricMessages = BiometricMessages()
)

data class BiometricMessages(
    val title: String = "Biometric login for my app",
    val subtitle: String = "Log in using your biometric credential",
    val negativeButtonText: String = "Use account password"
)

data class ChangePinTitleMessages(
    val enterCurrentPinTitle: String = "Enter your current pin",
    val enterNewPinTitle: String = "Enter your new pin",
    val confirmNewPinTitle: String = "Confirm your new pin",
    val pinNotMatchTitle: String = "Pin not match"
)