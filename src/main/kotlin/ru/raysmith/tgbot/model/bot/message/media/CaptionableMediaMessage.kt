package ru.raysmith.tgbot.model.bot.message.media

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.message.CaptionableMessage
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.model.network.message.ParseMode
import java.nio.file.Files

abstract class CaptionableMediaMessage : CaptionableMessage(), IMessage<MessageResponse>, KeyboardCreator {

    /** [Parse mode][ParseMode] for a simple caption text */
    var parseMode: ParseMode? = null

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