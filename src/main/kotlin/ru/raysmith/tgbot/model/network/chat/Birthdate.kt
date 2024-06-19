package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.MonthDay

/** Describes the birthdate of a user. */
@Serializable
data class Birthdate(

    /** Day of the user's birth; 1-31 */
    val day: Int,

    /** Month of the user's birth; 1-12 */
    val month: Int,

    /** Year of the user's birth */
    val year: Int? = null,
) {

    /** Returns [MonthDay] representation */
    fun toMonthDay() = MonthDay.of(month, day)

    /** Returns [LocalDate] representation or null if year is null */
    fun toLocalDateOrNull() = year?.let { LocalDate.of(it, month, day) }
}
