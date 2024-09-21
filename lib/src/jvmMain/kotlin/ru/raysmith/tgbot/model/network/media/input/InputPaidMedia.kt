package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the paid media to be sent. Currently, it can be one of
 *
 * - [InputPaidMediaPhoto]
 * - [InputPaidMediaVideo]
 * */
@Serializable
sealed class InputPaidMedia : InputMediaOrInputPaidMedia {

    /** Type of the media */
    abstract val type: String // TODO should be encoded by default in all of other sealed classes
}

/** The paid media to send is a photo. */
@Serializable
data class InputPaidMediaPhoto(

    /** File to send */
    @SerialName("media") val media: String
) : InputPaidMedia() {

    /** Type of the media, must be *photo* */
    @EncodeDefault
    @SerialName("type") override val type: String = "photo"
}

/** The paid media to send is a video. */
@Serializable
data class InputPaidMediaVideo(

    /** File to send */
    @SerialName("media") val media: String,

    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size.
     * A thumbnail's width and height should not exceed 320. Thumbnails can't be reused and can be only uploaded as a
     * new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using
     * multipart/form-data under <file_attach_name>.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    @SerialName("thumbnail") val thumbnail: String? = null,

    /** Video width */
    @SerialName("width") val width: Int? = null,

    /** Video height */
    @SerialName("height") val height: Int? = null,

    /** Video duration */
    @SerialName("duration") val duration: Int? = null,

    /** Pass *True* if the uploaded video is suitable for streaming */
    @SerialName("supports_streaming") val supportsStreaming: Boolean? = null,
) : InputPaidMedia() {

    /** Type of the media, must be *video* */
    @EncodeDefault
    @SerialName("type") override val type: String = "video"
}