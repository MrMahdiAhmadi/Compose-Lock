package dev.mahdidroid.compose_lock.auth

/**
 * Sealed class representing different retry policies for managing failed login attempts.
 * Developers can choose a policy based on the security and user experience requirements of their application.
 */
sealed class RetryBehaviorPolicy {

    /**
     * Resets all failed attempts and penalty times when the app is restarted.
     *
     * **Use Case:**
     * Ideal for applications where security is less critical, and the focus is on user convenience. Each session starts fresh, allowing users to retry without carrying over any penalties from previous sessions.
     */
    data object ResetOnRestart : RetryBehaviorPolicy()

    /**
     * Preserves the penalty time even if the app is restarted, but does not save the failed attempt count.
     *
     * **Use Case:**
     * Useful in scenarios where you want to enforce waiting periods between attempts but do not want to discourage users by preserving the failed attempt count. The user must wait out the penalty time, ensuring security without being overly strict.
     */
    data object PreservePenaltyTime : RetryBehaviorPolicy()

    /**
     * Stores both failed attempts and penalty times.
     *
     * **Use Case:**
     * Suitable for high-security applications where it's critical to maintain a record of failed attempts and enforce penalty times across sessions. Prevents users from bypassing security by restarting the app.
     */
    data object StrictMode : RetryBehaviorPolicy()

    /**
     * Resets the failed attempts and penalty times if the user does not attempt to log in within a specified timeout period.
     *
     * **Use Case:**
     * Ideal for applications that want to give users a break after a period of inactivity. This approach balances security with user convenience, offering a fresh start if users take a break.
     *
     * @param timeout The duration (in milliseconds) after which the failed attempts and penalty times are reset.
     */
    data class TimeoutReset(val timeout: Long) : RetryBehaviorPolicy()

    /**
     * Resets the failed attempts count but preserves penalty times when the app is restarted.
     *
     * **Use Case:**
     * Appropriate for applications where the penalty for failed attempts should remain but the user should be allowed to start with a clean slate for the number of attempts. It encourages careful behavior while still being forgiving.
     */
    data object ResetFailedAttemptsOnly : RetryBehaviorPolicy()

    /**
     * Resets the failed attempts and penalty times at regular intervals (e.g., every 24 hours).
     *
     * **Use Case:**
     * Best for applications where users are given a daily opportunity to start fresh, making it less frustrating for users while still imposing a time-based security measure.
     *
     * @param period The duration (in milliseconds) after which the failed attempts and penalty times are reset.
     */
    data class PeriodicReset(val period: Long) : RetryBehaviorPolicy()
}