package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A bot command. */
@Serializable
data class RichTextBotCommand(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The bot command */
    @SerialName("bot_command") val botCommand: String,
) : RichText {

    /** Type of the rich text, always “bot_command” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "bot_command"
}
