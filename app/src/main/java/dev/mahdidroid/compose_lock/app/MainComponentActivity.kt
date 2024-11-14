package dev.mahdidroid.compose_lock.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mahdidroid.compose_lock.activities.LockComponentFactory
import dev.mahdidroid.compose_lock.app.ui.theme.MyApplicationTheme
import dev.mahdidroid.compose_lock.utils.AuthState
import dev.mahdidroid.compose_lock.utils.LocalAuthAction
import dev.mahdidroid.compose_lock.utils.LocalAuthData
import dev.mahdidroid.compose_lock.utils.LockActions

class MainComponentActivity : ComponentActivity() {

    private val lockDelegate by lazy { LockComponentFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                lockDelegate.ProvideAuthAction {
                    val auth = LocalAuthAction.current
                    val authData = LocalAuthData.current

                    var showSetting by remember { mutableStateOf(false) }
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        if (showSetting) {
                            BackHandler { showSetting = false }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(innerPadding)
                                    .padding(top = 16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Pin Status",
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    Switch(checked = authData.defaultAuthState == AuthState.Pin,
                                        onCheckedChange = {
                                            if (authData.defaultAuthState == AuthState.Pin) auth.invoke(
                                                LockActions.OnSetDefaultValue(AuthState.NoAuth)
                                            )
                                            else auth.invoke(
                                                LockActions.OnSetDefaultValue(AuthState.Pin)
                                            )
                                        })
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Finger print status",
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    Switch(
                                        checked = authData.isFingerprintEnabled, onCheckedChange = {
                                            if (authData.isFingerprintEnabled) auth.invoke(
                                                LockActions.OnDisableFingerprints
                                            )
                                            else auth.invoke(LockActions.OnEnableFingerprints)
                                        }, enabled = authData.defaultAuthState == AuthState.Pin
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        auth.invoke(
                                            LockActions.OnOpenScreenNow(
                                                AuthState.ChangePin
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    enabled = authData.defaultAuthState == AuthState.Pin
                                ) {
                                    Text("Change Pin", modifier = Modifier.padding(16.dp))
                                }


                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .padding(innerPadding),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("App Main screen")
                                Button(onClick = { showSetting = true }) {
                                    Text("Setting")
                                }

                                Button(onClick = {
                                    auth.invoke(LockActions.OnOpenScreenNow(AuthState.Pin))
                                }) {
                                    Text(text = "open pin now")
                                }

                                Button(onClick = { auth.invoke(LockActions.OnSetDefaultValue(AuthState.ChangePin)) }) {
                                    Text(text = "change pin")
                                }

                                Button(onClick = { auth.invoke(LockActions.OnSetDefaultValue(AuthState.Pin)) }) {
                                    Text(text = "set pin as default")
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

    override fun onResume() {
        super.onResume()
        lockDelegate.onResume()
    }
}