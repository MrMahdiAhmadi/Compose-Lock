package dev.mahdidroid.compose_lock.ui.pin.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mahdidroid.compose_lock.auth.R

data class NumberButtonTheme(
    val buttonColor: Color,
    val textColor: Color,
)

@Composable
internal fun NumberButton(
    modifier: Modifier = Modifier, number: String, theme: NumberButtonTheme, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .size(54.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = theme.buttonColor
        )
    ) {
        Text(text = number, fontSize = 24.sp, color = theme.textColor)
    }
}

data class IconButtonTheme(
    val removeNumberIcon: Int = R.drawable.baseline_backspace_24,
    val displayFingerprintIcon: Int = R.drawable.baseline_fingerprint_24,
    val iconColor: Color,
    val backgroundColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun IconButton(
    modifier: Modifier = Modifier,
    theme: IconButtonTheme,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(CircleShape)
            .size(54.dp)
            .background(theme.backgroundColor)
            .combinedClickable(onLongClick = { onLongClick() }, onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = theme.removeNumberIcon),
            contentDescription = "",
            tint = theme.iconColor
        )
    }
}