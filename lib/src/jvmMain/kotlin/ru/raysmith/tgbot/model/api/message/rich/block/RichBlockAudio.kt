package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.Audio
import ru.raysmith.tgbot.model.serializer.RichBlockAudioSerializer

/** A block with a music file, corresponding to the HTML tag <audio>. */
@Serializable
data class RichBlockAudio(

    /** The audio(s) */
    @Serializable(RichBlockAudioSerializer::class)
    @SerialName("audio") val audio: List<Audio>,

    /** Optional. Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “audio” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "audio"
}
