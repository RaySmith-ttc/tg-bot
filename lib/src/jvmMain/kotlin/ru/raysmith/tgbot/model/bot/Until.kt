package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.UntilDateSerializer
import ru.raysmith.tgbot.model.serializer.UntilForeverSerializer
import ru.raysmith.tgbot.model.serializer.UntilSerializer
import java.time.ZonedDateTime

@Serializable(UntilSerializer::class)
sealed interface Until {

    @Serializable(UntilForeverSerializer::class)
    data object Forever : Until

    @Serializable(UntilDateSerializer::class)
    data class Date(val date: ZonedDateTime) : Until
}
