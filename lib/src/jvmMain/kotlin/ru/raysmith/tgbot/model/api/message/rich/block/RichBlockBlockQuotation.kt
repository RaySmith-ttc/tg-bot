package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A block quotation, corresponding to the HTML tag <blockquote>. */
@Serializable
data class RichBlockBlockQuotation(

    /** Content of the block */
    @SerialName("blocks") val blocks: List<RichBlock>,

    /** Credit of the block */
    @SerialName("credit") val credit: RichText? = null,
) : RichBlock {

    /** Type of the block, always “blockquote” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "blockquote"
}
