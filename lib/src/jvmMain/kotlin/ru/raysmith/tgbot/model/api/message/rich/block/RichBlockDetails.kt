package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** An expandable block for details disclosure, corresponding to the HTML tag <details>. */
@Serializable
data class RichBlockDetails(

    /** Always shown summary of the block */
    @SerialName("summary") val summary: RichText,

    /** Content of the block */
    @SerialName("blocks") val blocks: List<RichBlock>,

    /** *True*, if the content of the block is visible by default */
    @SerialName("is_open") val isOpen: Boolean? = null,
) : RichBlock {

    /** Type of the block, always “details” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "details"
}
