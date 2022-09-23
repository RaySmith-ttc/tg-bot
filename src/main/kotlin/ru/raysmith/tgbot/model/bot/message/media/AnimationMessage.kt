package ru.raysmith.tgbot.model.bot.message.media

import kotlinx.serialization.encodeToString
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class AnimationMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaMessageWithThumb() {

    var animation: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var width: Int? = null
    var height: Int? = null

    override val mediaName: String = "animation"

    override fun send(chatId: ChatId): MessageResponse {
        return service.sendAnimation(
            chatId = chatId.toRequestBody(),
            animation = if (media is InputFile.FileIdOrUrl) MultipartBody.Part.create((media as InputFile.FileIdOrUrl).value.toRequestBody()) else getMediaMultipartBody(),
            caption = getCaptionText()?.toRequestBody(),
            parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
            captionEntities = _caption?.getEntitiesString()?.toRequestBody(),
            duration = duration?.toString()?.toRequestBody(),
            width = width?.toString()?.toRequestBody(),
            height = height?.toString()?.toRequestBody(),
            thumb = getThumbMultipartBody(),
            disableNotification = disableNotification?.toString()?.toRequestBody(),
            protectContent = protectContent?.toString()?.toRequestBody(),
            replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
            allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
            keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
        ).execute().body() ?: errorBody()
    }
//    override fun send(chatId: ChatId): MessageResponse = when(animation) {
//        is InputFile.ByteArray, is InputFile.File -> {
//            service.sendAnimation(
//                chatId = chatId.toRequestBody(),
//                animation = getMediaMultipartBody(),
//                caption = getCaptionText()?.toRequestBody(),
//                parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
//                captionEntities = _caption?.getEntitiesString()?.toRequestBody(),
//                duration = duration?.toString()?.toRequestBody(),
//                width = width?.toString()?.toRequestBody(),
//                height = height?.toString()?.toRequestBody(),
//                thumb = getThumbMultipartBody(),
//                disableNotification = disableNotification?.toString()?.toRequestBody(),
//                protectContent = protectContent?.toString()?.toRequestBody(),
//                replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
//                allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
//                keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
//            ).execute().body() ?: errorBody()
//        }
//        is InputFile.FileIdOrUrl -> {
//            service.sendAnimation(
//                chatId = chatId,
//                animation = (media as InputFile.FileIdOrUrl).value,
//                caption = getCaptionText(),
//                parseMode = parseMode,
//                captionEntities = _caption?.getEntitiesString(),
//                duration = duration,
//                width = width,
//                height = height,
//                disableNotification = disableNotification,
//                protectContent = protectContent,
//                replyToMessageId = replyToMessageId,
//                allowSendingWithoutReply = allowSendingWithoutReply,
//                keyboardMarkup = keyboardMarkup?.toMarkup()
//            ).execute().body() ?: errorBody()
//        }
//        null -> error("$mediaName is required")
//    }
}