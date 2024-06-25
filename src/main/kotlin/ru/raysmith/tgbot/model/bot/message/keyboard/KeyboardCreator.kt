@file:JvmName("KeyboardDslKt")

package ru.raysmith.tgbot.model.bot.message.keyboard

@KeyboardDsl
interface KeyboardCreator : InlineKeyboardCreator, ReplyKeyboardCreator, RemoveKeyboardCreator, ForceReplyKeyboardCreator {

    /** Keyboard markup of a message */
    override var keyboardMarkup: MessageKeyboard?
}

inline fun buildReplyKeyboard(init: MessageReplyKeyboard.() -> Unit): MessageReplyKeyboard {
    return MessageReplyKeyboard().apply(init)
}

inline fun buildInlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageInlineKeyboard {
    return MessageInlineKeyboard().apply(init)
}

inline fun buildKeyboard(init: KeyboardCreator.() -> Unit): MessageKeyboard? {
    return object : KeyboardCreator{
        override var keyboardMarkup: MessageKeyboard? = null
    }.apply { init() }.keyboardMarkup
}

inline fun buildReplyKeyboardRemove(init: RemoveKeyboard.() -> Unit): RemoveKeyboard {
    return RemoveKeyboard().apply(init)
}

inline fun buildForceReplyKeyboard(init: ForceReplyKeyboard.() -> Unit): ForceReplyKeyboard {
    return ForceReplyKeyboard().apply(init)
}