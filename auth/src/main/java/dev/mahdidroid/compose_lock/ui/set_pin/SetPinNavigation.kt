package dev.mahdidroid.compose_lock.ui.set_pin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.PinEntryScreen
import dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin.NewPinScreen
import dev.mahdidroid.compose_lock.utils.ChangePinTitleMessages

@Composable
fun SetPinNavigation(
    modifier: Modifier = Modifier,
    theme: PinEntryData,
    changePinTitleMessages: ChangePinTitleMessages,
    navController: NavHostController,
    onFinishActivity: () -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SetPinScreens.EnterCurrentPin
    ) {
        composable<SetPinScreens.EnterCurrentPin>(enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            PinEntryScreen(
                modifier = modifier,
                theme = theme,
                title = changePinTitleMessages.enterCurrentPinTitle,
                isChangePassword = true,
                onFingerPrintClick = {},
                onNavigateToChangePassword = { navController.navigate(SetPinScreens.NewPin) },
                onNavigateToMainScreen = onFinishActivity
            )
        }
        composable<SetPinScreens.NewPin>(enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            NewPinScreen(
                theme = theme,
                title = changePinTitleMessages.enterNewPinTitle,
                onNavigateToConfirmPin = { newPin ->
                    navController.navigate(SetPinScreens.ConfirmNewPin(newPin))
                },
                onNavigateToMainScreen = onFinishActivity
            )
        }
        composable<SetPinScreens.ConfirmNewPin>(enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            NewPinScreen(
                theme = theme,
                title = changePinTitleMessages.confirmNewPinTitle,
                pin = it.toRoute<SetPinScreens.ConfirmNewPin>().newPin,
                pinNotMatchTitle = changePinTitleMessages.pinNotMatchTitle,
                onNavigateToConfirmPin = {},
                onNavigateToMainScreen = onFinishActivity
            )
        }
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            300, easing = LinearEasing
        )
    ) + slideIntoContainer(
        animationSpec = tween(300, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            300, easing = LinearEasing
        )
    ) + slideOutOfContainer(
        animationSpec = tween(300, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.End
    )
}