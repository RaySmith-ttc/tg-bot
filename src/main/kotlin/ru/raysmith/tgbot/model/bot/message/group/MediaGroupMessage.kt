package ru.raysmith.tgbot.model.bot.message.group

import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.network.response.MessageResponseArray
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.withSafeLength
import java.nio.file.Files

abstract class MediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) :
    IMessage<MessageResponseArray> {
    override var disableNotification: Boolean? = null
    override var protectContent: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null

    /** send [ChatAction.UPLOAD_PHOTO] action while upload files to telegram server */
    var sendAction = true

    protected val inputMedia = mutableListOf<InputMedia>()
    private val multipartBodyParts = mutableListOf<MultipartBody.Part>()

    protected fun append(byteArray: ByteArray, filename: String, mimeType: String): String {
        val requestBody = byteArray.toRequestBody(mimeType.toMediaType())
        multipartBodyParts.add(MultipartBody.Part.createFormData(filename, filename, requestBody))
        return "attach://$filename"
    }

    protected fun getMedia(media: InputFile) = when (media) {
        is InputFile.FileIdOrUrl -> media.value
        is InputFile.ByteArray -> append(media.byteArray, media.filename, media.mimeType)
        is InputFile.File -> append(
            media.file.readBytes(),
            media.file.nameWithoutExtension,
            Files.probeContentType(media.file.toPath())
        )
    }

    protected fun getMedia(media: NotReusableInputFile) = when (media) {
        is InputFile.ByteArray -> append(media.byteArray, media.filename, media.mimeType)
        is InputFile.File -> append(
            media.file.readBytes(),
            media.file.nameWithoutExtension,
            Files.probeContentType(media.file.toPath())
        )

        else -> error("ReusableMedia can be only a file or byte array")
    }


    protected fun getCaption(caption: String?, safeTextLength: Boolean, parseMode: ParseMode?) = when {
        safeTextLength && parseMode == null -> caption?.withSafeLength(MessageTextType.CAPTION)
        safeTextLength -> caption
        else -> caption
    }

    protected fun getCaption(printNulls: Boolean, caption: MessageText.() -> Unit) =
        MessageText(MessageTextType.CAPTION)
            .apply { this.printNulls = printNulls }
            .apply(caption).getTextString()

    protected fun getCaptionEntities(printNulls: Boolean, safeTextLength: Boolean, caption: MessageText.() -> Unit) =
        MessageText(MessageTextType.CAPTION)
            .apply {
                this.printNulls = printNulls
                this.safeTextLength = safeTextLength
            }
            .apply(caption).getEntities()

    override fun send(chatId: ChatId): MessageResponseArray {
        return if (multipartBodyParts.isEmpty()) {
            service.sendMediaGroup(
                chatId = chatId,
                media = TelegramApi.json.encodeToString(inputMedia.toList()),
                disableNotification = disableNotification,
                replyToMessageId = replyToMessageId,
                allowSendingWithoutReply = allowSendingWithoutReply,
            ).execute().body() ?: errorBody()
        } else {
            require(multipartBodyParts.size >= 2) {
                "Input media collection contains less than 2 items"
            }

            if (sendAction) {
                val action = when (this) {
                    is PhotoMediaGroupMessage -> ChatAction.UPLOAD_PHOTO
                    is DocumentMediaGroupMessage -> ChatAction.UPLOAD_DOCUMENT
                    else -> null
                }

                if (action != null) {
                    service.sendChatAction(chatId, action).execute()
                }
            }

            service.sendMediaGroup(
                chatId = chatId,
                media = TelegramApi.json.encodeToString(inputMedia.toList()),
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
            ).execute().body() ?: errorBody()
        }
    }
}