package dev.mahdidroid.compose_lock.activities

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.ui.pin.PinEntryScreen
import dev.mahdidroid.compose_lock.ui.set_pin.SetPinNavigation
import dev.mahdidroid.compose_lock.utils.AuthResult
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.ChangePinTitleMessages

@Composable
internal fun AuthenticationContent(
    modifier: Modifier = Modifier,
    state: AuthState,
    theme: LockTheme,
    pinTitleMessage: String,
    changePinTitleMessages: ChangePinTitleMessages,
    onBiometricPrompt: () -> Unit,
    onSendResult: (AuthResult) -> Unit,
    onFinishActivity: () -> Unit
) {
    val navController = rememberNavController()
    when (state) {
        AuthState.Pin -> PinEntryScreen(
            modifier = modifier,
            title = pinTitleMessage,
            theme = theme.pinTheme,
            onFingerPrintClick = {
                onBiometricPrompt()
            },
            onNavigateToChangePassword = {

            },
            onSendResult = onSendResult
        )

        AuthState.ChangePin -> {
            SetPinNavigation(
                modifier = modifier,
                navController = navController,
                changePinTitleMessages = changePinTitleMessages,
                theme = theme.pinTheme,
                onFinishActivity = { onSendResult(AuthResult.PIN_SUCCESS) }
            )
        }

        AuthState.Password -> {}
        AuthState.ChangePassword -> {}
        AuthState.NoAuth -> onFinishActivity()
    }
}