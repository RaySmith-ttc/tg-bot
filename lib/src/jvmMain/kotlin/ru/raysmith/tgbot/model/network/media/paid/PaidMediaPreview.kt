package ru.raysmith.tgbot.model.network.media.paid

import kotlinx.serialization.Serializable

/** The paid media isn't available before the payment. */
@Serializable
data class PaidMediaPreview(

    /** Media width as defined by the sender */
    val width: Int? = null,

    /** Media height as defined by the sender */
    val height: Int? = null,

    /** Duration of the media in seconds as defined by the sender */
    val duration: Int? = null,
) : PaidMedia() {

    /** Type of the paid media, always “preview” */
    override val type: String = "preview"
}
