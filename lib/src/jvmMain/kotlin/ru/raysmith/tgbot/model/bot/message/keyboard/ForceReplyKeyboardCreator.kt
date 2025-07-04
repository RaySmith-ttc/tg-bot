package ru.raysmith.tgbot.model.bot.message.keyboard

interface ForceReplyKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    fun forceReplyKeyboard(init: ForceReplyKeyboard.() -> Unit): ForceReplyKeyboard {
        keyboardMarkup = ForceReplyKeyboard().apply(init)
        return keyboardMarkup as ForceReplyKeyboard
    }
}