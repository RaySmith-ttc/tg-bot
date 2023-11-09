package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.Message

class VenueMessage(
    val latitude: Double, val longitude: Double, val title: String, val address: String,
    override val client: HttpClient
) : IMessage<Message>, KeyboardCreator {
    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var protectContent: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    var foursquareId: String? = null
    var foursquareType: String? = null
    var googlePlaceId: String? = null
    var googlePlaceType: String? = null

    override fun send(chatId: ChatId) = sendVenue(
        chatId = chatId,
        messageThreadId = messageThreadId,
        latitude = latitude,
        longitude = longitude,
        title = title,
        address = address,
        foursquareId = foursquareId,
        foursquareType = foursquareType,
        googlePlaceId = googlePlaceId,
        googlePlaceType = googlePlaceType,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}