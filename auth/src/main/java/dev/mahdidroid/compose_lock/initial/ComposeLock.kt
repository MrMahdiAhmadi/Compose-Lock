package dev.mahdidroid.compose_lock.initial

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object ComposeLock {

    @Synchronized
    fun initialize(context: Context) {
        if (context is Application) {
            startKoin(context)
        } else {
            throw IllegalArgumentException("Context should be an instance of Application")
        }
    }

    private fun startKoin(context: Application) {
        startKoin {
            androidContext(context)
            modules(composeLockModules)
        }
    }
}