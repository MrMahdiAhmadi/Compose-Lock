package dev.mahdidroid.compose_lock.theme

import androidx.compose.ui.graphics.Color
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme


internal val lightThemePinEntryData = PinEntryData(
    backgroundColor = Color(0xFFFAFAFA), // Light grey background
    titleColor = Color(0xFF333333), // Darker grey for better contrast
    pinIndicatorTheme = PinIndicatorTheme(
        filled = true, // Filled indicators for better visibility
        filledColor = Color(0xFF00796B), // Teal
        unfilledColor = Color(0xFFB2DFDB), // Lighter teal
        borderColor = Color(0xFF004D40) // Darker teal
    ),
    numberButtonTheme = NumberButtonTheme(
        buttonColor = Color(0xFFE0F2F1), // Light teal
        textColor = Color(0xFF00796B) // Teal
    ),
    iconButtonTheme = IconButtonTheme(
        iconColor = Color(0xFF004D40), // Dark teal
        backgroundColor = Color.Transparent // Transparent background
    )
)

internal val darkThemePinEntryData = PinEntryData(
    backgroundColor = Color(0xFF212121), // Dark grey background
    titleColor = Color(0xFFE0E0E0), // Light grey for contrast
    pinIndicatorTheme = PinIndicatorTheme(
        filled = true, // Filled indicators for better visibility
        filledColor = Color(0xFF64FFDA), // Bright teal
        unfilledColor = Color(0xFF004D40), // Dark teal
        borderColor = Color(0xFF80CBC4) // Medium teal
    ),
    numberButtonTheme = NumberButtonTheme(
        buttonColor = Color(0xFF37474F), // Darker grey with a blue hint
        textColor = Color(0xFF80CBC4) // Light teal
    ),
    iconButtonTheme = IconButtonTheme(
        iconColor = Color(0xFF80CBC4), // Light teal
        backgroundColor = Color(0xFF004D40).copy(alpha = 0.6f) // Semi-transparent dark teal
    )
)
