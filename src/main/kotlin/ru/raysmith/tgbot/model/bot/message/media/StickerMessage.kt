package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class StickerMessage(override val bot: Bot) : MediaMessage(), BotHolder {
    override val client: HttpClient = bot.client

    /** Emoji associated with the sticker; only for just uploaded stickers */
    var emoji: String? = null

    /** Sticker to send. */
    var sticker: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "sticker"
    override var sendChatAction: Boolean = bot.config.sendChatActionWithMedaMessage

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendSticker(
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