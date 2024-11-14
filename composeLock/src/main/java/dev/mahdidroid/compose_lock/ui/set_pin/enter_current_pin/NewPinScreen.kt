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
import dev.mahdidroid.compose_lock.R
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.composable.IconButton
import dev.mahdidroid.compose_lock.ui.composable.NumberButton
import dev.mahdidroid.compose_lock.ui.composable.PinComposable
import dev.mahdidroid.compose_lock.ui.composable.PinIndicator
import dev.mahdidroid.compose_lock.ui.pin.PinIntent
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

    PinComposable(modifier = modifier,
        pinIndicatorModifier = Modifier.offset { IntOffset(animOffset.value.dp.roundToPx(), 0) },
        theme = theme,
        isFingerPrintAvailable = false,
        title = title,
        pin = vm.viewState.value.pin,
        timer = "",
        maxLength = vm.viewState.value.maxLength,
        animColor = animColor.value,
        animBorderColor = animBorderColor.value,
        onNumberClick = { number ->
            vm.sendIntent(SetPinIntent.OnUpdatePin(number))
        },
        onFingerprintClick = {},
        onRemoveClick = { vm.sendIntent(SetPinIntent.OnRemovePin) },
        onRemoveAllClick = { vm.sendIntent(SetPinIntent.OnResetPin) })
}