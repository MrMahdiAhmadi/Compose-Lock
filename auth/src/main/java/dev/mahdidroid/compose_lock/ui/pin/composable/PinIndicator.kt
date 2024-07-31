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

@Composable
private fun PinIndicator(
    filled: Boolean, filledColor: Color = Color.White, unfilledColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(15.dp)
            .clip(CircleShape)
            .background(if (filled) filledColor else unfilledColor)
            .border(2.dp, Color.White, CircleShape)
    )
}