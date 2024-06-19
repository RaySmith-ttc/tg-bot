package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import kotlin.time.Duration

@Suppress("MemberVisibilityCanBePrivate")
class LocationMessage(
    val latitude: Double, val longitude: Double, override val bot: Bot
) : IMessage<Message>, KeyboardCreator, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var horizontalAccuracy: Double? = null
    var livePeriod: Duration? = null
    var heading: Int? = null
    var proximityAlertRadius: Int? = null
    override var keyboardMarkup: MessageKeyboard? = null

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var businessConnectionId: String? = null

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendLocation(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        latitude = latitude,
        longitude = longitude,
        horizontalAccuracy = horizontalAccuracy,
        livePeriod = livePeriod?.inWholeSeconds?.toInt(),
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

    suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = editMessageLiveLocation(
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        latitude = latitude,
        longitude = longitude,
        horizontalAccuracy = horizontalAccuracy,
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

    suspend fun stop(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = stopMessageLiveLocation(
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}