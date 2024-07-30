package dev.mahdidroid.compose_lock.initial

import dev.mahdidroid.compose_lock.AuthenticationViewModel
import org.koin.dsl.module

internal val composeLockModules = module {
    single { AuthenticationViewModel() }
}