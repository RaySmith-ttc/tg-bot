package ru.raysmith.tgbot.model.bot.message.keyboard

@KeyboardDsl
interface ReplyKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    @KeyboardDsl
    fun replyKeyboard(init: MessageReplyKeyboard.() -> Unit = {}): MessageKeyboard {
        keyboardMarkup = MessageReplyKeyboard().apply(init)
        return keyboardMarkup as MessageReplyKeyboard
    }
}