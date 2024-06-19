package ru.raysmith.tgbot.model.network.bisiness

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

/** Describes the opening hours of a business. */
@Serializable
data class BusinessOpeningHours(

    /** Unique name of the time zone for which the opening hours are defined */
    @SerialName("time_zone_name") val timeZoneName: String,

    /** List of time intervals describing business opening hours */
    @SerialName("opening_hours") val openingHours: List<BusinessOpeningHoursInterval>,
) {

    // TODO tests with business account

    fun getWorkingTime(dayOfWeek: DayOfWeek) = openingHours[dayOfWeek.value - 1].getWorkingTime()
}