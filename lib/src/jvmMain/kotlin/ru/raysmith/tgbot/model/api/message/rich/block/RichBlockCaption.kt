package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** Caption of a rich formatted block. */
@Serializable
data class RichBlockCaption(

    /** Block caption */
    @SerialName("text") val text: RichText,

    /** Block credit which corresponds to the HTML tag <cite> */
    @SerialName("credit") val credit: RichText? = null,
)
