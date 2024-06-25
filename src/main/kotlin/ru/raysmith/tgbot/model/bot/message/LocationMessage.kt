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

@Suppress("MemberVisibilityCanBePrivate")
class LocationMessage(
    val latitude: Double, val longitude: Double, override val bot: Bot
) : IMessage<Message>, KeyboardCreator, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var horizontalAccuracy: Double? = null

    /**
     * Period during which the location will be updated (see [Live Locations](https://telegram.org/blog/live-locations),
     * should be between `60` and `86400`, or [LivePeriod.Indefinitely] for live locations that can be edited
     * indefinitely.
     * */
    var livePeriod: LivePeriod? = null
    var heading: Int? = null
    var proximityAlertRadius: Int? = null
    override var keyboardMarkup: MessageKeyboard? = null

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendLocation(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        latitude = latitude,
        longitude = longitude,
        horizontalAccuracy = horizontalAccuracy,
        livePeriod = livePeriod,
        heading = heading,
        proximityAlertRadius = proximityAlertRadius,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

    suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = editMessageLiveLocation(
        businessConnectionId = businessConnectionId,
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
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}