package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import kotlin.time.Duration

class LocationMessage(
    val latitude: Double, val longitude: Double,
    override val service: TelegramService,
    override val fileService: TelegramFileService
) : IMessage<MessageResponse>, //EditableMessage,
    KeyboardCreator {

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

    override fun send(chatId: ChatId): MessageResponse {
        return service.sendLocation(
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
        ).execute().body() ?: errorBody()
    }

    fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): LiveLocationResponse {
        return service.editMessageLiveLocation(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            latitude = latitude,
            longitude = longitude,
            horizontalAccuracy = horizontalAccuracy,
            heading = heading,
            proximityAlertRadius = proximityAlertRadius,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }

    fun stop(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): LiveLocationResponse {
        return service.stopMessageLiveLocation(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }
}