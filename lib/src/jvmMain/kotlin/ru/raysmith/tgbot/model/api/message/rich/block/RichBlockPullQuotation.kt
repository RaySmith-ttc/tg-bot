package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A quotation with centered text, loosely corresponding to the HTML tag <aside>. */
@Serializable
data class RichBlockPullQuotation(

    /** Text of the block */
    @SerialName("text") val text: RichText,

    /** Credit of the block */
    @SerialName("credit") val credit: RichText? = null,
) : RichBlock {

    /** Type of the block, always “pullquote” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "pullquote"
}
