package ru.raysmith.tgbot.model.bot.message.media

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.core.ApiCaller
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.CaptionableMessage
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.media.input.InputMediaPhoto
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.model.network.media.input.toRequestBody
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.noimpl
import java.nio.file.Files

abstract class CaptionableMediaMessage : CaptionableMessage(), IMessage<MessageResponse>, KeyboardCreator {

    companion object {
        internal fun instance(
            apiCaller: ApiCaller, mediaName: String = "",
            send: (chatId: ChatId) -> MessageResponse = { noimpl() }
        ): CaptionableMediaMessage {
            return object : CaptionableMediaMessage() {
                override val mediaName: String = mediaName
                override val service: TelegramService = apiCaller.service
                override val fileService: TelegramFileService = apiCaller.fileService
                override fun send(chatId: ChatId) = send(chatId)
                override fun editReplyMarkup(
                    chatId: ChatId?, messageId: Int?, inlineMessageId: String?
                ): MessageResponse = noimpl()
            }
        }
    }

    /** [Parse mode][ParseMode] for a simple caption text */
    var parseMode: ParseMode? = null

    protected var media: InputFile? = null
    protected abstract val mediaName: String

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

    override fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): MessageResponse {
        return service.editMessageCaption(
            chatId, messageId, inlineMessageId, getCaptionText() ?: "", parseMode = null,
            _caption?.getEntitiesString(), keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }

    fun <T : InputMedia> editMedia(chatId: ChatId?, messageId: Int?, inlineMessageId: String?, media: InputMedia): MessageResponse {
        return service.editMessageMedia(
            chatId, messageId, inlineMessageId, media, keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }

    override fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): MessageResponse {
        return service.editMessageReplyMarkup(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }
}

// TODO add auto ChatAction + global config
abstract class MediaMessage : IMessage<MessageResponse>, KeyboardCreator {

    protected var media: InputFile? = null
    protected abstract val mediaName: String

    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null

    protected fun getMediaMultipartBody() = when(media) {
        is InputFile.ByteArray -> {
            val media = media as InputFile.ByteArray
            val body = media.byteArray.toRequestBody(media.mimeType.toMediaType())
            MultipartBody.Part.createFormData(mediaName, media.filename, body)
        }
        is InputFile.File -> {
            val media = media as InputFile.File
            val mimeType = Files.probeContentType(media.file.toPath()).toMediaType()
            val body = media.file.readBytes().toRequestBody(mimeType)
            MultipartBody.Part.createFormData(mediaName, media.file.name, body)
        }
        else -> error("Only ByteArray and File can be multipart")
    }
}