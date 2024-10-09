package ru.raysmith.tgbot.model.bot.message

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.LivePeriodSerializer

/**New period during which the location can be updated, starting from the message send date */
@Serializable(with = LivePeriodSerializer::class)
sealed class LivePeriod {

    @Serializable
    data class Duration(val value: kotlin.time.Duration) : LivePeriod()

    @Serializable
    data class Seconds(val value: Int) : LivePeriod()

    /** Location can be updated forever */
    @Serializable
    data object Indefinitely : LivePeriod()
}