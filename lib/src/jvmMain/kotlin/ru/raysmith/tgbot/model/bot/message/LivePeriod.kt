package ru.raysmith.tgbot.model.bot.message

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.LivePeriodDurationSerializer
import ru.raysmith.tgbot.model.serializer.LivePeriodIndefinitelySerializer
import ru.raysmith.tgbot.model.serializer.LivePeriodSecondsSerializer
import ru.raysmith.tgbot.model.serializer.LivePeriodSerializer

/** New period during which the location can be updated, starting from the message send date */
@Serializable(LivePeriodSerializer::class)
sealed interface LivePeriod {

    @Serializable(LivePeriodDurationSerializer::class)
    data class Duration(val value: kotlin.time.Duration) : LivePeriod

    @Serializable(LivePeriodSecondsSerializer::class)
    data class Seconds(val value: Int) : LivePeriod

    /** Location can be updated forever */
    @Serializable(LivePeriodIndefinitelySerializer::class)
    data object Indefinitely : LivePeriod
}
