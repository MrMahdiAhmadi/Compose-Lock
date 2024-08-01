package dev.mahdidroid.compose_lock.ui.pin

import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButton
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButton
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme
import org.koin.androidx.compose.koinViewModel

data class PinEntryData(
    val backgroundColor: Color,
    val titleColor: Color,
    val pinIndicatorTheme: PinIndicatorTheme,
    val numberButtonTheme: NumberButtonTheme,
    val iconButtonTheme: IconButtonTheme,
)

@Composable
internal fun PinEntryScreen(
    modifier: Modifier = Modifier,
    vm: PinViewModel = koinViewModel(),
    theme: PinEntryData,
    onFingerPrintClick: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }

    ConstraintLayout(modifier = modifier.background(theme.backgroundColor)) {
        val (pinNumbersRef, titleRef) = createRefs()
        val (oneRef, twoRef, threeRef, fourRef, fiveRef, sixRef, sevenRef, eightRef, nineRef, zeroRef, removeRef, fingerPrintRef) = createRefs()
        createHorizontalChain(oneRef, twoRef, threeRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fourRef, fiveRef, sixRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(sevenRef, eightRef, nineRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fingerPrintRef, zeroRef, removeRef, chainStyle = ChainStyle.Packed)

        Text(text = pin,
            color = theme.pinIndicatorTheme.filledColor,
            modifier = Modifier.constrainAs(pinNumbersRef) {
                top.linkTo(parent.top, 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        Text(text = "title", color = theme.titleColor, modifier = Modifier.constrainAs(titleRef) {
            top.linkTo(pinNumbersRef.bottom, 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        NumberButton(modifier = Modifier.constrainAs(oneRef) {
            top.linkTo(titleRef.bottom, 16.dp)
            start.linkTo(parent.start)
        }, number = "1", theme = theme.numberButtonTheme, onClick = { pin += "1" })
        NumberButton(modifier = Modifier.constrainAs(twoRef) {
            top.linkTo(oneRef.top)
            start.linkTo(oneRef.end)
        }, number = "2", theme = theme.numberButtonTheme, onClick = { pin += "2" })
        NumberButton(modifier = Modifier.constrainAs(threeRef) {
            top.linkTo(oneRef.top)
            start.linkTo(twoRef.end)
        }, number = "3", theme = theme.numberButtonTheme, onClick = { pin += "3" })
        NumberButton(modifier = Modifier.constrainAs(fourRef) {
            top.linkTo(twoRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        }, number = "4", theme = theme.numberButtonTheme, onClick = { pin += "4" })
        NumberButton(modifier = Modifier.constrainAs(fiveRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fourRef.end)
        }, number = "5", theme = theme.numberButtonTheme, onClick = { pin += "5" })
        NumberButton(modifier = Modifier.constrainAs(sixRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fiveRef.end)
        }, number = "6", theme = theme.numberButtonTheme, onClick = { pin += "6" })
        NumberButton(modifier = Modifier.constrainAs(sevenRef) {
            top.linkTo(fourRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        }, number = "7", theme = theme.numberButtonTheme, onClick = { pin += "7" })
        NumberButton(modifier = Modifier.constrainAs(eightRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(sevenRef.end)
        }, number = "8", theme = theme.numberButtonTheme, onClick = { pin += "8" })
        NumberButton(modifier = Modifier.constrainAs(nineRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(eightRef.end)
        }, number = "9", theme = theme.numberButtonTheme, onClick = { pin += "9" })
        IconButton(modifier = Modifier.constrainAs(fingerPrintRef) {
            top.linkTo(sevenRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        }, theme = theme.iconButtonTheme, onClick = { onFingerPrintClick() }, onLongClick = {})
        NumberButton(modifier = Modifier.constrainAs(zeroRef) {
            top.linkTo(fingerPrintRef.top)
            start.linkTo(fingerPrintRef.end)
        }, number = "0", theme = theme.numberButtonTheme, onClick = { pin += "0" })
        IconButton(modifier = Modifier.constrainAs(removeRef) {
            top.linkTo(fingerPrintRef.top)
            start.linkTo(zeroRef.end)
        },
            theme = theme.iconButtonTheme,
            onClick = { pin = pin.dropLast(1) },
            onLongClick = { pin = "" })
    }
}