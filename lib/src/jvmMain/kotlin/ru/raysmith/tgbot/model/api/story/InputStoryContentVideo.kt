package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.KSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile

/** Describes a video to post as a story. */
@Serializable(DefaultInputStoryContentVideoSerializer::class)
data class InputStoryContentVideo(

    /**
     * The video to post as a story. The video must be of the size 720x1280, streamable, encoded with H.265 codec
     * with key frames added each second in the MPEG4 format, and must not exceed 30 MB.
     * */
    @SerialName("video") val video: NotReusableInputFile,

    /** Precise duration of the video in seconds; 0-60 */
    @SerialName("duration") val duration: Float? = null,

    /** Timestamp in seconds of the frame that will be used as the static cover for the story. Defaults to 0.0. */
    @SerialName("cover_frame_timestamp") val coverFrameTimestamp: Float? = null,

    /** Pass True if the video has no sound */
    @SerialName("is_animation") val isAnimation: Boolean? = null,
) : InputStoryContent {

    /** Type of the content, must be *video* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "video"
}

internal object DefaultInputStoryContentVideoSerializer : InputStoryContentVideoSerializer("attach://video")

internal open class InputStoryContentVideoSerializer(val photoValue: String) : KSerializer<InputStoryContentVideo> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputStoryContentVideo") {
        element<String>("type")
        element<String>("video")
        element<Float?>("duration", isOptional = true)
        element<Float?>("cover_frame_timestamp", isOptional = true)
        element<Boolean?>("is_animation", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputStoryContentVideo) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, photoValue)
            if (value.duration != null) encodeFloatElement(descriptor, 2, value.duration)
            if (value.coverFrameTimestamp != null) encodeFloatElement(descriptor, 3, value.coverFrameTimestamp)
            if (value.isAnimation != null) encodeBooleanElement(descriptor, 4, value.isAnimation)
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputStoryContentVideo can't be deserialized")

}
