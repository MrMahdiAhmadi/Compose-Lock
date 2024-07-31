package dev.mahdidroid.compose_lock.datastore

import kotlinx.coroutines.flow.Flow

private object DataStoreKeys {
    const val PIN = "pin"
    const val PIN_LENGTH = "pin_length"
    const val PASSWORD = "password"
}

interface ComposeLockPreferences {
    suspend fun updatePin(value: String)
    suspend fun getPin(): Flow<String>
    suspend fun updatePinLength(value: Int)
    suspend fun getPinLength(): Flow<Int>
    suspend fun updatePassword(value: String)
    suspend fun getPassword(): Flow<String>
}

class ComposeLockPreferencesImpl(
    private val secureDataStore: SecureDataStore
) : ComposeLockPreferences {

    override suspend fun updatePin(value: String) {
        secureDataStore.saveString(DataStoreKeys.PIN, value)
    }

    override suspend fun getPin(): Flow<String> = secureDataStore.stringFlow(DataStoreKeys.PIN)

    override suspend fun updatePinLength(value: Int) =
        secureDataStore.saveInt(DataStoreKeys.PIN_LENGTH, value)

    override suspend fun getPinLength(): Flow<Int> =
        secureDataStore.intFlow(DataStoreKeys.PIN_LENGTH)

    override suspend fun updatePassword(value: String) {
        secureDataStore.saveString(DataStoreKeys.PASSWORD, value)
    }

    override suspend fun getPassword(): Flow<String> =
        secureDataStore.stringFlow(DataStoreKeys.PASSWORD)
}