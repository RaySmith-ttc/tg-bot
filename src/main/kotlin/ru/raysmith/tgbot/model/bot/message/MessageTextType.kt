package ru.raysmith.tgbot.model.bot.message

enum class MessageTextType(val maxLength: Int) {
    TEXT(IMessage.MAX_TEXT_LENGTH),
    CAPTION(IMessage.MAX_CAPTION_LENGTH),
    POLL_EXPLANATION(IMessage.MAX_POLL_EXPLANATION_LENGTH)
}