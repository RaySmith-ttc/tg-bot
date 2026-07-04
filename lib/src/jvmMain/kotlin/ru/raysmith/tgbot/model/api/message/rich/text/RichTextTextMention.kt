package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** A mention of a Telegram user by their identifier. */
@Serializable
data class RichTextTextMention(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The mentioned user */
    @SerialName("user") val user: User,
) : RichText {

    /** Type of the rich text, always “text_mention” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "text_mention"
}

