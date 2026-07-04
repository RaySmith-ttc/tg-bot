package ru.raysmith.tgbot.model.api.message.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlock

/** Rich formatted message. */
@Serializable
data class RichMessage(

    /** Content of the message */
    @SerialName("blocks") val blocks: List<RichBlock>,

    /** *True*, if the rich message must be shown right-to-left */
    @SerialName("is_rtl") val isRtl: Boolean? = null,
)
