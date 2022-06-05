package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.network.TelegramApi

interface MessageKeyboard {
    fun toMarkup(): KeyboardMarkup?
    fun toJson() = TelegramApi.json.encodeToString(toMarkup())
}