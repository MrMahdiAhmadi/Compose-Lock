package dev.mahdidroid.compose_lock.utils

/**
 * Enum class representing various activity result codes related to authentication outcomes.
 *
 * Each enum value corresponds to a specific authentication result and is associated with a unique integer code.
 *
 * @property code The integer code associated with the authentication result.
 */
internal enum class AuthResult(val code: Int) {

    /**
     * Fingerprint authentication was successful.
     * Code: 0
     */
    FINGERPRINT_SUCCESS(0),

    /**
     * Fingerprint authentication failed.
     * Code: 1
     */
    FINGERPRINT_FAILED(1),

    /**
     * An error occurred during fingerprint authentication.
     * Code: 2
     */
    FINGERPRINT_ERROR(2),

    /**
     * PIN authentication was successful.
     * Code: 3
     */
    PIN_SUCCESS(3),

    /**
     * PIN authentication failed.
     * Code: 4
     */
    PIN_FAILED(4),

    /**
     * Password authentication was successful.
     * Code: 5
     */
    PASSWORD_SUCCESS(5),

    /**
     * Password authentication failed.
     * Code: 6
     */
    PASSWORD_FAILED(6),

    /**
     * Represents an error or an unknown code.
     * Code: -1
     */
    Error(-1)
}

/**
 * Extension function that converts an integer code to its corresponding `ActivityResultCode` value.
 *
 * Searches through the `ActivityResultCode` enum entries to find the one that matches the provided code.
 * Returns the matching `ActivityResultCode` or `ActivityResultCode.Error` if no match is found.
 *
 * @return The corresponding `ActivityResultCode`, or `ActivityResultCode.Error` if no match is found.
 */
internal fun AuthResult.codeToActivityRequestCode(): AuthResult =
    AuthResult.entries.find { it.code == code } ?: AuthResult.Error

fun checkAuthResultIsSuccess(code: Int): Boolean =
    code == AuthResult.FINGERPRINT_SUCCESS.code || code == AuthResult.PIN_SUCCESS.code || code == AuthResult.PASSWORD_SUCCESS.code