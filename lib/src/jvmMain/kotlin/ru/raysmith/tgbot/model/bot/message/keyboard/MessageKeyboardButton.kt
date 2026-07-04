package ru.raysmith.tgbot.model.bot.message.keyboard

import ru.raysmith.tgbot.model.api.keyboard.IKeyboardButton

interface MessageKeyboardButton {
    fun toKeyboardButton(): IKeyboardButton
}
