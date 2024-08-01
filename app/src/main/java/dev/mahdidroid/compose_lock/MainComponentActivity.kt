package dev.mahdidroid.compose_lock

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.mahdidroid.compose_lock.activities.BaseLockComponentActivity
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme
import dev.mahdidroid.compose_lock.ui.theme.MyApplicationTheme

class MainComponentActivity : BaseLockComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SetLockTheme { light, dark ->
                    setLockTheme(lightTheme = light, darkTheme = dark)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Text(text = "Main Screen", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun SetLockTheme(onThemeSet: (LockTheme, LockTheme) -> Unit) {
    onThemeSet(
        LockTheme(
            pinTheme = PinEntryData(
                backgroundColor = Color.White,
                titleColor = Color.Black,
                pinIndicatorTheme = PinIndicatorTheme(
                    filled = false,
                    filledColor = Color.Black,
                    unfilledColor = Color.LightGray,
                    borderColor = Color.DarkGray
                ),
                numberButtonTheme = NumberButtonTheme(
                    buttonColor = Color.LightGray, textColor = Color.Black
                ),
                iconButtonTheme = IconButtonTheme(
                    iconColor = Color.Black, backgroundColor = Color.LightGray.copy(alpha = 0.7f)
                )
            )
        ), LockTheme(
            pinTheme = PinEntryData(
                backgroundColor = Color.Black,
                titleColor = Color.White,
                pinIndicatorTheme = PinIndicatorTheme(
                    filled = false,
                    filledColor = Color.White,
                    unfilledColor = Color.DarkGray,
                    borderColor = Color.LightGray
                ),
                numberButtonTheme = NumberButtonTheme(
                    buttonColor = Color.DarkGray, textColor = Color.White
                ),
                iconButtonTheme = IconButtonTheme(
                    iconColor = Color.White, backgroundColor = Color.DarkGray.copy(alpha = 0.7f)
                )
            )
        )
    )
}