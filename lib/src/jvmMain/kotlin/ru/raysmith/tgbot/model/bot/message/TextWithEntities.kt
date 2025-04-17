package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.utils.withSafeLength

@TextMessageDsl
abstract class TextWithEntities(private val bot: Bot, private val type: MessageTextType) {

    /** Full text of message with entities */
    protected var messageText: MessageText? = null

    /** Simple text to send the message, optionally with [parseMode] */
    open var text: String? = null
    open var parseMode: ParseMode? = null
    open var safeTextLength: Boolean = bot.botConfig.safeTextLength

    /** Sets the text as a [MessageText] object */

    open suspend fun textWithEntities(setText: suspend MessageText.() -> Unit) {
        messageText = MessageText(type, bot.botConfig)
        messageText!!.apply { setText() }
    }

    /** Returns the raw text with safe length for sending the message, empty string if not set */
    protected open fun getMessageText() =
        messageText?.getTextString()
            ?: text?.let { if (parseMode == null && safeTextLength) it.withSafeLength(type) else it }
            ?: ""

    /** Returns the [parseMode] if entities were not used, null otherwise */
    protected open fun getParseModeIfNeed() = if (messageText != null) null else parseMode

    protected open fun getEntities() = messageText?.getEntities()
}