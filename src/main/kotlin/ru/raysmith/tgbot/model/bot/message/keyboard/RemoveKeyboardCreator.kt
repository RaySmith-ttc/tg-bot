package ru.raysmith.tgbot.model.bot.message.keyboard

@KeyboardDsl
interface RemoveKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    fun removeKeyboard(init: RemoveKeyboard.() -> Unit): RemoveKeyboard {
        keyboardMarkup = RemoveKeyboard().apply(init)
        return keyboardMarkup as RemoveKeyboard
    }
}