package dev.mahdidroid.compose_lock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import dev.mahdidroid.compose_lock.auth.R


@Composable
fun PinEntryScreen(
    modifier: Modifier = Modifier, maxPinLength: Int, onFingerPrintClick: () -> Unit
) {
    var pin by remember { mutableStateOf("") }

    ConstraintLayout(modifier = modifier) {
        val (pinNumbersRef, titleRef) = createRefs()
        val (oneRef, twoRef, threeRef, fourRef, fiveRef, sixRef, sevenRef, eightRef, nineRef, zeroRef, removeRef, fingerPrintRef) = createRefs()
        createHorizontalChain(oneRef, twoRef, threeRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fourRef, fiveRef, sixRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(sevenRef, eightRef, nineRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fingerPrintRef, zeroRef, removeRef, chainStyle = ChainStyle.Packed)

        Text(text = pin, modifier = Modifier.constrainAs(pinNumbersRef) {
            top.linkTo(parent.top, 32.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Text(text = "title", modifier = Modifier.constrainAs(titleRef) {
            top.linkTo(pinNumbersRef.bottom, 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        NumberButton(number = "1",
            onClick = { pin += "1" },
            modifier = Modifier.constrainAs(oneRef) {
                top.linkTo(titleRef.bottom, 16.dp)
                start.linkTo(parent.start)
            })
        NumberButton(number = "2",
            onClick = { pin += "2" },
            modifier = Modifier.constrainAs(twoRef) {
                top.linkTo(oneRef.top)
                start.linkTo(oneRef.end)
            })

        NumberButton(number = "3",
            onClick = { pin += "3" },
            modifier = Modifier.constrainAs(threeRef) {
                top.linkTo(oneRef.top)
                start.linkTo(twoRef.end)
            })

        NumberButton(number = "4",
            onClick = { pin += "4" },
            modifier = Modifier.constrainAs(fourRef) {
                top.linkTo(twoRef.bottom, 16.dp)
                start.linkTo(oneRef.start)
            })

        NumberButton(number = "5",
            onClick = { pin += "5" },
            modifier = Modifier.constrainAs(fiveRef) {
                top.linkTo(fourRef.top)
                start.linkTo(fourRef.end)
            })

        NumberButton(number = "6",
            onClick = { pin += "6" },
            modifier = Modifier.constrainAs(sixRef) {
                top.linkTo(fourRef.top)
                start.linkTo(fiveRef.end)
            })

        NumberButton(number = "7",
            onClick = { pin += "7" },
            modifier = Modifier.constrainAs(sevenRef) {
                top.linkTo(fourRef.bottom, 16.dp)
                start.linkTo(oneRef.start)
            })

        NumberButton(number = "8",
            onClick = { pin += "8" },
            modifier = Modifier.constrainAs(eightRef) {
                top.linkTo(sevenRef.top)
                start.linkTo(sevenRef.end)
            })

        NumberButton(number = "9",
            onClick = { pin += "9" },
            modifier = Modifier.constrainAs(nineRef) {
                top.linkTo(sevenRef.top)
                start.linkTo(eightRef.end)
            })

        IconButton(icon = R.drawable.baseline_fingerprint_24,
            onClick = { onFingerPrintClick() },
            onLongClick = {},
            modifier = Modifier.constrainAs(fingerPrintRef) {
                top.linkTo(sevenRef.bottom, 16.dp)
                start.linkTo(oneRef.start)
            })

        NumberButton(number = "0",
            onClick = { pin += "0" },
            modifier = Modifier.constrainAs(zeroRef) {
                top.linkTo(fingerPrintRef.top)
                start.linkTo(fingerPrintRef.end)
            })

        IconButton(icon = R.drawable.baseline_backspace_24,
            onClick = { pin = pin.dropLast(1) },
            onLongClick = { pin = "" },
            modifier = Modifier.constrainAs(removeRef) {
                top.linkTo(fingerPrintRef.top)
                start.linkTo(zeroRef.end)
            })
    }
}

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


@Composable
fun NumberButton(
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
fun IconButton(
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