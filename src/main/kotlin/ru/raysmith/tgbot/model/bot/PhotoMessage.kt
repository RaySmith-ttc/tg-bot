package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.getOrDefault
import ru.raysmith.tgbot.utils.withSafeLength
import java.io.File
import java.nio.file.Files

class PhotoMessage(override val service: TelegramService = TelegramApi.service
) : IMessage<MessageResponse>, KeyboardCreator {

    private var photo: String? = null
    private var file: File? = null
    private var fileName: String? = null
    private var mimeType: String? = null
    private var byteArray: ByteArray? = null
    private var _caption: MessageText? = null

    /** Simple caption text to send the message, optionally with [parseMode] */
    var caption: String? = null

    /** [Parse mode][ParseMode] for a simple caption text */
    var parseMode: ParseMode? = null

    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    /** Whether test should be truncated if caption length is greater than 4096 */
    var safeTextLength: Boolean = Bot.properties.getOrDefault("safeTextLength", "true").toBoolean()

    /**
     * Sets a caption as [MessageText] object
     *
     * @param printNulls Set true for apply null strings to message text
     * */
    fun captionWithEntities(printNulls: Boolean = false, setText: MessageText.() -> Unit) {
        _caption = MessageText(printNulls).apply(setText)
    }

    /**
     * Set a photo to send.
     * @param fileId identifier of telegram [file][ru.raysmith.tgbot.model.network.file.File]
     * */
    fun photo(fileId: String) {
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
    fun photo(file: File) {
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
    fun photo(byteArray: ByteArray, fileName: String, mimeType: String) {
        this.photo = null
        this.file = null
        this.fileName = fileName
        this.mimeType = mimeType
        this.byteArray = byteArray
    }

    private fun getCaptionText(): String? =
        _caption?.let { if (safeTextLength) it.getSafeTextString() else it.getTextString() }
            ?: caption?.let { if (safeTextLength) it.withSafeLength() else it }

    override fun send(chatId: String): MessageResponse {
        return when {
            photo != null -> {
                service.sendPhoto(
                    chatId = chatId,
                    photo = photo!!,
                    caption = getCaptionText(),
                    parseMode = parseMode,
                    captionEntities = _caption?.getEntitiesString(safeTextLength),
                    disableNotification = disableNotification,
                    replyToMessageId = replyToMessageId,
                    allowSendingWithoutReply = allowSendingWithoutReply,
                    keyboardMarkup = keyboardMarkup?.toMarkup()
                ).execute().body() ?: errorBody()
            }
            file != null -> {
                val requestBody = file!!.asRequestBody(mimeType!!.toMediaType())
                val photo = MultipartBody.Part.createFormData("photo", fileName!!, requestBody)
                service.sendPhoto(
                    chatId = chatId.toRequestBody(),
                    photo = photo,
                    caption = getCaptionText()?.toRequestBody(),
                    parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                    captionEntities = _caption?.getEntitiesString(safeTextLength)?.toRequestBody(),
                    disableNotification = disableNotification?.toString()?.toRequestBody(),
                    replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                    allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                    keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
                ).execute().body() ?: errorBody()
            }
            byteArray != null -> {
                val requestBody = byteArray!!.toRequestBody(mimeType!!.toMediaType())
                val photo = MultipartBody.Part.createFormData("photo", fileName!!, requestBody)
                service.sendPhoto(
                    chatId = chatId.toRequestBody(),
                    photo = photo,
                    caption = getCaptionText()?.toRequestBody(),
                    parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                    captionEntities = _caption?.getEntitiesString(safeTextLength)?.toRequestBody(),
                    disableNotification = disableNotification?.toString()?.toRequestBody(),
                    replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                    allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                    keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
                ).execute().body() ?: errorBody()
            }
            else -> throw IllegalArgumentException("Photo or file is required")
        }
    }
}