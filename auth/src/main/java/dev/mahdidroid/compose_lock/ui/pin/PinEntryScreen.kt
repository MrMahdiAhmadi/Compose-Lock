package dev.mahdidroid.compose_lock.ui.pin

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButton
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButton
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicator
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme
import dev.mahdidroid.compose_lock.utils.vibratePhone
import kotlinx.coroutines.launch
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
    title: String,
    theme: PinEntryData,
    onFingerPrintClick: () -> Unit,
) {
    val animOffset = remember { Animatable(0f) }
    val animColor = remember { Animatable(theme.pinIndicatorTheme.filledColor) }
    val animBorderColor = remember { Animatable(theme.pinIndicatorTheme.borderColor) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.actions.collect { action ->
            when (action) {
                PinAction.ShowErrorPin -> {
                    animColor.animateTo(
                        targetValue = theme.pinIndicatorTheme.errorColor,
                        animationSpec = tween(durationMillis = 100)
                    )

                    animBorderColor.animateTo(
                        targetValue = theme.pinIndicatorTheme.errorBoarderColor,
                        animationSpec = tween(durationMillis = 100)
                    )

                    vibratePhone(context)

                    animOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = repeatable(iterations = 4, animation = keyframes {
                            durationMillis = 100
                            10f at 0
                            -10f at 50
                            10f at 100
                            -10f at 150
                            10f at 200
                            -10f at 250
                            10f at 300
                            -10f at 350
                            10f at 400
                        })
                    )

                    launch {
                        animColor.animateTo(
                            targetValue = theme.pinIndicatorTheme.filledColor,
                            animationSpec = tween(durationMillis = 100)
                        )
                    }
                    launch {
                        animBorderColor.animateTo(
                            targetValue = theme.pinIndicatorTheme.borderColor,
                            animationSpec = tween(durationMillis = 100)
                        )
                    }
                    vm.sendIntent(PinIntent.OnResetPin)
                }
            }
        }
    }

    Log.i("TAG", "PinEntryScreen: ${animOffset.value}")

    ConstraintLayout(modifier = modifier.background(theme.backgroundColor)) {
        val (pinRef, pinNumbersRef, titleRef) = createRefs()
        val (oneRef, twoRef, threeRef, fourRef, fiveRef, sixRef, sevenRef, eightRef, nineRef, zeroRef, removeRef, fingerPrintRef) = createRefs()
        createHorizontalChain(oneRef, twoRef, threeRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fourRef, fiveRef, sixRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(sevenRef, eightRef, nineRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fingerPrintRef, zeroRef, removeRef, chainStyle = ChainStyle.Packed)

        Text(text = vm.viewState.value.pin,
            color = theme.pinIndicatorTheme.filledColor,
            modifier = Modifier.constrainAs(pinRef) {
                top.linkTo(parent.top, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        Row(modifier = Modifier
            .constrainAs(pinNumbersRef) {
                top.linkTo(parent.top, 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .offset(x = animOffset.value.dp)) {
            repeat(vm.viewState.value.maxLength) { index ->
                PinIndicator(
                    theme = theme.pinIndicatorTheme.copy(
                        filledColor = animColor.value, borderColor = animBorderColor.value
                    ), isFilled = index < vm.viewState.value.pin.length
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
        },
            number = "1",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("1")) })
        NumberButton(modifier = Modifier.constrainAs(twoRef) {
            top.linkTo(oneRef.top)
            start.linkTo(oneRef.end)
        },
            number = "2",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("2")) })
        NumberButton(modifier = Modifier.constrainAs(threeRef) {
            top.linkTo(oneRef.top)
            start.linkTo(twoRef.end)
        },
            number = "3",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("3")) })
        NumberButton(modifier = Modifier.constrainAs(fourRef) {
            top.linkTo(twoRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        },
            number = "4",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("4")) })
        NumberButton(modifier = Modifier.constrainAs(fiveRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fourRef.end)
        },
            number = "5",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("5")) })
        NumberButton(modifier = Modifier.constrainAs(sixRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fiveRef.end)
        },
            number = "6",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("6")) })
        NumberButton(modifier = Modifier.constrainAs(sevenRef) {
            top.linkTo(fourRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        },
            number = "7",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("7")) })
        NumberButton(modifier = Modifier.constrainAs(eightRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(sevenRef.end)
        },
            number = "8",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("8")) })
        NumberButton(modifier = Modifier.constrainAs(nineRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(eightRef.end)
        },
            number = "9",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("9")) })
        IconButton(modifier = Modifier.constrainAs(fingerPrintRef) {
            top.linkTo(sevenRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        },
            theme = theme.iconButtonTheme,
            icon = theme.iconButtonTheme.displayFingerprintIcon,
            onClick = { onFingerPrintClick() },
            onLongClick = {})
        NumberButton(modifier = Modifier.constrainAs(zeroRef) {
            top.linkTo(fingerPrintRef.top)
            start.linkTo(fingerPrintRef.end)
        },
            number = "0",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(PinIntent.OnUpdatePin("0")) })
        IconButton(modifier = Modifier.constrainAs(removeRef) {
            top.linkTo(fingerPrintRef.top)
            start.linkTo(zeroRef.end)
        },
            theme = theme.iconButtonTheme,
            icon = theme.iconButtonTheme.removeNumberIcon,
            onClick = { vm.sendIntent(PinIntent.OnRemovePin) },
            onLongClick = { vm.sendIntent(PinIntent.OnResetPin) })
    }
}