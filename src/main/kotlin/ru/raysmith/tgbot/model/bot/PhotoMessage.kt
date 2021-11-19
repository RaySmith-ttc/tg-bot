package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.network.TelegramApi
import java.io.File
import java.nio.file.Files

class PhotoMessage : IMessage {
    private var photo: String? = null
    private var file: File? = null
    private var fileName: String? = null
    private var mimeType: String? = null
    private var byteArray: ByteArray? = null
    private var _caption: MessageText? = null
    var caption: String? = null
    var parseMode: ParseMode? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    private var keyboardMarkup: MessageKeyboard? = null

    /** Sets a caption as [MessageText] object */
    fun captionWithEntities(setText: MessageText.() -> Unit) {
        _caption = MessageText().apply(setText)
    }

    /**
     * Set a photo to send.
     * @param fileId identifier of telegram [file][ru.raysmith.tgbot.model.network.file.File]
     * */
    fun setPhoto(fileId: String) {
        this.photo = fileId
        this.file = null
        this.byteArray = null
        this.fileName = null
        this.mimeType = null
    }

    /**
     * Set a photo to send.
     * @param file The image file that will be sent
     * */
    fun setPhoto(file: File) {
        this.photo = null
        this.file = file
        this.fileName = file.nameWithoutExtension
        this.mimeType = Files.probeContentType(file.toPath())
        this.byteArray = null
    }

    /**
     * Set a photo to send.
     * @param byteArray [ByteArray] of the image file that will be sent
     * @param fileName the name of the file that be created
     * @param mimeType content type
     * */
    fun setPhoto(byteArray: ByteArray, fileName: String, mimeType: String) {
        this.photo = null
        this.file = null
        this.fileName = fileName
        this.mimeType = mimeType
        this.byteArray = byteArray
    }

    private fun getCaptionText(): String? = _caption?.getTextString() ?: caption

    override fun send(chatId: String): Response<MessageResponse> {
        return when {
            photo != null -> {
                TelegramApi.service.sendPhoto(
                    chatId = chatId,
                    photo = photo!!,
                    caption = getCaptionText(),
                    parseMode = parseMode,
                    captionEntities = _caption?.getEntitiesString(),
                    disableNotification = disableNotification,
                    replyToMessageId = replyToMessageId,
                    allowSendingWithoutReply = allowSendingWithoutReply,
                    keyboardMarkup = keyboardMarkup?.toMarkup()
                ).execute()
            }
            file != null -> {
                val requestBody = file!!.asRequestBody(mimeType!!.toMediaType())
                val photo = MultipartBody.Part.createFormData("photo", fileName!!, requestBody)
                TelegramApi.service.sendPhoto(
                    chatId = chatId.toRequestBody(),
                    photo = photo,
                    caption = getCaptionText()?.toRequestBody(),
                    parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                    disableNotification = disableNotification?.toString()?.toRequestBody(),
                    replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                    allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                    keyboardMarkup = keyboardMarkup?.let { TelegramApi.json.encodeToString(it.toMarkup()) }?.toRequestBody()
                ).execute()
            }
            byteArray != null -> {
                val requestBody = byteArray!!.toRequestBody(mimeType!!.toMediaType())
                val photo = MultipartBody.Part.createFormData("photo", fileName!!, requestBody)
                TelegramApi.service.sendPhoto(
                    chatId = chatId.toRequestBody(),
                    photo = photo,
                    caption = getCaptionText()?.toRequestBody(),
                    parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                    disableNotification = disableNotification?.toString()?.toRequestBody(),
                    replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                    allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                    keyboardMarkup = keyboardMarkup?.let { TelegramApi.json.encodeToString(it.toMarkup()) }?.toRequestBody()
                ).execute()
            }
            else -> throw IllegalArgumentException("Photo or file is required")
        }
    }
}

//TelegramApi.service.sendPhotoMultipart(
//MultipartBody.Builder()
//.setType(MultipartBody.FORM)
//.addFormDataPart("chat_id", chatId)
//.addPart(
//MultipartBody.Part.createFormData("photo", fileName, file!!.asRequestBody("image/*".toMediaType()))
//)
//.apply {
//    getCaptionText()?.also { caption -> addFormDataPart("caption", caption) }
//    parseMode?.also { addFormDataPart("parse_mode", TelegramApi.json.encodeToString(it)) }
//    disableNotification?.also { addFormDataPart("disable_notification", it.toString()) }
//    replyToMessageId?.also { addFormDataPart("reply_to_message_id", it.toString()) }
//    allowSendingWithoutReply?.also { addFormDataPart("allow_sending_without_reply", it.toString()) }
//    keyboardMarkup?.also { addFormDataPart("reply_markup", TelegramApi.json.encodeToString(it)) }
//}
//.build()
//).execute()