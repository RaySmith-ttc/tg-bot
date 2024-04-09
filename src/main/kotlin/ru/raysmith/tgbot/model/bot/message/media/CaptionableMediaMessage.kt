package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.EditableMessage
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.utils.noimpl
import ru.raysmith.tgbot.utils.withSafeLength

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
                override suspend fun send(chatId: ChatId, messageThreadId: Int?) = send(chatId, messageThreadId)
                override val bot: Bot = bot
                override val client: HttpClient = bot.client
                override val botConfig: BotConfig = bot.botConfig

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
    var safeTextLength: Boolean = bot.botConfig.safeTextLength

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

    override suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageCaption(
            chatId, messageId, inlineMessageId, getCaptionText() ?: "", parseMode = null,
            _caption?.getEntitiesString(), keyboardMarkup?.toMarkup()
        )
    }

    suspend fun <T : InputMedia> editMedia(chatId: ChatId?, messageId: Int?, inlineMessageId: String?, media: InputMedia): Message {
        return editMessageMedia(
            chatId, messageId, inlineMessageId, media, keyboardMarkup?.toMarkup()
        )
    }

    override suspend fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageReplyMarkup(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        )
    }
}

