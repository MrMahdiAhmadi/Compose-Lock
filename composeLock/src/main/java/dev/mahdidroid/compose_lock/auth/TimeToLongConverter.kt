package dev.mahdidroid.compose_lock.auth

/**
 * A utility class for converting time (hours, minutes, and seconds) into milliseconds.
 * This class uses a builder pattern for easy and flexible construction.
 *
 * Example usage:
 * ```
 * val durationInMillis = TimeToLongConverter.build {
 *     hours = 1
 *     minutes = 30
 *     seconds = 45
 * }
 * ```
 *
 * @property hours The number of hours to be converted.
 * @property minutes The number of minutes to be converted.
 * @property seconds The number of seconds to be converted.
 */
class TimeToLongConverter private constructor(
    private val hours: Int, private val minutes: Int, private val seconds: Int
) {
    fun build(): Long {
        return hours.toLong() * 3600000 + minutes.toLong() * 60000 + seconds.toLong() * 1000
    }

    companion object {
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var hours: Int = 0
        var minutes: Int = 0
        var seconds: Int = 0

        fun build() = TimeToLongConverter(hours, minutes, seconds).build()
    }
}