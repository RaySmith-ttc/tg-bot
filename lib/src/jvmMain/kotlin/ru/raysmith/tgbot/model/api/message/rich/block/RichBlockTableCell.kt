package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** Cell in a table. */
@Serializable
data class RichBlockTableCell(

    /** Text in the cell. If omitted, then the cell is invisible. */
    @SerialName("text") val text: RichText? = null,

    /** *True*, if the cell is a header cell */
    @SerialName("is_header") val isHeader: Boolean? = null,

    /** The number of columns the cell spans if it is bigger than 1 */
    @SerialName("colspan") val colspan: Int? = null,

    /** The number of rows the cell spans if it is bigger than 1 */
    @SerialName("rowspan") val rowspan: Int? = null,

    /** Horizontal cell content alignment */
    @SerialName("align") val align: HorizontalAlignment,

    /** Vertical cell content alignment. Currently, must be one of “top”, “middle”, or “bottom”. */
    @SerialName("valign") val valign: VerticalAlignment,
)
