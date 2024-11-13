package dev.mahdidroid.compose_lock.datastore

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SecureDataStore(
    private val context: Context,
    preferencesDataStoreName: String,
    keysetName: String,
    keysetPrefName: String,
    masterKeyUri: String
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = preferencesDataStoreName)

    private lateinit var aead: Aead

    init {
        initializeAead(keysetName, keysetPrefName, masterKeyUri)
    }

    private fun initializeAead(keysetName: String, keysetPrefName: String, masterKeyUri: String) {
        AeadConfig.register()
        val androidKeysetManager =
            AndroidKeysetManager.Builder().withSharedPref(context, keysetName, keysetPrefName)
                .withKeyTemplate(AeadKeyTemplates.AES256_GCM).withMasterKeyUri(masterKeyUri).build()

        aead = androidKeysetManager.keysetHandle.getPrimitive(Aead::class.java)
    }

    private fun encrypt(data: String): ByteArray {
        return aead.encrypt(data.toByteArray(Charsets.UTF_8), null)
    }

    private fun decrypt(data: ByteArray): String {
        return String(aead.decrypt(data, null), Charsets.UTF_8)
    }

    suspend fun saveString(key: String, value: String) {
        val encryptedValue = encrypt(value)
        val encodedValue = Base64.encodeToString(encryptedValue, Base64.DEFAULT)
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = encodedValue
        }
    }

    fun stringFlow(key: String): Flow<String> {
        return context.dataStore.data.map {
            val encodedValue = it[stringPreferencesKey(key)] ?: ""
            if (encodedValue.isNotEmpty()) {
                val encryptedValue = Base64.decode(encodedValue, Base64.DEFAULT)
                decrypt(encryptedValue)
            } else {
                ""
            }
        }
    }

    suspend fun saveIntWithoutEncryption(key: String, value: Int) {
        context.dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    fun intFlowWithoutEncryption(key: String): Flow<Int> {
        return context.dataStore.data.map {
            it[intPreferencesKey(key)] ?: 0
        }
    }

    suspend fun saveInt(key: String, value: Int) {
        val encryptedValue = encrypt(value.toString())
        val encodedValue = Base64.encodeToString(encryptedValue, Base64.DEFAULT)
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = encodedValue
        }
    }

    fun intFlow(key: String): Flow<Int> {
        return context.dataStore.data.map {
            val encodedValue = it[stringPreferencesKey(key)] ?: ""
            if (encodedValue.isNotEmpty()) {
                val encryptedValue = Base64.decode(encodedValue, Base64.DEFAULT)
                decrypt(encryptedValue).toInt()
            } else {
                0
            }
        }
    }

    suspend fun saveLong(key: String, value: Long) {
        val encryptedValue = encrypt(value.toString())
        val encodedValue = Base64.encodeToString(encryptedValue, Base64.DEFAULT)
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = encodedValue
        }
    }

    fun longFlow(key: String): Flow<Long> {
        return context.dataStore.data.map {
            val encodedValue = it[stringPreferencesKey(key)] ?: ""
            if (encodedValue.isNotEmpty()) {
                val encryptedValue = Base64.decode(encodedValue, Base64.DEFAULT)
                decrypt(encryptedValue).toLong()
            } else {
                0
            }
        }
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    fun booleanFlow(key: String): Flow<Boolean> {
        return context.dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: false
        }
    }
}