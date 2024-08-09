package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LocalAuthAction
import dev.mahdidroid.compose_lock.utils.LockActions
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockMessages
import dev.mahdidroid.compose_lock.utils.LockViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


abstract class BaseLockComponentActivity : ComponentActivity() {
    private val vm: LockViewModel by inject()

    override fun onResume() {
        super.onResume()
        if (vm.viewState.value.currentAuthState != AuthState.NoAuth && vm.viewState.value.currentAuthState != AuthState.Loading) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
        CoroutineScope(Dispatchers.Main).launch {
            val a = async { vm.loadAuthState() }
            val f = a.await()

            if (f) startActivity(
                Intent(
                    this@BaseLockComponentActivity, AuthenticationActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStop() {
        super.onStop()
        vm.sendIntent(LockIntent.OnStop)/*setContent {
            LaunchedEffect(Unit) {
                vm.actions.collect {
                    when (it) {
                        LockAction.StartAuthenticationActivity -> startActivity(
                            Intent(
                                this@BaseLockComponentActivity, AuthenticationActivity::class.java
                            )
                        )
                    }
                }
            }
        }*/
    }

    fun setLockTheme(singleTheme: LockTheme) =
        vm.sendIntent(LockIntent.OnSingleTheme(theme = singleTheme))

    fun setLockTheme(lightTheme: LockTheme, darkTheme: LockTheme) =
        vm.sendIntent(LockIntent.OnTwoTheme(lightTheme = lightTheme, darkTheme = darkTheme))

    fun setLockMessages(lockMessages: LockMessages) {
        vm.sendIntent(LockIntent.OnLockMessagesChange(lockMessages))
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