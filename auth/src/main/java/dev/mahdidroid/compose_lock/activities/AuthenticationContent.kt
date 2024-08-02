package dev.mahdidroid.compose_lock.activities

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.ui.pin.PinEntryScreen
import dev.mahdidroid.compose_lock.utils.AuthState

@Composable
internal fun AuthenticationContent(
    modifier: Modifier = Modifier,
    state: AuthState,
    theme: LockTheme,
    pinTitleMessage: String,
    onBiometricPrompt: () -> Unit,
    onFinishActivity: () -> Unit
) {
    when (state) {
        AuthState.Pin -> PinEntryScreen(modifier = modifier,
            title = pinTitleMessage,
            theme = theme.pinTheme,
            onFingerPrintClick = {
                onBiometricPrompt()
            })

        AuthState.ChangePin -> {
            //   SetPinScreen(modifier = Modifier.background(MaterialTheme.colorScheme.primary))
        }

        AuthState.Password -> {}
        AuthState.ChangePassword -> {}
        AuthState.Main -> {
            onFinishActivity()
        }
    }
}