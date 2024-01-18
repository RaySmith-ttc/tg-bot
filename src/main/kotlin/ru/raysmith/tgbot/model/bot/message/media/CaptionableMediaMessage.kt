package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.EditableMessage
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.noimpl
import ru.raysmith.tgbot.utils.withSafeLength

abstract class CaptionableMediaMessage : MediaMessage(), IMessage<Message>, KeyboardCreator, EditableMessage {

    companion object {
        internal fun instance(
            service: API, mediaName: String = "",
            send: (chatId: ChatId) -> Message = { noimpl() }
        ): CaptionableMediaMessage {
            return object : CaptionableMediaMessage() {
                override val mediaName: String = mediaName
                override val client: HttpClient = service.client
                override suspend fun send(chatId: ChatId) = send(chatId)

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

