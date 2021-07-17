package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup

interface MessageKeyboard {
    fun toMarkup(): KeyboardMarkup?
}