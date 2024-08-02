package dev.mahdidroid.compose_lock.utils

data class LockMessages(
    val pinTitleMessage: String = "Enter your pin",
    val biometricMessages: BiometricMessages = BiometricMessages()
)

data class BiometricMessages(
    val title: String = "Biometric login for my app",
    val subtitle: String = "Log in using your biometric credential",
    val negativeButtonText: String = "Use account password"
)