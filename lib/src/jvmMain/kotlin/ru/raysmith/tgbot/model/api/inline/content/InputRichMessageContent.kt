package ru.raysmith.tgbot.model.api.inline.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.InputRichMessage

/** Represents the content of a rich message to be sent as the result of an inline query. */
@Serializable
data class InputRichMessageContent(

    /** The message to be sent */
    @SerialName("rich_message") val richMessage: InputRichMessage,
) : InputMessageContent
