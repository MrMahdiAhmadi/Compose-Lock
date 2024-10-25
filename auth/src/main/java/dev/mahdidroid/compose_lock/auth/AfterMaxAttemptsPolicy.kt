package dev.mahdidroid.compose_lock.auth

/**
 * A sealed class representing the various policies that can be executed
 * after a maximum number of unsuccessful attempts have been made.
 */
sealed class AfterMaxAttemptsPolicy {

    /**
     * Policy to lock the user's account after too many failed attempts.
     *
     * @property message The message to be displayed to the user when their account is locked.
     * @property contactNumber The support contact number that the user can call.
     * @property contactEmail The support email address that the user can reach out to.
     */
    data class LockAccount(
        val message: String, val contactNumber: String, val contactEmail: String
    ) : AfterMaxAttemptsPolicy()

    /**
     * Policy to continue without any changes, allowing the user to proceed
     * with the normal flow after the maximum attempts have been reached.
     *
     * This policy essentially does nothing and allows the system to behave as it
     * did before, without imposing any additional penalties or actions.
     */
    data object ContinueNormal : AfterMaxAttemptsPolicy()

    /**
     * Policy to increase the wait time after the maximum attempts have been reached.
     *
     * @property increaseBy The amount by which the wait time should be increased, in milliseconds.
     * @property multiplier If true, the wait time will be multiplied by the increaseBy value.
     *                      If false, the wait time will be increased by the exact increaseBy value.
     *
     * Example:
     * - If increaseBy is 60000 (60 seconds) and multiplier is false,
     *   the new wait time will be the previous wait time + 60 seconds.
     * - If increaseBy is 2 and multiplier is true, the new wait time will be the previous wait time * 2.
     */
    data class IncreaseWaitTime(
        val increaseBy: Long, val multiplier: Boolean = false
    ) : AfterMaxAttemptsPolicy()

    /**
     * Policy to completely lock the application, requiring the user to either
     * reinstall the app or clear the app's data to continue.
     *
     * @property message The message explaining to the user that the app has been locked
     *                   and how they can resolve the issue.
     *
     * This policy is a drastic measure, intended to completely block access until
     * the user takes specific actions to reset the application.
     */
    data class LockApp(
        val message: String
    ) : AfterMaxAttemptsPolicy()

    /**
     * Policy to show a "Forgot Password?" button, allowing the user to reset their password
     * through an alternative recovery method.
     *
     * @property buttonTitle The title of the "Forgot Password?" button that will be displayed to the user.
     *
     * This policy is intended to provide an alternative path for the user to regain access
     * to their account if they've forgotten their password and have failed too many login attempts.
     */
    data class ShowForgotPassword(
        val buttonTitle: String
    ) : AfterMaxAttemptsPolicy()
}