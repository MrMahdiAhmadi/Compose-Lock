package dev.mahdidroid.compose_lock.ui.pin.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class PinIndicatorTheme(
    val filledColor: Color,
    val unfilledColor: Color,
    val borderColor: Color,
    val errorColor: Color,
    val errorBoarderColor: Color
)

@Composable
fun PinIndicator(
    modifier: Modifier = Modifier, isFilled: Boolean, theme: PinIndicatorTheme
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .size(15.dp)
            .clip(CircleShape)
            .background(if (isFilled) theme.filledColor else theme.unfilledColor)
            .border(2.dp, theme.borderColor, CircleShape)
    )
}