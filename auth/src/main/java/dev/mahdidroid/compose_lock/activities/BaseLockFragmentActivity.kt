package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.fragment.app.FragmentActivity
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockIntent
import dev.mahdidroid.compose_lock.utils.LockMessages
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.android.ext.android.inject

abstract class BaseLockFragmentActivity : FragmentActivity() {
    private val vm: LockViewModel by inject()

    override fun onResume() {
        super.onResume()
        if (vm.viewState.value.authState != AuthState.Main) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        vm.sendIntent(LockIntent.OnUpdateScreenState(AuthState.Pin))
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
}