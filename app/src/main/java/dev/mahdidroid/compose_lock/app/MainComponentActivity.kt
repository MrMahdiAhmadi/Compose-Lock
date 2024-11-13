package dev.mahdidroid.compose_lock.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dev.mahdidroid.compose_lock.activities.BaseLockComponentActivity
import dev.mahdidroid.compose_lock.app.ui.theme.MyApplicationTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LocalAuthAction
import dev.mahdidroid.compose_lock.utils.LockActions

class MainComponentActivity : BaseLockComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ProvideAuthAction {
                    val auth = LocalAuthAction.current

                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Text(text = "Main Screen", color = MaterialTheme.colorScheme.primary)

                            Button(onClick = {
                                auth.invoke(LockActions.OnOpenScreenNow(AuthState.Pin))
                            }) {
                                Text(text = "open pin now")
                            }

                            Button(onClick = { auth.invoke(LockActions.OnSetDefaultValue(AuthState.ChangePin)) }) {
                                Text(text = "change pin")
                            }

                            Button(onClick = { auth.invoke(LockActions.OnSetDefaultValue(AuthState.NoAuth)) }) {
                                Text(text = "turn off auth")
                            }
                        }
                    }
                }
            }
        }
    }
}