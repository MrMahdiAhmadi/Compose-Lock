package dev.mahdidroid.compose_lock

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mahdidroid.compose_lock.activities.BaseSplashActivity
import dev.mahdidroid.compose_lock.theme.ComposeLockTheme
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.ui.pin.PinEntryData
import dev.mahdidroid.compose_lock.ui.pin.composable.IconButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.NumberButtonTheme
import dev.mahdidroid.compose_lock.ui.pin.composable.PinIndicatorTheme
import dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin.NewPinConfirm
import dev.mahdidroid.compose_lock.utils.BiometricMessages
import dev.mahdidroid.compose_lock.utils.LockMessages

class SplashActivity : BaseSplashActivity() {

    override fun getComposeLockTheme(): ComposeLockTheme = ComposeLockTheme.SingleTheme(
        LockTheme(
            PinEntryData(
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

    override fun getLockMessages(): LockMessages = LockMessages(
        pinTitleMessage = "test new message",
        biometricMessages = BiometricMessages(title = "skvjsklvjlksdafjvldksfjvdf")
    )

    override fun getContentActivityClass(): Class<*> = MainComponentActivity::class.java
}