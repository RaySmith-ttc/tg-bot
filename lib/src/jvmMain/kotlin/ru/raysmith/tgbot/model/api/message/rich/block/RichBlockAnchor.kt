package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A block with an anchor, corresponding to the HTML tag <a> with the attribute name. */
@Serializable
data class RichBlockAnchor(

    /** The name of the anchor */
    @SerialName("name") val name: String,
) : RichBlock {

    /** Type of the block, always “anchor” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "anchor"
}
