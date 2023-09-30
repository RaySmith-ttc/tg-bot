package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class StickerMessage(override val client: HttpClient) : MediaMessage() {

    /** Emoji associated with the sticker; only for just uploaded stickers */
    var emoji: String? = null

    /** Sticker to send. */
    var sticker: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "sticker"

    override suspend fun send(chatId: ChatId) = sendSticker(
        chatId = chatId,
        messageThreadId = messageThreadId,
        sticker = media ?: error("$mediaName is required"),
        emoji = emoji,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}