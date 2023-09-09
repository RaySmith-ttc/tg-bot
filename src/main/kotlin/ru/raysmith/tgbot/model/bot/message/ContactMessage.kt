package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class ContactMessage(
    val phoneNumber: String, val firstName: String,
    override val service: TelegramService,
    override val fileService: TelegramFileService
) : IMessage<MessageResponse>, KeyboardCreator {

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var protectContent: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    var lastName: String? = null
    var vcard: String? = null

    override fun send(chatId: ChatId): MessageResponse {
        return service.sendContact(
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
        ).execute().body() ?: errorBody()
    }
}

class DiceMessage(
    val emoji: String,
    override val service: TelegramService,
    override val fileService: TelegramFileService
) : IMessage<MessageResponse>, KeyboardCreator {

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var protectContent: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    override fun send(chatId: ChatId): MessageResponse {
        return service.sendDice(
            chatId = chatId,
            messageThreadId = messageThreadId,
            emoji = emoji,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }
}