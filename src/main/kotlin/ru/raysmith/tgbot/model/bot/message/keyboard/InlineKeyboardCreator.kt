package ru.raysmith.tgbot.model.bot.message.keyboard

interface InlineKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    @KeyboardDsl
    fun inlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageKeyboard {
        keyboardMarkup = MessageInlineKeyboard().apply { init() }
        return keyboardMarkup as MessageKeyboard
    }
}