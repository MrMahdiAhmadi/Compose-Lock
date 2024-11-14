package dev.mahdidroid.compose_lock.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.composable.PinIndicatorTheme
import dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin.NewPinConfirm

internal val lightThemePinEntryData = PinEntryData(
    backgroundColor = Color(0xFFFAFAFA),
    titleColor = Color(0xFF333333),
    pinIndicatorTheme = PinIndicatorTheme(
        filledColor = Color(0xFF00796B),
        unfilledColor = Color(0xFFB2DFDB),
        borderColor = Color(0xFF004D40),
        errorColor = Color(0xFFE57373),
        errorBoarderColor = Color(0xFFC62828)
    ),
    numberButtonTheme = NumberButtonTheme(
        buttonColor = Color(0xFFE0F2F1), textColor = Color(0xFF00796B)
    ),
    iconButtonTheme = IconButtonTheme(
        iconColor = Color(0xFF004D40), backgroundColor = Color.Transparent
    ),
    newPinConfirm = NewPinConfirm(
        containerColor = Color(0xFF00796B),
        contentColor = Color.Black,
        shape = RoundedCornerShape(16.dp)
    )
)

internal val darkThemePinEntryData = PinEntryData(
    backgroundColor = Color(0xFF212121),
    titleColor = Color(0xFFE0E0E0),
    pinIndicatorTheme = PinIndicatorTheme(
        filledColor = Color(0xFF64FFDA),
        unfilledColor = Color(0xFF004D40),
        borderColor = Color(0xFF80CBC4),
        errorColor = Color(0xFFFF5252),
        errorBoarderColor = Color(0xFFD50000)
    ),
    numberButtonTheme = NumberButtonTheme(
        buttonColor = Color(0xFF37474F), textColor = Color(0xFF80CBC4)
    ),
    iconButtonTheme = IconButtonTheme(
        iconColor = Color(0xFF80CBC4), backgroundColor = Color(0xFF004D40).copy(alpha = 0.6f)
    ),
    newPinConfirm = NewPinConfirm(
        containerColor = Color(0xFF80CBC4),
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
)
