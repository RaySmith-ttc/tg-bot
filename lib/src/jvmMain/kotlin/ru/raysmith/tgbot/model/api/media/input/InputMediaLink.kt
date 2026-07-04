package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents an HTTP link to be sent. */
@Serializable
data class InputMediaLink(

    /** HTTP URL of the link */
    @SerialName("url") val url: String,
) : InputPollOptionMedia {

    /** Type of the media, must be *link* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "link"
}
