package dev.mahdidroid.compose_lock.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData

@Composable
fun PinComposable(
    modifier: Modifier = Modifier,
    pinIndicatorModifier:Modifier,
    theme: PinEntryData,
    isFingerPrintAvailable: Boolean,
    title: String,
    pin: String,
    timer: String,
    maxLength: Int,
    animColor: Color,
    animBorderColor: Color,
    onNumberClick: (number: String) -> Unit,
    onFingerprintClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onRemoveAllClick: () -> Unit
) {

    ConstraintLayout(modifier = modifier.background(theme.backgroundColor)) {
        val (pinRef, timerRef, pinNumbersRef, titleRef) = createRefs()
        val (oneRef, twoRef, threeRef, fourRef, fiveRef, sixRef, sevenRef, eightRef, nineRef, zeroRef, removeRef, fingerPrintRef) = createRefs()
        createHorizontalChain(oneRef, twoRef, threeRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fourRef, fiveRef, sixRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(sevenRef, eightRef, nineRef, chainStyle = ChainStyle.Packed)
        if (isFingerPrintAvailable) createHorizontalChain(
            fingerPrintRef, zeroRef, removeRef, chainStyle = ChainStyle.Packed
        )

        Text(text = pin,
            color = theme.pinIndicatorTheme.filledColor,
            modifier = Modifier.constrainAs(pinRef) {
                top.linkTo(parent.top, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        if (timer.isNotEmpty()) {
            Text(text = timer,
                color = theme.pinIndicatorTheme.filledColor,
                fontSize = 24.sp,
                modifier = Modifier.constrainAs(timerRef) {
                    top.linkTo(pinRef.bottom, 8.dp)
                    start.linkTo(parent.start)
                })
        }

        Row(modifier = pinIndicatorModifier
            .constrainAs(pinNumbersRef) {
                top.linkTo(parent.top, 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            repeat(maxLength) { index ->
                PinIndicator(
                    theme = theme.pinIndicatorTheme.copy(
                        filledColor = animColor, borderColor = animBorderColor
                    ), isFilled = index < pin.length
                )
            }
        }
        Text(text = title, color = theme.titleColor, modifier = Modifier.constrainAs(titleRef) {
            top.linkTo(pinNumbersRef.bottom, 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        NumberButton(modifier = Modifier.constrainAs(oneRef) {
            top.linkTo(titleRef.bottom, 16.dp)
            start.linkTo(parent.start)
        }, number = "1", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(twoRef) {
            top.linkTo(oneRef.top)
            start.linkTo(oneRef.end)
        }, number = "2", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(threeRef) {
            top.linkTo(oneRef.top)
            start.linkTo(twoRef.end)
        }, number = "3", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(fourRef) {
            top.linkTo(twoRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        }, number = "4", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(fiveRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fourRef.end)
        }, number = "5", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(sixRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fiveRef.end)
        }, number = "6", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(sevenRef) {
            top.linkTo(fourRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        }, number = "7", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(eightRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(sevenRef.end)
        }, number = "8", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })
        NumberButton(modifier = Modifier.constrainAs(nineRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(eightRef.end)
        }, number = "9", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })

        if (isFingerPrintAvailable) {
            IconButton(modifier = Modifier.constrainAs(fingerPrintRef) {
                top.linkTo(nineRef.bottom, 16.dp)
                start.linkTo(sevenRef.start)
            },
                theme = theme.iconButtonTheme,
                icon = theme.iconButtonTheme.displayFingerprintIcon,
                onClick = { onFingerprintClick() },
                onLongClick = {})
        } else {
            Spacer(modifier = Modifier
                .constrainAs(fingerPrintRef) {
                    top.linkTo(nineRef.bottom, 16.dp)
                    start.linkTo(sevenRef.start)
                }
                .size(54.dp))
        }

        NumberButton(modifier = Modifier.constrainAs(zeroRef) {
            top.linkTo(eightRef.bottom, 16.dp)
            start.linkTo(eightRef.start)
        }, number = "0", theme = theme.numberButtonTheme, onClick = { onNumberClick(it) })

        IconButton(modifier = Modifier.constrainAs(removeRef) {
            top.linkTo(nineRef.bottom, 16.dp)
            start.linkTo(nineRef.start)
        },
            theme = theme.iconButtonTheme,
            icon = theme.iconButtonTheme.removeNumberIcon,
            onClick = { onRemoveClick() },
            onLongClick = { onRemoveAllClick() })
    }
}