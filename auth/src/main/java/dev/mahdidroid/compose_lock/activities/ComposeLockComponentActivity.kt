package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import androidx.activity.ComponentActivity
import dev.mahdidroid.compose_lock.utils.AuthScreen
import dev.mahdidroid.compose_lock.utils.AuthViewModel
import org.koin.android.ext.android.inject

abstract class ComposeLockComponentActivity : ComponentActivity() {
    private val vm: AuthViewModel by inject()

    override fun onResume() {
        super.onResume()

        if (vm.state.value != AuthScreen.Main) {
            startActivity(Intent(this, ComposeAuthActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        vm.changeScreen(AuthScreen.Pin)
    }
}