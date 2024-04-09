package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.Message

class ContactMessage(
    val phoneNumber: String, val firstName: String,
    override val bot: Bot
) : IMessage<Message>, KeyboardCreator, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var protectContent: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    var lastName: String? = null
    var vcard: String? = null

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendContact(
        chatId = chatId,
        messageThreadId = messageThreadId,
        phoneNumber = phoneNumber,
        firstName = firstName,
        lastName = lastName,
        vcard = vcard,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}

