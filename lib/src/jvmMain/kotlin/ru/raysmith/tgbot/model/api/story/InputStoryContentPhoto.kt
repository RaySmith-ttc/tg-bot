package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile

/** Describes a photo to post as a story. */
@Serializable(DefaultInputStoryContentPhotoSerializer::class)
data class InputStoryContentPhoto(

    /** The photo to post as a story */
    @SerialName("photo") val photo: NotReusableInputFile,
) : InputStoryContent {

    /** Type of the content, must be *photo* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "photo"
}

internal object DefaultInputStoryContentPhotoSerializer : InputStoryContentPhotoSerializer("attach://photo")

internal open class InputStoryContentPhotoSerializer(val photoValue: String) : KSerializer<InputStoryContentPhoto> {
    override val descriptor = buildClassSerialDescriptor("InputStoryContentPhoto") {
        element<String>("type")
        element<String>("photo")
    }

    override fun serialize(encoder: Encoder, value: InputStoryContentPhoto) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, photoValue)
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputStoryContentPhoto can't be deserialized")
}
