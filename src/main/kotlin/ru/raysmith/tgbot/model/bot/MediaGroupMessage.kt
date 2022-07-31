package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.media.input.*
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.MessageResponseArray
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.withSafeLength
import java.nio.file.Files

class DocumentMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaGroupMessage(service, fileService) {
    fun document(
        document: InputFile, thumb: NotReusableInputFile, disableContentTypeDetection: Boolean? = null,
        printNulls: Boolean = Bot.Config.printNulls, safeTextLength: Boolean = Bot.Config.safeTextLength,
        caption: MessageText.() -> Unit
    ) {
        inputMedia.add(InputMediaDocument(
            getMedia(document), getMedia(thumb), getCaption(printNulls, caption), null,
            getCaptionEntities(printNulls, safeTextLength, caption), disableContentTypeDetection
        ))
    }

    fun document(
        document: InputFile, thumb: NotReusableInputFile, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, disableContentTypeDetection: Boolean? = null
    ) {
        inputMedia.add(InputMediaDocument(
            getMedia(document), getMedia(thumb), getCaption(caption, false, parseMode), parseMode,
            captionEntities, disableContentTypeDetection
        ))
    }
}

class AudioMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaGroupMessage(service, fileService) {
    fun audio(
        audio: InputFile, thumb: NotReusableInputFile, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, duration: Int? = null, performer: String? = null,
        title: String? = null
    ) {
        inputMedia.add(InputMediaAudio(
            getMedia(audio), getMedia(thumb), getCaption(caption, false, parseMode),
            parseMode, captionEntities, duration, performer, title
        ))
    }

    fun audio(
        audio: InputFile, thumb: NotReusableInputFile, duration: Int? = null, performer: String? = null,
        title: String? = null, printNulls: Boolean = Bot.Config.printNulls,
        safeTextLength: Boolean = Bot.Config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(InputMediaAudio(
            getMedia(audio), getMedia(thumb), getCaption(printNulls, caption), null,
            getCaptionEntities(printNulls, safeTextLength, caption), duration, performer, title
        ))
    }
}

class PhotoMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaGroupMessage(service, fileService) {
    // TODO docs: not correctly work with the safeLength property when parseMode is not null. Provide hand-made safe caption
    fun photo(
        photo: InputFile, caption: String? = null, parseMode: ParseMode? = null,
        safeTextLength: Boolean = Bot.Config.safeTextLength, captionEntities: List<MessageEntity>? = null
    ) {
        inputMedia.add(InputMediaPhoto(
            getMedia(photo), getCaption(caption, safeTextLength, parseMode), parseMode, captionEntities
        ))
    }
    fun photo(
        photo: InputFile, printNulls: Boolean = Bot.Config.printNulls,
        safeTextLength: Boolean = Bot.Config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(InputMediaPhoto(
            getMedia(photo), getCaption(printNulls, caption), null, getCaptionEntities(printNulls, safeTextLength, caption)
        ))
    }
}

abstract class MediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : IMessage<MessageResponseArray> {
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