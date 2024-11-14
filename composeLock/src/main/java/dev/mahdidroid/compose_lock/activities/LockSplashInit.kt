package dev.mahdidroid.compose_lock.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import dev.mahdidroid.compose_lock.activities.vm.LockIntent
import dev.mahdidroid.compose_lock.activities.vm.LockViewModel
import dev.mahdidroid.compose_lock.auth.ComposeLockRetryPolicy
import dev.mahdidroid.compose_lock.theme.ComposeLockTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockMessages
import org.koin.java.KoinJavaComponent.getKoin

class LockSplashInit(
    private val context: Context,
    private val activityResultRegistry: ActivityResultRegistry,
    private val getContentActivityClass: () -> Class<*>,
    private val getComposeLockTheme: () -> ComposeLockTheme,
    private val getLockMessages: () -> LockMessages,
    private val getRetryPolicy: () -> ComposeLockRetryPolicy
) {
    private val vm: LockViewModel by getKoin().inject()
    private lateinit var authActivityResultLauncher: ActivityResultLauncher<Intent>

    fun initialize() {
        vm.loadAuthState {
            if (vm.viewState.value.currentAuthState == AuthState.NoAuth) {
                val intent = Intent(context, getContentActivityClass())
                context.startActivity(intent)
                if (context is Activity) context.finish()
            } else {
                authActivityResultLauncher.launch(
                    Intent(context, AuthenticationActivity::class.java)
                )
            }
        }

        setLockTheme(getComposeLockTheme())
        setLockMessages(getLockMessages())
        setRetryPolicy(getRetryPolicy())

        authActivityResultLauncher = activityResultRegistry.register(
            "authResult", ActivityResultContracts.StartActivityForResult()
        ) {
            val intent = Intent(context, getContentActivityClass())
            context.startActivity(intent)
            if (context is Activity) context.finish()
        }
    }

    private fun setLockTheme(theme: ComposeLockTheme) {
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
}