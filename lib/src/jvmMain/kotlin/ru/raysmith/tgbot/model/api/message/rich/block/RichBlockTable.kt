package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A table, corresponding to the HTML tag <table>. */
@Serializable
data class RichBlockTable(

    /** Cells of the table */
    @SerialName("cells") val cells: List<List<RichBlockTableCell>>,

    /** *True*, if the table has borders */
    @SerialName("is_bordered") val isBordered: Boolean? = null,

    /** *True*, if the table is striped */
    @SerialName("is_striped") val isStriped: Boolean? = null,

    /** Caption of the table */
    @SerialName("caption") val caption: RichText? = null,
) : RichBlock {

    /** Type of the block, always “table” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "table"
}
