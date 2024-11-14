package dev.mahdidroid.compose_lock.activities

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.mahdidroid.compose_lock.activities.vm.LockIntent
import dev.mahdidroid.compose_lock.activities.vm.LockViewModel
import dev.mahdidroid.compose_lock.utils.AuthData
import dev.mahdidroid.compose_lock.utils.LocalAuthAction
import dev.mahdidroid.compose_lock.utils.LocalAuthData
import dev.mahdidroid.compose_lock.utils.LockActions
import org.koin.java.KoinJavaComponent.getKoin

interface LockComponentDelegate {

    fun onResume()

    fun handleAuthAction(action: LockActions)

    @Composable
    fun ProvideAuthAction(content: @Composable () -> Unit)
}

object LockComponentFactory {
    fun create(context: Context): LockComponentDelegate {
        return LockComponentDelegateImpl(context, getKoin().inject<LockViewModel>().value)
    }
}


internal class LockComponentDelegateImpl(
    private val context: Context, private val vm: LockViewModel
) : LockComponentDelegate {

    override fun onResume() {
        if (vm.isShowLockOnResume()) context.startActivity(
            Intent(
                context, AuthenticationActivity::class.java
            )
        )
        else vm.sendIntent(LockIntent.OnShowPinOnResume)
    }

    override fun handleAuthAction(action: LockActions) {
        when (action) {
            is LockActions.OnOpenScreenNow -> {
                vm.sendIntent(LockIntent.OnUpdateScreenState(action.value))
                context.startActivity(Intent(context, AuthenticationActivity::class.java))
            }

            is LockActions.OnSetDefaultValue -> {
                vm.sendIntent(LockIntent.OnSetDefaultAuth(action.value))
            }

            LockActions.OnDisableFingerprints -> {
                vm.sendIntent(LockIntent.OnFingerprintStateChange(false))
            }

            LockActions.OnEnableFingerprints -> {
                vm.sendIntent(LockIntent.OnFingerprintStateChange(true))
            }
        }
    }

    @Composable
    override fun ProvideAuthAction(content: @Composable () -> Unit) {
        val authData = AuthData(
            isFingerprintEnabled = vm.viewState.value.isFingerprintsEnabled,
            defaultAuthState = vm.viewState.value.defaultAuthState
        )
        CompositionLocalProvider(
            LocalAuthAction provides this::handleAuthAction, LocalAuthData provides authData
        ) {
            content()
        }
    }
}