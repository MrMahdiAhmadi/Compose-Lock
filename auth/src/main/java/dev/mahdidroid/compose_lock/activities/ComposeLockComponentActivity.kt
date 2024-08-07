package dev.mahdidroid.compose_lock.activities

import android.content.Intent
import androidx.activity.ComponentActivity
import dev.mahdidroid.compose_lock.AuthenticationViewModel
import org.koin.android.ext.android.inject

abstract class ComposeLockComponentActivity : ComponentActivity() {
    private val vm: AuthenticationViewModel by inject()

    override fun onResume() {
        super.onResume()
        if (!vm.isAuthenticated.value) {
            startActivity(Intent(this, ComposeAuthActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        vm.setAuthenticated(false)
    }
}