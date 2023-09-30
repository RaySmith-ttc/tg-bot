package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import okhttp3.MultipartBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.CaptionableMessage
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.media.input.toRequestBody
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramService2
import ru.raysmith.tgbot.utils.noimpl

abstract class CaptionableMediaMessage : CaptionableMessage(), IMessage<Message>, KeyboardCreator {

    companion object {
        internal fun instance(
            service: TelegramService2, mediaName: String = "",
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

    /** [Parse mode][ParseMode] for a simple caption text */
    var parseMode: ParseMode? = null

    protected var media: InputFile? = null
    protected abstract val mediaName: String

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null

    protected fun getMediaMultipartBody(): MultipartBody.Part {
        if (media is InputFile.FileIdOrUrl) {
            error("Only ByteArray and File can be multipart")
        }

        return media?.toRequestBody(mediaName) ?: error("media is not set")
    }

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

