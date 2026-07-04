package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.BusinessMessage
import ru.raysmith.tgbot.model.bot.message.EffectableMessage
import ru.raysmith.tgbot.model.bot.message.ThreadMessage
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.message.SuggestablePostMessage

class StickerMessage(override val bot: Bot) : MediaMessage(), BotHolder, ThreadMessage, EffectableMessage,
    BusinessMessage, SuggestablePostMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    /** Emoji associated with the sticker; only for just uploaded stickers */
    var emoji: String? = null

    /** Sticker to send. */
    var sticker: InputFile?
        get() = media
        set(value) {
            media = value
        }

    override val mediaName: String = "sticker"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var messageThreadId: Int? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    override suspend fun send(chatId: ChatId) = sendSticker(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        sticker = media ?: error("$mediaName is required"),
        emoji = emoji,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}
