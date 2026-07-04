package ru.raysmith.tgbot.model.api.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.Link
import ru.raysmith.tgbot.model.api.Location
import ru.raysmith.tgbot.model.api.Venue
import ru.raysmith.tgbot.model.api.media.Animation
import ru.raysmith.tgbot.model.api.media.Audio
import ru.raysmith.tgbot.model.api.media.Document
import ru.raysmith.tgbot.model.api.media.LivePhoto
import ru.raysmith.tgbot.model.api.media.PhotoSize
import ru.raysmith.tgbot.model.api.media.Video
import ru.raysmith.tgbot.model.api.sticker.Sticker

/** At most **one** of the optional fields can be present in any given object. */
@Serializable
data class PollMedia(

    /** Media is an animation, information about the animation */
    @SerialName("animation") val animation: Animation? = null,

    /** Media is an audio file, information about the file; currently, can't be received in a poll option */
    @SerialName("audio") val audio: Audio? = null,

    /** Media is a general file, information about the file; currently, can't be received in a poll option */
    @SerialName("document") val document: Document? = null,

    /** The HTTP link attached to the poll option */
    @SerialName("link") val link: Link? = null,

    /** Media is a live photo, information about the live photo */
    @SerialName("live_photo") val livePhoto: LivePhoto? = null,

    /** Media is a shared location, information about the location */
    @SerialName("location") val location: Location? = null,

    /** Media is a photo, available sizes of the photo */
    @SerialName("photo") val photo: List<PhotoSize>? = null,

    /** Media is a sticker, information about the sticker; currently, for poll options only */
    @SerialName("sticker") val sticker: Sticker? = null,

    /** Media is a venue, information about the venue */
    @SerialName("venue") val venue: Venue? = null,

    /** Media is a video, information about the video */
    @SerialName("video") val video: Video? = null,
)
