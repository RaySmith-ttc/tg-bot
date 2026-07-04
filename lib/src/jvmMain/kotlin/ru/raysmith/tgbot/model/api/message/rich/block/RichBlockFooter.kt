package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A footer, corresponding to the HTML tag <footer>. */
@Serializable
data class RichBlockFooter(

    /** Text of the block */
    @SerialName("text") val text: RichText,
) : RichBlock {

    /** Type of the block, always “footer” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "footer"
}
