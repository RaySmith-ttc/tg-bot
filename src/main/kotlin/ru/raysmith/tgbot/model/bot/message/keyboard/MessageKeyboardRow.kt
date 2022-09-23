package ru.raysmith.tgbot.model.bot.message.keyboard

interface MessageKeyboardRow<T : MessageKeyboardButton> : Iterable<T> {
    fun getRow(): List<T>
    fun button(button: T)
}