package dev.mahdidroid.compose_lock.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mahdidroid.compose_lock.ui.pin.PinEntryScreen
import dev.mahdidroid.compose_lock.ui.set_pin.SetPinScreen
import dev.mahdidroid.compose_lock.utils.AuthState

@Composable
internal fun AuthenticationContent(
    modifier: Modifier = Modifier,
    state: AuthState,
    onBiometricPrompt: () -> Unit,
    onFinishActivity: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier.padding(innerPadding)
        ) {
            when (state) {
                AuthState.Pin -> PinEntryScreen(onFingerPrintClick = {
                    onBiometricPrompt()
                })

                AuthState.ChangePin -> {
                    SetPinScreen(modifier = Modifier.background(MaterialTheme.colorScheme.primary))
                }

                AuthState.Password -> {}
                AuthState.ChangePassword -> {}
                AuthState.Main -> {
                    onFinishActivity()
                }
            }
        }
    }
}