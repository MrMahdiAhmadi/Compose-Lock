package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockMessages
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject

abstract class BaseSplashActivity : ComponentActivity() {
    private val vm: LockViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setLockTheme(getSingleTheme(), getLightTheme(), getDarkTheme())
        setLockMessages(getLockMessages())

        splashScreen.setKeepOnScreenCondition { true }

        vm.loadAuthState {
            val destinationClass = getDestinationClass()
            val intent = Intent(this@BaseSplashActivity, destinationClass)
            startActivity(intent)
            finish()
        }
    }

    private fun setLockTheme(
        singleTheme: LockTheme?, lightTheme: LockTheme?, darkTheme: LockTheme?
    ) {
        singleTheme?.let {
            vm.sendIntent(LockIntent.OnSingleTheme(theme = it))
        }
        if (lightTheme != null && darkTheme != null) {
            vm.sendIntent(LockIntent.OnTwoTheme(lightTheme = lightTheme, darkTheme = darkTheme))
        }
    }

    private fun setLockMessages(lockMessages: LockMessages) {
        vm.sendIntent(LockIntent.OnLockMessagesChange(lockMessages))
    }

    abstract fun getSingleTheme(): LockTheme?
    abstract fun getLightTheme(): LockTheme?
    abstract fun getDarkTheme(): LockTheme?
    abstract fun getLockMessages(): LockMessages
    abstract fun getDestinationClass(): Class<*>
}