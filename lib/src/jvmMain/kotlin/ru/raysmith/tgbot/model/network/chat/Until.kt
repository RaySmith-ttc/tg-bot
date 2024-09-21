package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.UntilSerializer
import java.time.ZonedDateTime

@Serializable(with = UntilSerializer::class)
sealed class Until {
    data object Forever : Until()
    data class Date(val date: ZonedDateTime) : Until()
}