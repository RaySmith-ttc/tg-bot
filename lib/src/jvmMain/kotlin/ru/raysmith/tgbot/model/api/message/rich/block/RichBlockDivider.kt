package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.RichBlockDividerSerializer

/** A divider, corresponding to the HTML tag <hr/>. */
@Serializable(RichBlockDividerSerializer::class)
data object RichBlockDivider : RichBlock {

    /** Type of the block, always “divider” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "divider"
}
