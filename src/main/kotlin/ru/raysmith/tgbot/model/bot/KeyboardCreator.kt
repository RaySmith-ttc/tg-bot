package ru.raysmith.tgbot.model.bot

@DslMarker
annotation class KeyboardDsl

@KeyboardDsl
interface InlineKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    @KeyboardDsl
    fun inlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageKeyboard {
        keyboardMarkup = MessageInlineKeyboard().apply(init)
        return keyboardMarkup as MessageKeyboard
    }
}

@KeyboardDsl
interface ReplyKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    @KeyboardDsl
    fun replyKeyboard(init: MessageReplyKeyboard.() -> Unit = {}): MessageKeyboard {
        keyboardMarkup = MessageReplyKeyboard().apply(init)
        return keyboardMarkup as MessageReplyKeyboard
    }
}


@KeyboardDsl
interface RemoveKeyboardCreator {
    var keyboardMarkup: MessageKeyboard?

    @KeyboardDsl
    fun removeKeyboard(init: RemoveKeyboard.() -> Unit): RemoveKeyboard {
        keyboardMarkup = RemoveKeyboard().apply(init)
        return keyboardMarkup as RemoveKeyboard
    }
}

@KeyboardDsl
interface KeyboardCreator : InlineKeyboardCreator, ReplyKeyboardCreator, RemoveKeyboardCreator {

    /** Keyboard markup of a message */
    override var keyboardMarkup: MessageKeyboard?
}

inline fun buildReplyKeyboard(init: MessageReplyKeyboard.() -> Unit): MessageKeyboard {
    return MessageReplyKeyboard().apply(init)
}

inline fun buildInlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageInlineKeyboard {
    return MessageInlineKeyboard().apply(init)
}