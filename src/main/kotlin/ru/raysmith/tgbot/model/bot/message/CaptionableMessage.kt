package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.utils.withSafeLength

abstract class CaptionableMessage : EditableMessage {
    protected var _caption: MessageText? = null

    /** Simple caption text to send the message */
    var caption: String? = null

    fun hasCaption() = caption != null || _caption != null

    /** Whether test should be truncated if caption length is greater than 1024 */
    var safeTextLength: Boolean = Bot.Config.safeTextLength

    /**
     * Sets a caption as [MessageText] object
     * */
    fun captionWithEntities(setText: MessageText.() -> Unit) {
        _caption = MessageText(MessageTextType.CAPTION).apply(setText)
    }

    fun getCaptionText(): String? =
        _caption?.getTextString()
            ?: caption?.let { if (safeTextLength) it.withSafeLength(MessageTextType.CAPTION) else it }
}