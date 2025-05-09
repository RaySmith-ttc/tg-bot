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

class ContactMessage(
    val phoneNumber: String, val firstName: String,
    override val bot: Bot
) : KeyboardCreator, BotHolder, ExtendedMessage<Message> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var protectContent: Boolean? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var replyParameters: ReplyParameters? = null
    override var allowPaidBroadcast: Boolean? = null

    var lastName: String? = null
    var vcard: String? = null

    override suspend fun send(chatId: ChatId) = sendContact(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        phoneNumber = phoneNumber,
        firstName = firstName,
        lastName = lastName,
        vcard = vcard,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}

