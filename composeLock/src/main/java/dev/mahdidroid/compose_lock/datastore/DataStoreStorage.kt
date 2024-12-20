package dev.mahdidroid.compose_lock.datastore

import dev.mahdidroid.compose_lock.R
import dev.mahdidroid.compose_lock.utils.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

private object DataStoreKeys {
    const val PIN = "pin"
    const val PIN_LENGTH = "pin_length"
    const val PASSWORD = "password"
    const val AUTH_STATE = "auth_state"
    const val UNLOCK_DURATION = "unlock_duration"
    const val FINGERPRINT_STATUS = "fingerprint_status"
}

internal interface ComposeLockPreferences {
    suspend fun updatePin(value: String)
    suspend fun getPin(): Flow<String>
    suspend fun updatePinLength(value: Int)
    suspend fun getPinLength(): Flow<Int>
    suspend fun updatePassword(value: String)
    suspend fun getPassword(): Flow<String>
    suspend fun updateAuthState(value: Int)
    suspend fun getAuthState(): AuthState
    suspend fun updateUnlockDuration(value: Long)
    suspend fun getUnlockDuration(): Flow<Long>
    suspend fun updateFingerprintStatus(value: Boolean)
    suspend fun getFingerprintStatus(): Flow<Boolean>
}


internal class ComposeLockPreferencesImpl(
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

    override suspend fun updateAuthState(value: Int) =
        secureDataStore.saveIntWithoutEncryption(DataStoreKeys.AUTH_STATE, value)

    override suspend fun getAuthState(): AuthState =
        when (secureDataStore.intFlowWithoutEncryption(DataStoreKeys.AUTH_STATE).first()) {
            R.string.AuthStatePin -> AuthState.Pin
            R.string.AuthStatePassword -> AuthState.Password
            R.string.AuthStateChangePin -> AuthState.ChangePin
            R.string.AuthStateChangePassword -> AuthState.ChangePassword
            R.string.AuthStateNoAuth -> AuthState.NoAuth
            else -> AuthState.ChangePin
        }

    override suspend fun updateUnlockDuration(value: Long) =
        secureDataStore.saveLong(DataStoreKeys.UNLOCK_DURATION, value)

    override suspend fun getUnlockDuration(): Flow<Long> =
        secureDataStore.longFlow(DataStoreKeys.UNLOCK_DURATION)

    override suspend fun updateFingerprintStatus(value: Boolean) =
        secureDataStore.saveBoolean(DataStoreKeys.FINGERPRINT_STATUS, value)


    override suspend fun getFingerprintStatus(): Flow<Boolean> =
        secureDataStore.booleanFlow(DataStoreKeys.FINGERPRINT_STATUS)

}