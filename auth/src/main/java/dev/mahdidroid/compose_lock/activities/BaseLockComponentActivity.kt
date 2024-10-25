package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.mahdidroid.compose_lock.activities.vm.LockIntent
import dev.mahdidroid.compose_lock.activities.vm.LockViewModel
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LocalAuthAction
import dev.mahdidroid.compose_lock.utils.LockActions
import org.koin.android.ext.android.inject


abstract class BaseLockComponentActivity : ComponentActivity() {
    private val vm: LockViewModel by inject()

    override fun onResume() {
        super.onResume()
        if (vm.viewState.value.currentAuthState != AuthState.NoAuth) startActivity(
            Intent(
                this, AuthenticationActivity::class.java
            )
        )
    }

    override fun onStop() {
        super.onStop()
        vm.sendIntent(LockIntent.OnStop)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun handleAuthAction(action: LockActions) {
        when (action) {
            is LockActions.OnOpenScreenNow -> {
                vm.sendIntent(LockIntent.OnUpdateScreenState(action.value))
                startActivity(Intent(this, AuthenticationActivity::class.java))
            }

            is LockActions.OnSetDefaultValue -> {
                vm.sendIntent(LockIntent.OnSetDefaultAuth(action.value))
            }
        }
    }

    @Composable
    fun ProvideAuthAction(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalAuthAction provides this::handleAuthAction) {
            content()
        }
    }
}