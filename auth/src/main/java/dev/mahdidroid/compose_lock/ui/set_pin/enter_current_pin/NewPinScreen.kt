package dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import dev.mahdidroid.compose_lock.auth.R
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButton
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButton
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicator
import dev.mahdidroid.compose_lock.ui.pin.shakeAnimation
import dev.mahdidroid.compose_lock.utils.vibratePhone
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class NewPinConfirm(
    val containerColor: Color, val contentColor: Color, val shape: Shape
)

@Composable
internal fun NewPinScreen(
    modifier: Modifier = Modifier,
    pin: String = "",
    title: String,
    pinNotMatchTitle: String = "",
    vm: NewPinViewModel = koinViewModel(parameters = {
        parametersOf(
            pin, title, pinNotMatchTitle
        )
    }),
    theme: PinEntryData,
    onNavigateToConfirmPin: (String) -> Unit,
    onNavigateToMainScreen: () -> Unit
) {
    val animOffset = remember { Animatable(0f) }
    val animColor = remember { Animatable(theme.pinIndicatorTheme.filledColor) }
    val animBorderColor = remember { Animatable(theme.pinIndicatorTheme.borderColor) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.actions.collect { action ->
            when (action) {
                SetPinAction.ShowErrorPin -> {
                    animColor.animateTo(
                        targetValue = theme.pinIndicatorTheme.errorColor,
                        animationSpec = tween(durationMillis = 100)
                    )

                    animBorderColor.animateTo(
                        targetValue = theme.pinIndicatorTheme.errorBoarderColor,
                        animationSpec = tween(durationMillis = 100)
                    )

                    vibratePhone(context)

                    shakeAnimation(animOffset)

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
                    vm.sendIntent(SetPinIntent.OnResetPin)
                }

                is SetPinAction.NavigateToConfirmPin -> onNavigateToConfirmPin(action.pin)

                SetPinAction.ShakePin -> shakeAnimation(animOffset)
                SetPinAction.NavigateToMainScreen -> onNavigateToMainScreen()
            }
        }
    }

    ConstraintLayout(modifier = modifier.background(theme.backgroundColor)) {
        val (pinRef, pinNumbersRef, titleRef) = createRefs()
        val (oneRef, twoRef, threeRef, fourRef, fiveRef, sixRef, sevenRef, eightRef, nineRef, zeroRef, removeRef, fingerPrintRef, nextActionRef) = createRefs()
        createHorizontalChain(oneRef, twoRef, threeRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(fourRef, fiveRef, sixRef, chainStyle = ChainStyle.Packed)
        createHorizontalChain(sevenRef, eightRef, nineRef, chainStyle = ChainStyle.Packed)

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
            .offset { IntOffset(animOffset.value.dp.roundToPx(), 0) }) {
            repeat(vm.viewState.value.currentPinMaxLength) { index ->
                PinIndicator(
                    theme = theme.pinIndicatorTheme.copy(
                        filledColor = animColor.value, borderColor = animBorderColor.value
                    ), isFilled = index < vm.viewState.value.pin.length
                )
            }
        }
        Text(text = vm.viewState.value.title,
            color = theme.titleColor,
            modifier = Modifier.constrainAs(titleRef) {
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
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("1")) })
        NumberButton(modifier = Modifier.constrainAs(twoRef) {
            top.linkTo(oneRef.top)
            start.linkTo(oneRef.end)
        },
            number = "2",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("2")) })
        NumberButton(modifier = Modifier.constrainAs(threeRef) {
            top.linkTo(oneRef.top)
            start.linkTo(twoRef.end)
        },
            number = "3",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("3")) })
        NumberButton(modifier = Modifier.constrainAs(fourRef) {
            top.linkTo(twoRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        },
            number = "4",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("4")) })
        NumberButton(modifier = Modifier.constrainAs(fiveRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fourRef.end)
        },
            number = "5",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("5")) })
        NumberButton(modifier = Modifier.constrainAs(sixRef) {
            top.linkTo(fourRef.top)
            start.linkTo(fiveRef.end)
        },
            number = "6",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("6")) })
        NumberButton(modifier = Modifier.constrainAs(sevenRef) {
            top.linkTo(fourRef.bottom, 16.dp)
            start.linkTo(oneRef.start)
        },
            number = "7",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("7")) })
        NumberButton(modifier = Modifier.constrainAs(eightRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(sevenRef.end)
        },
            number = "8",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("8")) })
        NumberButton(modifier = Modifier.constrainAs(nineRef) {
            top.linkTo(sevenRef.top)
            start.linkTo(eightRef.end)
        },
            number = "9",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("9")) })

        Spacer(modifier = Modifier
            .constrainAs(fingerPrintRef) {
                top.linkTo(nineRef.bottom, 16.dp)
                start.linkTo(sevenRef.start)
            }
            .size(54.dp))

        NumberButton(modifier = Modifier.constrainAs(zeroRef) {
            top.linkTo(eightRef.bottom, 16.dp)
            start.linkTo(eightRef.start)
        },
            number = "0",
            theme = theme.numberButtonTheme,
            onClick = { vm.sendIntent(SetPinIntent.OnUpdatePin("0")) })

        IconButton(modifier = Modifier.constrainAs(removeRef) {
            top.linkTo(nineRef.bottom, 16.dp)
            start.linkTo(nineRef.start)
        },
            theme = theme.iconButtonTheme,
            icon = theme.iconButtonTheme.removeNumberIcon,
            onClick = { vm.sendIntent(SetPinIntent.OnRemovePin) },
            onLongClick = { vm.sendIntent(SetPinIntent.OnResetPin) })


        if (vm.viewState.value.showNextScreenButton) FloatingActionButton(onClick = {
            vm.sendIntent(
                SetPinIntent.OnNextScreenClick
            )
        },
            modifier = Modifier.constrainAs(nextActionRef) {
                top.linkTo(zeroRef.bottom, 54.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            containerColor = theme.newPinConfirm.containerColor,
            contentColor = theme.newPinConfirm.contentColor,
            shape = theme.newPinConfirm.shape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sharp_arrow_right_alt_24),
                contentDescription = ""
            )
        }
    }

}