package dev.mahdidroid.compose_lock

import android.app.Application
import dev.mahdidroid.compose_lock.initial.ComposeLock

class ComposeLockApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ComposeLock.initialize(this)
    }
}