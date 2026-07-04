package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText
import ru.raysmith.tgbot.network.API

/**
 * A block with a “Thinking…” placeholder, corresponding to the custom HTML tag <tg-thinking>. The block may be used
 * only in [sendRichMessageDraft][API.sendRichMessageDraft], therefore it can't be received in messages. See
 * [https://t.me/addemoji/AIActions](https://t.me/addemoji/AIActions) for examples of custom emoji, which are
 * recommended for usage in the block.
 * */
@Serializable
data class RichBlockThinking(

    /**
     * Text of the block. See [https://t.me/addemoji/AIActions](https://t.me/addemoji/AIActions) for examples of custom
     * emoji, which are recommended for usage in the block.
     * */
    @SerialName("text") val text: RichText,
) : RichBlock {

    /** Type of the block, always “thinking” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "thinking"
}
