# Compose-Lock

A Jetpack Compose Android library for PIN and biometric authentication with encrypted storage.

![Min SDK](https://img.shields.io/badge/Min%20SDK-24-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-✓-brightgreen)

---

## Status

> ⚠️ This library is incomplete and not production-ready.
> Development paused

---

## Features

- **PIN authentication** — numeric keypad UI with configurable PIN length
- **Biometric / fingerprint** — integrates with the Android system biometric prompt
- **Encrypted storage** — PIN and settings stored using Google Tink (AES-256-GCM) via Android Keystore
- **Retry policy** — configurable lockout delays after failed attempts
- **MVI architecture** — predictable state management with `LockViewModel`
- **Customizable theme** — single or day/night `LockTheme` support
- **Haptic feedback** — phone vibration on failed attempts

---

## Requirements

| Requirement | Version |
|---|---|
| Android Min SDK | 24 |
| Target SDK | 34 |
| Kotlin | 2.0+ |
| Jetpack Compose | 2024.06.00+ |

---

## Installation

Clone or download the repository, then add the `composeLock` module to your project:

```kotlin
// settings.gradle.kts
include(":composeLock")
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(project(":composeLock"))
}
```

---

## Setup

### 1. Initialize in your Application class

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ComposeLock.initialize(this)
    }
}
```

All parameters have sensible defaults. You can override them to avoid conflicts with other libraries that use Tink:

```kotlin
ComposeLock.initialize(
    context = this,
    preferencesDataStoreName = "MyAppPreferencesDataStore",
    keysetName = "my_master_keyset",
    keysetPrefName = "my_master_key_pref",
    masterKeyUri = "android-keystore://my_master_key"
)
```

| Parameter | Default | Description |
|---|---|---|
| `context` | — | Must be an `Application` instance |
| `preferencesDataStoreName` | `"ComposeLockPreferencesDataStore"` | DataStore file name |
| `keysetName` | `"master_keyset"` | Tink keyset name |
| `keysetPrefName` | `"master_key_preference"` | Tink key preference name |
| `masterKeyUri` | `"android-keystore://master_key"` | Android Keystore key URI |

### 2. Register your Application class in AndroidManifest.xml

```xml
<application
    android:name=".MyApp"
    ...>
```

---

## Usage

### Wire up the lock delegate in your Activity

```kotlin
class MainActivity : ComponentActivity() {

    private val lockDelegate by lazy { LockComponentFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            lockDelegate.ProvideAuthAction {
                val auth = LocalAuthAction.current
                val authData = LocalAuthData.current

                // Your app content here
                MyAppContent(auth = auth, authData = authData)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lockDelegate.onResume() // triggers lock screen if auth is enabled
    }
}
```

### Trigger authentication actions

```kotlin
val auth = LocalAuthAction.current

// Show PIN screen immediately
auth.invoke(LockActions.OnOpenScreenNow(AuthState.Pin))

// Set PIN as the default auth on app resume
auth.invoke(LockActions.OnSetDefaultValue(AuthState.Pin))

// Open the change-PIN flow
auth.invoke(LockActions.OnSetDefaultValue(AuthState.ChangePin))
// or
auth.invoke(LockActions.OnOpenScreenNow(AuthState.ChangePin))

// Disable authentication
auth.invoke(LockActions.OnSetDefaultValue(AuthState.NoAuth))

// Enable / disable fingerprint
auth.invoke(LockActions.OnEnableFingerprints)
auth.invoke(LockActions.OnDisableFingerprints)
```

### Read current auth state

```kotlin
val authData = LocalAuthData.current

val isPinEnabled = authData.defaultAuthState == AuthState.Pin
val isFingerprintOn = authData.isFingerprintEnabled
```

---

## Retry Policy

Configure lockout delays after failed PIN attempts:

```kotlin
val policy = ComposeLockRetryPolicy(
    retryPolicies = mapOf(
        3 to 30_000L,    // wait 30s after 3 failed attempts
        5 to 120_000L,   // wait 2min after 5 failed attempts
        7 to 300_000L,   // wait 5min after 7 failed attempts
        10 to 600_000L   // wait 10min after 10 failed attempts
    ),
    afterMaxAttemptsPolicy = AfterMaxAttemptsPolicy.ContinueNormal,
    retryBehaviorPolicy = RetryBehaviorPolicy.StrictMode
)
```

Pass the policy when opening the lock screen (integration with the ViewModel — see `LockViewModel`).

### `AfterMaxAttemptsPolicy` options

| Value | Behavior |
|---|---|
| `ContinueNormal` | No additional action after max attempts (default) |

### `RetryBehaviorPolicy` options

| Value | Behavior |
|---|---|
| `StrictMode` | Attempts and penalty times persist across app restarts (default) |

---

## Theming

Provide a single theme or separate light/dark themes:

```kotlin
// Single theme
ComposeLockTheme.SingleTheme(theme = myLockTheme)

// Day/night themes
ComposeLockTheme.DayNightTheme(
    lightTheme = lightLockTheme,
    darkTheme = darkLockTheme
)
```

`LockTheme` wraps a `PinEntryData` that controls the visual appearance of the PIN entry screen (colors, button styles, indicator shapes, etc.).

---

## Architecture

The library uses **MVI (Model-View-Intent)**:

| Layer | Class | Role |
|---|---|---|
| Model | `LockViewState`, `PinViewState` | Immutable UI state |
| View | `PinEntryScreen`, `AuthenticationContent` | Jetpack Compose UI |
| Intent | `LockIntent`, `PinIntent` | User actions dispatched to ViewModel |
| ViewModel | `LockViewModel`, `PinViewModel` | State management |
| Storage | `SecureDataStore` | Encrypted persistence via Tink |

Auth actions are accessed anywhere in the Compose tree via `CompositionLocal`:

```kotlin
val auth = LocalAuthAction.current   // dispatch actions
val data = LocalAuthData.current     // read current auth state
```

---

## Running the Sample App

```bash
./gradlew :app:installDebug       # install on connected device
./gradlew :app:assembleDebug      # build APK only
./gradlew test                    # run unit tests
./gradlew connectedAndroidTest    # run instrumentation tests
```

---

## License

```
Copyright (c) 2022 Mahdi Ahmadi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
