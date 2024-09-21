package ru.raysmith.tgbot.model.bot.message

import kotlinx.serialization.Serializable

@Serializable
enum class MessageTextType(val maxLength: Int) {
    TEXT(IMessage.MAX_TEXT_LENGTH),
    CAPTION(IMessage.MAX_CAPTION_LENGTH),
    POLL_QUESTION(IMessage.MAX_POLL_QUESTION_LENGTH),
    POLL_OPTION(IMessage.MAX_POLL_OPTION_LENGTH),
    POLL_EXPLANATION(IMessage.MAX_POLL_EXPLANATION_LENGTH),
}