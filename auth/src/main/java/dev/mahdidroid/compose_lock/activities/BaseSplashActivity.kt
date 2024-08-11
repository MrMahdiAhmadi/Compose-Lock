package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.mahdidroid.compose_lock.theme.ComposeLockTheme
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockMessages
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject

abstract class BaseSplashActivity : ComponentActivity() {
    private val vm: LockViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setLockTheme(getComposeLockTheme())
        setLockMessages(getLockMessages())

        splashScreen.setKeepOnScreenCondition { true }

        vm.loadAuthState {
            val destinationClass = getContentActivityClass()
            val intent = Intent(this@BaseSplashActivity, destinationClass)
            startActivity(intent)
            finish()
        }
    }

    private fun setLockTheme(
        theme: ComposeLockTheme
    ) {
        when (theme) {
            is ComposeLockTheme.SingleTheme -> vm.sendIntent(LockIntent.OnSingleTheme(theme = theme.theme))

            is ComposeLockTheme.DayNightTheme -> vm.sendIntent(
                LockIntent.OnTwoTheme(
                    lightTheme = theme.lightTheme, darkTheme = theme.darkTheme
                )
            )
        }
    }

    private fun setLockMessages(lockMessages: LockMessages) {
        vm.sendIntent(LockIntent.OnLockMessagesChange(lockMessages))
    }

    abstract fun getComposeLockTheme(): ComposeLockTheme
    abstract fun getLockMessages(): LockMessages
    abstract fun getContentActivityClass(): Class<*>
}