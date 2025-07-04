package ru.raysmith.tgbot.model.bot.message.keyboard

interface ReplyKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    suspend fun replyKeyboard(init: suspend MessageReplyKeyboard.() -> Unit = {}): MessageKeyboard {
        keyboardMarkup = MessageReplyKeyboard().apply { init() }
        return keyboardMarkup as MessageReplyKeyboard
    }
}