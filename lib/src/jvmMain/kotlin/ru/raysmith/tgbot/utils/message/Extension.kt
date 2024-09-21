package ru.raysmith.tgbot.utils.message

import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

suspend fun <T> T.message(
    action: MessageAction,
    chatId: ChatId = getChatIdOrThrow(),
    setup: suspend TextMessage.() -> Unit
): Message where T : ISender, T : IEditor {
    return when(action) {
        MessageAction.SEND -> send(chatId, setup)
        MessageAction.EDIT -> edit(message = setup)
    }
}