package ru.raysmith.tgbot.model.bot

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.response.MessageEditResponse
import ru.raysmith.tgbot.model.network.message.response.MessageSendResponse
import ru.raysmith.tgbot.network.TelegramApi
import java.io.File

class PhotoMessage : IMessage {
    var photo: String? = null
    var file: File? = null
    private var _caption: MessageText? = null
    var caption: String? = null
    var parseMode: ParseMode? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    private var keyboardMarkup: MessageKeyboard? = null

    /** Sets caption as [MessageText] object */
    fun captionWithEntities(setText: MessageText.() -> Unit) {
        _caption = MessageText().apply(setText)
    }

    private fun getCaptionText(): String? = _caption?.getTextString() ?: caption

    override fun send(chatId: String): Response<MessageSendResponse> {
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
                    allowSendingWithoutReply = allowSendingWithoutReply
                ).execute()
            }
            file != null -> {
                TelegramApi.service.sendPhotoPost(
                    chatId = chatId,
                    photo = MultipartBody.Part.createFormData("photo", file!!.nameWithoutExtension, file!!.asRequestBody("image/*".toMediaType())), //file!!.asRequestBody("image/*".toMediaType()),
                    caption = getCaptionText(),
                    parseMode = parseMode,
                    disableNotification = disableNotification,
                    replyToMessageId = replyToMessageId,
                    allowSendingWithoutReply = allowSendingWithoutReply
                ).execute()
            }
            else -> throw IllegalArgumentException("Photo or file is required")
        }
    }
}