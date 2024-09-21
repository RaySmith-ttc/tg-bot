package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.*
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.utils.noimpl
import ru.raysmith.tgbot.utils.withSafeLength

abstract class ExtendedCaptionableMediaMessage : CaptionableMediaMessage(), ExtendedMessage<Message> {
    companion object {
        internal fun instance(
            bot: Bot, mediaName: String = "",
            send: (chatId: ChatId, messageThreadId: Int?) -> Message = { _, _ -> noimpl() }
        ): ExtendedCaptionableMediaMessage {
            return object : ExtendedCaptionableMediaMessage() {
                override var sendChatAction: Boolean = false
                override val mediaName: String = mediaName
                override suspend fun send(chatId: ChatId) = send(chatId, null)
                override val bot: Bot = bot
                override var messageThreadId: Int? = null
                override var messageEffectId: String? = null
                override var businessConnectionId: String? = null
                override val client: HttpClient = bot.client
                override val botConfig: BotConfig = bot.botConfig
                override var replyParameters: ReplyParameters? = null
                override var safeTextLength: Boolean = bot.botConfig.safeTextLength

                override suspend fun editReplyMarkup(
                    chatId: ChatId?, messageId: Int?, inlineMessageId: String?
                ): Message = noimpl()
            }
        }
    }

    override suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageCaption(
            businessConnectionId, chatId, messageId, inlineMessageId, getCaptionText() ?: "", parseMode = null,
            _caption?.getEntities(), showCaptionAboveMedia, keyboardMarkup?.toMarkup()
        )
    }

    override suspend fun editMedia(media: InputMedia, chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageMedia(
            businessConnectionId, chatId, messageId, inlineMessageId, media, keyboardMarkup?.toMarkup()
        )
    }

    override suspend fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageReplyMarkup(
            businessConnectionId = businessConnectionId,
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        )
    }
}

abstract class CaptionableMediaMessage :
    MediaMessage(), IMessage<Message>, KeyboardCreator, EditableMessage, BotHolder {

    companion object {
        internal fun instance(
            bot: Bot, mediaName: String = "",
            send: (chatId: ChatId, messageThreadId: Int?) -> Message = { _, _ -> noimpl() }
        ): CaptionableMediaMessage {
            return object : CaptionableMediaMessage() {
                override var sendChatAction: Boolean = false
                override val mediaName: String = mediaName
                override suspend fun send(chatId: ChatId) = send(chatId, null)
                override val bot: Bot = bot
                override val client: HttpClient = bot.client
                override val botConfig: BotConfig = bot.botConfig
                override var replyParameters: ReplyParameters? = null
                override var safeTextLength: Boolean = bot.botConfig.safeTextLength

                override suspend fun editReplyMarkup(
                    chatId: ChatId?, messageId: Int?, inlineMessageId: String?
                ): Message = noimpl()
            }
        }
    }

    protected var _caption: MessageText? = null

    /** Simple caption text to send the message */
    var caption: String? = null

    fun hasCaption() = caption != null || _caption != null

    /** Whether test should be truncated if caption length is greater than 1024 */
    abstract var safeTextLength: Boolean

    /**
     * Sets a caption as [MessageText] object
     * */
    suspend fun captionWithEntities(setText: suspend MessageText.() -> Unit) {
        _caption = MessageText(MessageTextType.CAPTION, bot.botConfig).apply { setText() }
    }

    fun getCaptionText(): String? =
        _caption?.getTextString()
            ?: caption?.let { if (safeTextLength) it.withSafeLength(MessageTextType.CAPTION) else it }

    /** [Parse mode][ParseMode] for a simple caption text */
    var parseMode: ParseMode? = null

    /** Returns the [parseMode] if entities were not used, null otherwise */
    protected open fun getParseModeIfNeed() = if (_caption != null) null else parseMode

    protected open fun getEntities() = _caption?.getEntities()

    override suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageCaption(
            null, chatId, messageId, inlineMessageId, getCaptionText() ?: "", parseMode = null,
            _caption?.getEntities(), showCaptionAboveMedia, keyboardMarkup?.toMarkup()
        )
    }

    open suspend fun editMedia(media: InputMedia, chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageMedia(
            null, chatId, messageId, inlineMessageId, media, keyboardMarkup?.toMarkup()
        )
    }

    override suspend fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageReplyMarkup(
            businessConnectionId = null,
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        )
    }
}

