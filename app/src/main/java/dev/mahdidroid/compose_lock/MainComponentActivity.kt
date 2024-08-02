package dev.mahdidroid.compose_lock

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mahdidroid.compose_lock.activities.BaseLockComponentActivity
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme
import dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin.NewPinConfirm
import dev.mahdidroid.compose_lock.ui.theme.MyApplicationTheme
import dev.mahdidroid.compose_lock.utils.BiometricMessages
import dev.mahdidroid.compose_lock.utils.LockMessages

class MainComponentActivity : BaseLockComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                /*  SetLockTheme { light, dark ->
                      setLockTheme(lightTheme = light, darkTheme = dark)
                  }*/
                setLockMessages(
                    lockMessages = LockMessages(
                        pinTitleMessage = "test change pin", biometricMessages = BiometricMessages(
                            title = "sdgsdfgdfgdfsgdfg",
                            subtitle = "1111111111111",
                            negativeButtonText = "test neg"
                        )
                    )
                )
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
                backgroundColor = Color(0xFFFAFAFA),
                titleColor = Color(0xFF333333),
                pinIndicatorTheme = PinIndicatorTheme(
                    filledColor = Color(0xFF00796B),
                    unfilledColor = Color(0xFFB2DFDB),
                    borderColor = Color(0xFF004D40),
                    errorColor = Color(0xFFE57373),
                    errorBoarderColor = Color(0xFFC62828)
                ),
                numberButtonTheme = NumberButtonTheme(
                    buttonColor = Color(0xFFE0F2F1), textColor = Color(0xFF00796B)
                ),
                iconButtonTheme = IconButtonTheme(
                    iconColor = Color(0xFF004D40), backgroundColor = Color.Transparent
                ),
                newPinConfirm = NewPinConfirm(
                    containerColor = Color(0xFF00796B),
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                )
            )
        ), LockTheme(
            pinTheme = PinEntryData(
                backgroundColor = Color(0xFF212121),
                titleColor = Color(0xFFE0E0E0),
                pinIndicatorTheme = PinIndicatorTheme(
                    filledColor = Color(0xFF64FFDA),
                    unfilledColor = Color(0xFF004D40),
                    borderColor = Color(0xFF80CBC4),
                    errorColor = Color(0xFFFF5252),
                    errorBoarderColor = Color(0xFFD50000)
                ),
                numberButtonTheme = NumberButtonTheme(
                    buttonColor = Color(0xFF37474F), textColor = Color(0xFF80CBC4)
                ),
                iconButtonTheme = IconButtonTheme(
                    iconColor = Color(0xFF80CBC4),
                    backgroundColor = Color(0xFF004D40).copy(alpha = 0.6f)
                ),
                newPinConfirm = NewPinConfirm(
                    containerColor = Color(0xFF80CBC4),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
            )
        )
    )
}