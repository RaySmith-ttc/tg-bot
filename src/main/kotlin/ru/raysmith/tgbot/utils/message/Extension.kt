package ru.raysmith.tgbot.utils.message

import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

fun <T> T.message(action: MessageAction, setup: TextMessage.() -> Unit): Message where T : ISender, T : IEditor {
    return when(action) {
        MessageAction.SEND -> send(message = setup)
        MessageAction.EDIT -> edit(message = setup)
    }
}