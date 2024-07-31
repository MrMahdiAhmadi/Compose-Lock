package dev.mahdidroid.compose_lock.ui.pin.composable

import androidx.compose.foundation.ExperimentalFoundationApi
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

@Composable
internal fun NumberButton(
    modifier: Modifier = Modifier, number: String, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .size(54.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        )
    ) {
        Text(text = number, fontSize = 24.sp, color = Color.Black)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun IconButton(
    modifier: Modifier = Modifier, icon: Int, onClick: () -> Unit, onLongClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(CircleShape)
            .size(54.dp)
            //.background(Color.White.copy(alpha = 0.3f))
            .combinedClickable(onLongClick = { onLongClick() }, onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = "", tint = Color.Black)
    }
}