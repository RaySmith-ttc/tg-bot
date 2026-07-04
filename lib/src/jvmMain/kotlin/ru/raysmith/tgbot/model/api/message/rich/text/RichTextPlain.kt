package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.RichTextPlainSerializer

/**
 * A plain text value of a [RichText] field represented as a raw JSON string instead of a [RichText] object.
 *
 * @property value The plain text
 * */
@Serializable(RichTextPlainSerializer::class)
data class RichTextPlain(val value: String) : RichText {

    /** Type of the rich text, always empty since a plain text has no discriminator */
    override val type: String = ""
}
