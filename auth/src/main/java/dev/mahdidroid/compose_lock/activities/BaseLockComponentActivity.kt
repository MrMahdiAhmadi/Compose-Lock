package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import androidx.activity.ComponentActivity
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LockViewModel
import dev.mahdidroid.compose_lock.utils.ThemeConfiguration
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
        vm.switchScreen(AuthState.ChangePin)
    }

    fun applyTheme(themeConfiguration: ThemeConfiguration) = vm.setTheme(themeConfiguration)
}