package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup

class MessageInlineKeyboard : MessageKeyboard {
    private val rows: MutableList<MessageInlineKeyboardRow> = mutableListOf()

    override fun toMarkup(): KeyboardMarkup {
        return InlineKeyboardMarkup(rows.map { it.getRow() })
    }

    fun row(setRow: MessageInlineKeyboardRow.() -> Unit) {
        rows.add(
            MessageInlineKeyboardRow()
                .apply(setRow)
        )
    }

    inner class MessageInlineKeyboardRow {
        private val row: MutableList<InlineKeyboardButton> = mutableListOf()

        internal fun getRow(): List<InlineKeyboardButton> = row

        fun button(text: String, callbackData: String) {
            row.add(
                MessageInlineKeyboardButton()
                    .apply { this.text = text; this.callbackData = callbackData }
                    .toKeyboardButton()
            )
        }
        fun button(setButton: MessageInlineKeyboardButton.() -> Unit) {
            row.add(
                MessageInlineKeyboardButton()
                    .apply(setButton)
                    .toKeyboardButton()
            )
        }

        inner class MessageInlineKeyboardButton {
            var text: String = ""
            var url: String? = null
            var loginUrl: String? = null
            var callbackData: String? = null
            var switchInlineQuery: String? = null
            var pay: Boolean? = null

            internal fun toKeyboardButton(): InlineKeyboardButton {
                require(text.isNotEmpty()) { "Button text must be is not empty" }
                return InlineKeyboardButton(text, url, loginUrl, callbackData, switchInlineQuery, pay)
            }
        }
    }
}