package ru.raysmith.tgbot.model.bot.message.keyboard

interface InlineKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    @KeyboardDsl
    suspend fun inlineKeyboard(init: suspend MessageInlineKeyboard.() -> Unit): MessageKeyboard {
        keyboardMarkup = MessageInlineKeyboard().apply { init() }
        return keyboardMarkup as MessageKeyboard
    }
}