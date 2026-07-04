package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters

class VenueMessage(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    override val bot: Bot,
) : KeyboardCreator, BotHolder, IMessage<Message>, ThreadMessage, EffectableMessage, BusinessMessage,
    SuggestablePostMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageThreadId: Int? = null
    override var directMessagesTopicId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var businessConnectionId: String? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var messageEffectId: String? = null
    override var allowPaidBroadcast: Boolean? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    var foursquareId: String? = null
    var foursquareType: String? = null
    var googlePlaceId: String? = null
    var googlePlaceType: String? = null

    override suspend fun send(chatId: ChatId) = sendVenue(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
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
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

}
