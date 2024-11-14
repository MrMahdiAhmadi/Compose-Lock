package dev.mahdidroid.compose_lock.ui.pin

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.mahdidroid.compose_lock.ui.composable.PinComposable
import dev.mahdidroid.compose_lock.ui.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.composable.PinIndicatorTheme
import dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin.NewPinConfirm
import dev.mahdidroid.compose_lock.utils.AuthResult
import dev.mahdidroid.compose_lock.utils.vibratePhone
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class PinEntryData(
    val backgroundColor: Color,
    val titleColor: Color,
    val pinIndicatorTheme: PinIndicatorTheme,
    val numberButtonTheme: NumberButtonTheme,
    val iconButtonTheme: IconButtonTheme,
    val newPinConfirm: NewPinConfirm
)

@Composable
internal fun PinEntryScreen(
    modifier: Modifier = Modifier,
    isChangePassword: Boolean = false,
    isFingerPrintAvailable: Boolean = true,
    title: String,
    theme: PinEntryData,
    vm: PinViewModel = koinViewModel(parameters = { parametersOf(isChangePassword) }),
    onFingerPrintClick: () -> Unit,
    onNavigateToChangePin: () -> Unit,
    onSendResult: (AuthResult) -> Unit
) {
    val animOffset = remember { Animatable(0f) }
    val animColor = remember { Animatable(theme.pinIndicatorTheme.filledColor) }
    val animBorderColor = remember { Animatable(theme.pinIndicatorTheme.borderColor) }
    val context = LocalContext.current
    val showFingerPrint = remember {
        mutableStateOf(
            if (!isFingerPrintAvailable) false else {
                !isChangePassword
            }
        )
    }

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

                    shakeAnimation(animOffset)
                    vm.sendIntent(PinIntent.OnResetPin)

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
                    onSendResult(AuthResult.PIN_FAILED)
                }

                PinAction.NavigateToChangePin -> onNavigateToChangePin()

                PinAction.NavigateToMainScreen -> onSendResult(AuthResult.PIN_SUCCESS)
            }
        }
    }

    PinComposable(modifier = modifier,
        pinIndicatorModifier = Modifier.offset { IntOffset(animOffset.value.dp.roundToPx(), 0) },
        theme = theme,
        isFingerPrintAvailable = isFingerPrintAvailable,
        title = title,
        pin = vm.viewState.value.pin,
        timer = vm.viewState.value.timer,
        maxLength = vm.viewState.value.maxLength,
        animColor = animColor.value,
        animBorderColor = animBorderColor.value,
        onNumberClick = { number ->
            vm.sendIntent(PinIntent.OnUpdatePin(number))
        },
        onFingerprintClick = { onFingerPrintClick() },
        onRemoveClick = { vm.sendIntent(PinIntent.OnRemovePin) },
        onRemoveAllClick = { vm.sendIntent(PinIntent.OnResetPin) })
}


suspend fun shakeAnimation(animOffset: Animatable<Float, AnimationVector1D>) {
    animOffset.animateTo(
        targetValue = 0f, animationSpec = repeatable(iterations = 2, animation = keyframes {
            durationMillis = 200 // Total duration of the shake animation
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
}