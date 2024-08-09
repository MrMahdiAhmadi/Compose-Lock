package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LocalAuthAction
import dev.mahdidroid.compose_lock.utils.LockActions
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject

abstract class BaseLockFragmentActivity : FragmentActivity() {
    private val vm: LockViewModel by inject()

    override fun onResume() {
        super.onResume()
        if (vm.viewState.value.currentAuthState != AuthState.NoAuth && vm.viewState.value.currentAuthState != AuthState.Loading) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
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