package ru.raysmith.tgbot.model.bot

abstract class KeyboardCreator {

    var keyboardMarkup: MessageKeyboard? = null

    fun replyKeyboard(init: MessageReplyKeyboard.() -> Unit): MessageKeyboard {
        keyboardMarkup = MessageReplyKeyboard()
            .apply(init)

        return keyboardMarkup as MessageReplyKeyboard
    }

    fun inlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageKeyboard {
        keyboardMarkup = MessageInlineKeyboard()
            .apply(init)

        return keyboardMarkup as MessageKeyboard
    }

}

fun replyKeyboard(init: MessageReplyKeyboard.() -> Unit): MessageKeyboard {
    return MessageReplyKeyboard().apply(init)
}

fun inlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageKeyboard {
    return MessageInlineKeyboard().apply(init)
}