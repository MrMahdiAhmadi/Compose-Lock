package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.mahdidroid.compose_lock.activities.vm.LockIntent
import dev.mahdidroid.compose_lock.activities.vm.LockViewModel
import dev.mahdidroid.compose_lock.auth.ComposeLockRetryPolicy
import dev.mahdidroid.compose_lock.theme.ComposeLockTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockMessages
import org.koin.android.ext.android.inject

abstract class BaseSplashActivity : ComponentActivity() {
    private val vm: LockViewModel by inject()
    private lateinit var authActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setLockTheme(getComposeLockTheme())
        setLockMessages(getLockMessages())

        authActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            startActivity(Intent(this, getContentActivityClass()))
            finish()
        }

        splashScreen.setKeepOnScreenCondition { true }

        vm.loadAuthState {
            if (vm.viewState.value.currentAuthState == AuthState.NoAuth) {
                startActivity(Intent(this@BaseSplashActivity, getContentActivityClass()))
                finish()
            } else {
                authActivityResultLauncher.launch(
                    Intent(
                        this@BaseSplashActivity, AuthenticationActivity::class.java
                    )
                )
            }
        }
    }

    private fun setLockTheme(
        theme: ComposeLockTheme
    ) {
        when (theme) {
            is ComposeLockTheme.SingleTheme -> vm.sendIntent(LockIntent.OnSingleTheme(theme = theme.theme))

            is ComposeLockTheme.DayNightTheme -> vm.sendIntent(
                LockIntent.OnDayNightTheme(
                    lightTheme = theme.lightTheme, darkTheme = theme.darkTheme
                )
            )
        }
    }

    private fun setLockMessages(lockMessages: LockMessages) {
        vm.sendIntent(LockIntent.OnLockMessagesChange(lockMessages))
    }

    private fun setRetryPolicy(retryPolicy: ComposeLockRetryPolicy) {
        vm.sendIntent(LockIntent.OnRetryPolicyChange(retryPolicy))
    }

    abstract fun getComposeLockTheme(): ComposeLockTheme
    abstract fun getLockMessages(): LockMessages
    abstract fun getRetryPolicy(): ComposeLockRetryPolicy
    /**
     * Must be implemented by the child class to specify which Activity should be launched after
     * successful authentication or when authentication is not required.
     * @return The `Class` object of the Activity to be started.
     */
    abstract fun getContentActivityClass(): Class<*>
}