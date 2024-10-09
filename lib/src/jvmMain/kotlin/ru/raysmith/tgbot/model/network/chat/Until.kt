package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.UntilSerializer
import ru.raysmith.tgbot.network.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

@Serializable(with = UntilSerializer::class)
sealed class Until {

    @Serializable
    data object Forever : Until()

    @Serializable
    data class Date(@Serializable(with = ZonedDateTimeAsUnixSerializer::class) val date: ZonedDateTime) : Until() // TODO move to contextual; tests
}