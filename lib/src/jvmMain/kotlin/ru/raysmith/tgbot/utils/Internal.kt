package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.utils.properties.getOrNull

internal fun obtainToken(): String? {
    val token = Bot.properties?.getOrNull("token")
        ?: System.getenv("TG_BOT_TOKEN")
        ?: System.getenv("BOT_TOKEN") // TODO [stable] remove

    return token?.ifEmpty { null }
}