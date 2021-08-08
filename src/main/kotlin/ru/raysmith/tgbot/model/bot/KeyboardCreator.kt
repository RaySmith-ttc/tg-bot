package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardRemove

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

    fun removeKeyboard(selective: Boolean? = null) {
        keyboardMarkup = object : MessageKeyboard {
            override fun toMarkup(): KeyboardMarkup {
                return ReplyKeyboardRemove(selective = selective)
            }
        }
    }

}

fun replyKeyboard(init: MessageReplyKeyboard.() -> Unit): MessageKeyboard {
    return MessageReplyKeyboard().apply(init)
}

fun inlineKeyboard(init: MessageInlineKeyboard.() -> Unit): MessageKeyboard {
    return MessageInlineKeyboard().apply(init)
}