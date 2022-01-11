package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.media.input.InputMediaPhoto
import ru.raysmith.tgbot.model.network.message.MessageResponseArray
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi
import java.io.File
import java.io.InputStream
import java.nio.file.Files

class MediaGroupMessage : IMessage<MessageResponseArray> {
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null

    /** send [ChatAction.UPLOAD_PHOTO] action while upload files to telegram server */
    var sendAction = true

    private val inputMedia = mutableListOf<InputMedia>()
    private val multipartBodyParts = mutableListOf<MultipartBody.Part>()

    fun photo(fileId: String, caption: String? = null, parseMode: ParseMode? = null) {
        inputMedia.add(InputMediaPhoto(media = fileId, caption = caption, parseMode = parseMode))
    }
    fun photo(file: File, caption: String? = null, parseMode: ParseMode? = null) {
        inputMedia.add(InputMediaPhoto(media = "attach://${file.name}", caption = caption, parseMode = parseMode))

        val mimeType = Files.probeContentType(file.toPath())
        val requestBody = file.asRequestBody(mimeType!!.toMediaType())
        multipartBodyParts.add(MultipartBody.Part.createFormData(file.name, file.name, requestBody))
    }
    fun photo(inputStream: InputStream, filename: String, mimeType: String, caption: String? = null, parseMode: ParseMode? = null) {
        inputMedia.add(InputMediaPhoto(media = "attach://$filename", caption = caption, parseMode = parseMode))
        val requestBody = inputStream.readAllBytes().toRequestBody(mimeType.toMediaType())
        multipartBodyParts.add(MultipartBody.Part.createFormData(filename, filename, requestBody))
    }

    override fun send(chatId: String): Response<MessageResponseArray> {
        return when(inputMedia.firstOrNull()) {
            is InputMediaPhoto -> {
                if (multipartBodyParts.isEmpty()) {
                    TelegramApi.service.sendMediaGroup(
                        chatId = chatId,
                        media = TelegramApi.json.encodeToString(inputMedia),
                        disableNotification = disableNotification,
                        replyToMessageId = replyToMessageId,
                        allowSendingWithoutReply = allowSendingWithoutReply,
                    ).execute()
                } else {
                    require(multipartBodyParts.size >= 2) {
                        "Input media collection contains less than 2 items"
                    }

                    if (sendAction) {
                        TelegramApi.service.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO).execute()
                    }

                    TelegramApi.service.sendMediaGroup(
                        chatId = chatId,
                        media = TelegramApi.json.encodeToString(inputMedia),
                        mediaPart1 = multipartBodyParts[0],
                        mediaPart2 = multipartBodyParts[1],
                        mediaPart3 = multipartBodyParts.getOrNull(2),
                        mediaPart4 = multipartBodyParts.getOrNull(3),
                        mediaPart5 = multipartBodyParts.getOrNull(4),
                        mediaPart6 = multipartBodyParts.getOrNull(5),
                        mediaPart7 = multipartBodyParts.getOrNull(6),
                        mediaPart8 = multipartBodyParts.getOrNull(7),
                        mediaPart9 = multipartBodyParts.getOrNull(8),
                        mediaPart10 = multipartBodyParts.getOrNull(9),
                        disableNotification = disableNotification,
                        replyToMessageId = replyToMessageId,
                        allowSendingWithoutReply = allowSendingWithoutReply,
                    ).execute()
                }
            }
            else -> { throw NotImplementedError() }
        }
    }
}