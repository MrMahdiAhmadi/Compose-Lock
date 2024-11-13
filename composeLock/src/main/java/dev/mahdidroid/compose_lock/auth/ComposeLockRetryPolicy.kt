package dev.mahdidroid.compose_lock.auth

/**
 * A data class that defines the retry policy configuration for handling
 * multiple unsuccessful attempts within an application.
 *
 * @property retryPolicies A map defining the number of attempts and the corresponding
 *                         wait time in milliseconds before the next attempt is allowed.
 *                         The key represents the number of attempts, and the value
 *                         represents the wait time associated with that attempt count.
 *
 *                         Example:
 *                         - 3 to 30000: After 3 failed attempts, the user has to wait 30 seconds.
 *                         - 5 to 120000: After 5 failed attempts, the user has to wait 120 seconds.
 *                         - 7 to 300000: After 7 failed attempts, the user has to wait 5 minutes.
 *                         - 10 to 600000: After 10 failed attempts, the user has to wait 10 minutes.
 *
 *                         Developers can customize this map according to the desired retry policy.
 *
 * @property afterMaxAttemptsPolicy The policy to be executed after the maximum number of attempts
 *                                  has been reached (as defined in `retryPolicies`). This policy is
 *                                  of type `AfterMaxAttemptsPolicy`, which can be one of several
 *                                  different actions, such as locking the account, increasing the
 *                                  wait time, or showing a "Forgot Password?" option.
 *                                  Default is `ContinueNormal`, which allows the process to continue
 *                                  without any additional actions.
 *
 * @property retryBehaviorPolicy The policy defining how failed attempts and penalty times are handled
 *                               across app restarts or after a period of inactivity. This policy is
 *                               of type `RetryBehaviorPolicy`, which offers several different approaches,
 *                               such as resetting all attempts on restart, preserving penalty times, or
 *                               imposing a strict mode where both attempts and penalties are maintained.
 *                               Developers can choose the policy based on the desired balance between security
 *                               and user convenience.
 */
data class ComposeLockRetryPolicy(
    val retryPolicies: Map<Int, Long> = mapOf(
        3 to 30000, 5 to 120000, 7 to 300000, 10 to 600000
    ), val afterMaxAttemptsPolicy: AfterMaxAttemptsPolicy = AfterMaxAttemptsPolicy.ContinueNormal,
    // todo handle this
    val retryBehaviorPolicy: RetryBehaviorPolicy = RetryBehaviorPolicy.StrictMode
)