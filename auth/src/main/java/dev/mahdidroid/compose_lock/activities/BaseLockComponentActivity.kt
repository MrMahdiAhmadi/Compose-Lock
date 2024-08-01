package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import androidx.activity.ComponentActivity
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject


abstract class BaseLockComponentActivity : ComponentActivity() {
    private val vm: LockViewModel by inject()

    override fun onResume() {
        super.onResume()

        if (vm.state.value != AuthState.Main) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        // todo don't call this when theme change
        vm.sendIntent(LockIntent.OnSwitchScreen(AuthState.Pin))
    }

    fun setLockTheme(singleTheme: LockTheme) =
        vm.sendIntent(LockIntent.OnSingleTheme(theme = singleTheme))

    fun setLockTheme(lightTheme: LockTheme, darkTheme: LockTheme) =
        vm.sendIntent(LockIntent.OnTwoTheme(lightTheme = lightTheme, darkTheme = darkTheme))
}