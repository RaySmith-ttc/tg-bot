@file:JvmName("KeyboardDslKt")

package ru.raysmith.tgbot.model.bot.message.keyboard

@KeyboardDsl
interface KeyboardCreator : InlineKeyboardCreator, ReplyKeyboardCreator, RemoveKeyboardCreator {

    /** Keyboard markup of a message */
    override var keyboardMarkup: MessageKeyboard?
}

suspend inline fun buildReplyKeyboard(crossinline init: suspend MessageReplyKeyboard.() -> Unit): MessageKeyboard {
    return MessageReplyKeyboard().apply { init() }
}

suspend inline fun buildInlineKeyboard(crossinline init: suspend MessageInlineKeyboard.() -> Unit): MessageInlineKeyboard {
    return MessageInlineKeyboard().apply { init() }
}