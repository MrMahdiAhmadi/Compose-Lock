package dev.mahdidroid.compose_lock.initial

import android.content.Context
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferencesImpl
import dev.mahdidroid.compose_lock.datastore.SecureDataStore
import dev.mahdidroid.compose_lock.ui.pin.PinViewModel
import dev.mahdidroid.compose_lock.ui.set_pin.enter_current_pin.NewPinViewModel
import dev.mahdidroid.compose_lock.utils.AuthStateManager
import dev.mahdidroid.compose_lock.utils.LockViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal fun composeLockModules(
    context: Context,
    preferencesDataStoreName: String = "ComposeLockPreferencesDataStore",
    keysetName: String = "master_keyset",
    keysetPrefName: String = "master_key_preference",
    masterKeyUri: String = "android-keystore://master_key"
) = module {
    single {
        SecureDataStore(
            context, preferencesDataStoreName, keysetName, keysetPrefName, masterKeyUri
        )
    }
    single { AuthStateManager() }
    single<ComposeLockPreferences> { ComposeLockPreferencesImpl(get()) }
    single { LockViewModel(get()) }

    viewModelOf(::PinViewModel)
    viewModelOf(::NewPinViewModel)

}