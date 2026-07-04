package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A preformatted text block, corresponding to the nested HTML tags <pre> and <code>. */
@Serializable
data class RichBlockPreformatted(

    /** Text of the block */
    @SerialName("text") val text: RichText,

    /** The programming language of the text */
    @SerialName("language") val language: String? = null,
) : RichBlock {

    /** Type of the block, always “pre” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "pre"
}

