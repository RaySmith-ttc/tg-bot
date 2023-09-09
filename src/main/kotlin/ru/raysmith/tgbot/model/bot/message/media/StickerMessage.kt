package ru.raysmith.tgbot.model.bot.message.media

import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class StickerMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaMessage() {

    /** Emoji associated with the sticker; only for just uploaded stickers */
    var emoji: String? = null

    /** Sticker to send. */
    var sticker: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "sticker"

    override fun send(chatId: ChatId): MessageResponse = when(sticker) {
        is InputFile.ByteArray, is InputFile.File -> {
            service.sendSticker(
                chatId = chatId.toRequestBody(),
                messageThreadId = messageThreadId?.toString()?.toRequestBody(),
                sticker = getMediaMultipartBody(),
                emoji = emoji?.toRequestBody(),
                disableNotification = disableNotification?.toString()?.toRequestBody(),
                protectContent = protectContent?.toString()?.toRequestBody(),
                replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
            ).execute().body() ?: errorBody()
        }
        is InputFile.FileIdOrUrl -> {
            service.sendSticker(
                chatId = chatId,
                messageThreadId = messageThreadId,
                sticker = (media as InputFile.FileIdOrUrl).value,
                emoji = emoji,
                disableNotification = disableNotification,
                protectContent = protectContent,
                replyToMessageId = replyToMessageId,
                allowSendingWithoutReply = allowSendingWithoutReply,
                keyboardMarkup = keyboardMarkup?.toMarkup()
            ).execute().body() ?: errorBody()
        }
        null -> error("$mediaName is required")
    }
}