package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.RichTextListSerializer

/**
 * A list of [RichText] values represented as a raw JSON array instead of a single [RichText] object.
 *
 * @property items The rich text values
 * */
@Serializable(RichTextListSerializer::class)
data class RichTextList(val items: List<RichText>) : RichText {

    /** Type of the rich text, always empty since a list has no discriminator */
    override val type: String = ""
}
