package dev.mahdidroid.compose_lock.theme

import androidx.compose.ui.graphics.Color
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme


internal val lightThemePinEntryData = PinEntryData(
    backgroundColor = Color.White, titleColor = Color.Black, pinIndicatorTheme = PinIndicatorTheme(
        filled = false,
        filledColor = Color.Black,
        unfilledColor = Color.LightGray,
        borderColor = Color.DarkGray
    ), numberButtonTheme = NumberButtonTheme(
        buttonColor = Color.LightGray, textColor = Color.Black
    ), iconButtonTheme = IconButtonTheme(
        iconColor = Color.Black, backgroundColor = Color.LightGray.copy(alpha = 0.7f)
    )
)

internal val darkThemePinEntryData = PinEntryData(
    backgroundColor = Color.Black, titleColor = Color.White, pinIndicatorTheme = PinIndicatorTheme(
        filled = false,
        filledColor = Color.White,
        unfilledColor = Color.DarkGray,
        borderColor = Color.LightGray
    ), numberButtonTheme = NumberButtonTheme(
        buttonColor = Color.DarkGray, textColor = Color.White
    ), iconButtonTheme = IconButtonTheme(
        iconColor = Color.White, backgroundColor = Color.DarkGray.copy(alpha = 0.7f)
    )
)
