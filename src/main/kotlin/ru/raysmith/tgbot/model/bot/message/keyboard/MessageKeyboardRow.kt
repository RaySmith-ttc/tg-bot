package ru.raysmith.tgbot.model.bot.message.keyboard

interface MessageKeyboardRow<T : MessageKeyboardButton> : Iterable<T> {
    val buttons: List<T>
    fun button(button: T)
}