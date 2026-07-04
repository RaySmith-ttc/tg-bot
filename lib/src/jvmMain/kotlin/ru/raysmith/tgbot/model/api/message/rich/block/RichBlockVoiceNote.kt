package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.Voice
import ru.raysmith.tgbot.model.serializer.RichBlockVoiceNoteSerializer

/** A block with a voice note, corresponding to the HTML tag <audio>. */
@Serializable
data class RichBlockVoiceNote(

    /** The voice note(s) */
    @Serializable(RichBlockVoiceNoteSerializer::class)
    @SerialName("voice_note") val voiceNote: List<Voice>,

    /** Optional. Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “voice_note” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "voice_note"
}
