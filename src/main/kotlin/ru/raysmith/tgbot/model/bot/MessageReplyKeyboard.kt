package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.model.network.keyboard.KeyboardButton
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardMarkup

class MessageReplyKeyboard : MessageKeyboard {
    var resizeKeyboard: Boolean? = true
    var oneTimeKeyboard: Boolean? = null
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null
    private val rows: MutableList<MessageReplyKeyboardRow> = mutableListOf()

    fun row(text: String) = row { button(text) }

    fun row(setRow: MessageReplyKeyboardRow.() -> Unit) {
        rows.add(
            MessageReplyKeyboardRow()
                .apply(setRow)
        )
    }

    override fun toMarkup(): ReplyKeyboardMarkup {
        return ReplyKeyboardMarkup(rows.map { it.getRow() }, resizeKeyboard, oneTimeKeyboard, inputFieldPlaceholder, selective)
    }

    inner class MessageReplyKeyboardRow {
        private val row: MutableList<KeyboardButton> = mutableListOf()

        fun button(text: String) {
            row.add(
                MessageReplyKeyboardButton()
                    .apply { this.text = text }
                    .toKeyboardButton()
            )
        }
        fun button(setButton: MessageReplyKeyboardButton.() -> Unit) {
            row.add(
                MessageReplyKeyboardButton()
                    .apply(setButton)
                    .toKeyboardButton()
            )
        }

        internal fun getRow(): List<KeyboardButton> = row

        inner class MessageReplyKeyboardButton {
            var text: String = ""
            var requestContact: Boolean? = null
            var requestLocation: Boolean? = null

            internal fun toKeyboardButton(): KeyboardButton {
                require(text.isNotEmpty()) { "Button text must be is not empty" }
                return KeyboardButton(text, requestContact, requestLocation)
            }
        }
    }
}