package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A list of blocks, corresponding to the HTML tag <ul> or <ol> with multiple nested tags <li>. */
@Serializable
data class RichBlockList(

    /** Items of the list */
    @SerialName("items") val items: List<RichBlockListItem>,
) : RichBlock {

    /** Type of the block, always “list” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "list"
}
