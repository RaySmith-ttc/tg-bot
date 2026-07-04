package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A text paragraph, corresponding to the HTML tag <p>. */
@Serializable
data class RichBlockParagraph(

    /** Text of the block */
    @SerialName("text") val text: RichText,
) : RichBlock {

    /** Type of the block, always “paragraph” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "paragraph"
}
