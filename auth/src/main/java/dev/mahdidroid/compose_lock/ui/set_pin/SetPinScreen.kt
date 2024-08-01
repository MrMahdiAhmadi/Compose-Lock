package dev.mahdidroid.compose_lock.ui.set_pin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import dev.mahdidroid.compose_lock.auth.R
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButton
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButton

@Composable
internal fun SetPinScreen(
    modifier: Modifier = Modifier
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

        /* IconButton(icon = R.drawable.baseline_fingerprint_24,
             onClick = { onFingerPrintClick() },
             onLongClick = {},
             modifier = Modifier.constrainAs(fingerPrintRef) {
                 top.linkTo(sevenRef.bottom, 16.dp)
                 start.linkTo(oneRef.start)
             })*/

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