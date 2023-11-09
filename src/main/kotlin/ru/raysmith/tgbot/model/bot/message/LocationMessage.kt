package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.Message
import kotlin.time.Duration

@Suppress("MemberVisibilityCanBePrivate")
class LocationMessage(
    val latitude: Double, val longitude: Double,
    override val client: HttpClient
) : IMessage<Message>, KeyboardCreator {

    var horizontalAccuracy: Double? = null
    var livePeriod: Duration? = null
    var heading: Int? = null
    var proximityAlertRadius: Int? = null
    override var keyboardMarkup: MessageKeyboard? = null

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var protectContent: Boolean? = null

    override fun send(chatId: ChatId) = sendLocation(
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
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

    fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = editMessageLiveLocation(
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

    fun stop(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = stopMessageLiveLocation(
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}