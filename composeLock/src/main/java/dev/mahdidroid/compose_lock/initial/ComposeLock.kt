package dev.mahdidroid.compose_lock.initial

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object ComposeLock {

    @Synchronized
    fun initialize(
        context: Context,
        preferencesDataStoreName: String = "ComposeLockPreferencesDataStore",
        keysetName: String = "master_keyset",
        keysetPrefName: String = "master_key_preference",
        masterKeyUri: String = "android-keystore://master_key"
    ) {
        if (context is Application) {
            startKoin(context, preferencesDataStoreName, keysetName, keysetPrefName, masterKeyUri)
        } else {
            throw IllegalArgumentException("Context should be an instance of Application")
        }
    }

    private fun startKoin(
        context: Application,
        preferencesDataStoreName: String,
        keysetName: String,
        keysetPrefName: String,
        masterKeyUri: String
    ) {
        startKoin {
            androidContext(context)
            modules(
                composeLockModules(
                    context, preferencesDataStoreName, keysetName, keysetPrefName, masterKeyUri
                )
            )
        }
    }
}