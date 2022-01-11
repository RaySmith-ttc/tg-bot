package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.Pagination

class MessageInlineKeyboard : MessageKeyboard {
    private val rows: MutableList<MessageInlineKeyboardRow> = mutableListOf()
    val rowsCount get() = rows.size

    override fun toMarkup(): KeyboardMarkup {
        return InlineKeyboardMarkup(rows.map { it.getRow() })
    }

    fun <T> pagination(
        data: Iterable<T>,
        callbackQueryPrefix: String,
        page: Long = Pagination.PAGE_FIRST,
        setup: Pagination<T>.() -> Unit = {},
        createButtons: MessageInlineKeyboardRow.(item: T) -> Unit
    ) {
        Pagination(data, callbackQueryPrefix, createButtons)
            .apply(setup)
            .apply { this.startPage = page }
            .setupMarkup(this)
    }

    fun createDatePicker(datePicker: DatePicker) {
        datePicker.setupMarkup(this)
    }

    fun row(text: String, callbackData: String) = row { button(text, callbackData) }

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
                return InlineKeyboardButton(text, callbackData, url, loginUrl, switchInlineQuery, pay)
            }
        }
    }
}