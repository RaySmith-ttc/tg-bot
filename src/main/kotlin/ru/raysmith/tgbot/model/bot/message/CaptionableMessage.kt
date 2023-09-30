package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.utils.withSafeLength

abstract class CaptionableMessage : EditableMessage {
    protected var _caption: MessageText? = null

    /** Simple caption text to send the message */
    var caption: String? = null

    fun hasCaption() = caption != null || _caption != null

    /** Whether test should be truncated if caption length is greater than 1024 */
    var safeTextLength: Boolean = Bot.config.safeTextLength

    /**
     * Sets a caption as [MessageText] object
     * */
    suspend fun captionWithEntities(setText: suspend MessageText.() -> Unit) {
        _caption = MessageText(MessageTextType.CAPTION).apply { setText() }
    }

    fun getCaptionText(): String? =
        _caption?.getTextString()
            ?: caption?.let { if (safeTextLength) it.withSafeLength(MessageTextType.CAPTION) else it }
}