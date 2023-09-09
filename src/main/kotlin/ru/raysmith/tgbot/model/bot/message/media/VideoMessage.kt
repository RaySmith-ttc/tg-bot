package ru.raysmith.tgbot.model.bot.message.media

import kotlinx.serialization.encodeToString
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class VideoMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaMessageWithThumb(), SpolerableContent {

    override var hasSpoiler: Boolean? = null
    var video: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var width: Int? = null
    var height: Int? = null
    var supportsStreaming: Boolean? = null

    override val mediaName: String = "video"

    // TODO test; use same for other sendMedia methods with thumb
    override fun send(chatId: ChatId): MessageResponse = when(video) {
        is InputFile.ByteArray, is InputFile.File -> {
            service.sendVideo(
                chatId = chatId.toRequestBody(),
                messageThreadId = messageThreadId?.toString()?.toRequestBody(),
                video = getMediaMultipartBody(),
                caption = getCaptionText()?.toRequestBody(),
                parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                captionEntities = _caption?.getEntitiesString()?.toRequestBody(),
                hasSpoiler = hasSpoiler?.toString()?.toRequestBody(),
                duration = duration?.toString()?.toRequestBody(),
                width = width?.toString()?.toRequestBody(),
                height = height?.toString()?.toRequestBody(),
                thumbnail = getThumbMultipartBody(),
                supportsStreaming = supportsStreaming?.toString()?.toRequestBody(),
                disableNotification = disableNotification?.toString()?.toRequestBody(),
                protectContent = protectContent?.toString()?.toRequestBody(),
                replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
            ).execute().body() ?: errorBody()
        }
        is InputFile.FileIdOrUrl -> {
            if (thumbnail != null) {
                service.sendVideo(
                    chatId = chatId.toRequestBody(),
                    messageThreadId = messageThreadId?.toString()?.toRequestBody(),
                    video = (video as? InputFile.FileIdOrUrl)?.value?.toRequestBody() ?: error("media is not set"),
                    caption = getCaptionText()?.toRequestBody(),
                    parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                    captionEntities = _caption?.getEntitiesString()?.toRequestBody(),
                    hasSpoiler = hasSpoiler?.toString()?.toRequestBody(),
                    duration = duration?.toString()?.toRequestBody(),
                    width = width?.toString()?.toRequestBody(),
                    height = height?.toString()?.toRequestBody(),
                    thumbnail = getThumbMultipartBody(),
                    supportsStreaming = supportsStreaming?.toString()?.toRequestBody(),
                    disableNotification = disableNotification?.toString()?.toRequestBody(),
                    protectContent = protectContent?.toString()?.toRequestBody(),
                    replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                    allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                    keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
                ).execute().body() ?: errorBody()
            } else {
                service.sendVideo(
                    chatId = chatId,
                    messageThreadId = messageThreadId,
                    video = (media as InputFile.FileIdOrUrl).value,
                    caption = getCaptionText(),
                    parseMode = parseMode,
                    captionEntities = _caption?.getEntitiesString(),
                    hasSpoiler = hasSpoiler,
                    duration = duration,
                    width = width,
                    height = height,
                    supportsStreaming = supportsStreaming,
                    disableNotification = disableNotification,
                    protectContent = protectContent,
                    replyToMessageId = replyToMessageId,
                    allowSendingWithoutReply = allowSendingWithoutReply,
                    keyboardMarkup = keyboardMarkup?.toMarkup()
                ).execute().body() ?: errorBody()
            }
        }
        null -> error("$mediaName is required")
    }
}


