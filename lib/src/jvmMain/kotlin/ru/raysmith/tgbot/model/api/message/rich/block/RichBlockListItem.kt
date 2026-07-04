package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An item of a list. */
@Serializable
data class RichBlockListItem(

    /** Label of the item */
    @SerialName("label") val label: String,

    /** The content of the item */
    @SerialName("blocks") val blocks: List<RichBlock>,

    /** *True*, if the item has a checkbox */
    @SerialName("has_checkbox") val hasCheckbox: Boolean? = null,

    /** *True*, if the item has a checked checkbox */
    @SerialName("is_checked") val isChecked: Boolean? = null,

    /** For ordered lists, the numeric value of the item label */
    @SerialName("value") val value: Int? = null,

    /** For ordered lists, the type of the item label */
    @SerialName("type") val type: RichBlockListItemLabelType? = null,
)
