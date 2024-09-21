package ru.raysmith.tgbot.model.network.bisiness

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalTime

/** Describes an interval of time during which a business is open. */
@Serializable
data class BusinessOpeningHoursInterval(

    /**
     * The minute's sequence number in a week, starting on Monday, marking the start of the time interval
     * during which the business is open; 0 - 7 * 24 * 60
     * */
    @SerialName("opening_minute") val openingMinute: Int,

    /**
     * The minute's sequence number in a week, starting on Monday, marking the end of the time interval
     * during which the business is open; 0 - 8 * 24 * 60
     * */
    @SerialName("closing_minute") val closingMinute: Int,
) {

    // TODO tests with business account

    fun getOpeningTime(): LocalTime = LocalTime.of(openingMinute / 24, openingMinute / (24 * 60))
    fun getClosingTime(): LocalTime = LocalTime.of(closingMinute / 24, closingMinute / (24 * 60))
    fun getWorkingTime() = getOpeningTime()..getClosingTime()
}

