package ru.raysmith.tgbot.model.bot.message.keyboard

import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.utils.Pagination
import ru.raysmith.tgbot.utils.datepicker.DatePicker

class MessageInlineKeyboard : MessageKeyboard, Iterable<MessageInlineKeyboard.Row> {
    private val _rows: MutableList<Row> = mutableListOf()
    val rows: List<Row> = _rows

    override fun toMarkup(): KeyboardMarkup {
        return InlineKeyboardMarkup(_rows.map { it.buttons.map { b -> b.toKeyboardButton() } })
    }

    fun <T> pagination(
        data: Iterable<T>,
        callbackQueryPrefix: String,
        page: Long = Pagination.PAGE_FIRST,
        setup: Pagination<T>.() -> Unit = {},
        createButtons: Row.(item: T) -> Unit
    ) {
        Pagination(data, callbackQueryPrefix, createButtons)
            .apply(setup)
            .apply { this.startPage = page }
            .setupMarkup(this)
    }

    fun createDatePicker(datePicker: DatePicker, data: String? = null) {
        datePicker.setupMarkup(this, data)
    }

    fun row(row: Row) = _rows.add(row)
    fun row(text: String, callbackData: String) = row { button(text, callbackData) }
    fun row(setRow: Row.() -> Unit) {
        _rows.add(
            Row()
                .apply(setRow)
        )
    }

    class Row : MessageKeyboardRow<Button> {
        private val _buttons: MutableList<Button> = mutableListOf()
        override val buttons: List<Button> = _buttons

        override fun button(button: Button) {
            _buttons.add(button)
        }
        fun button(text: String, callbackData: String) {
            _buttons.add(
                Button().apply {
                    this.text = text
                    this.callbackData = callbackData
                }
            )
        }
        fun button(setButton: Button.() -> Unit) {
            _buttons.add(Button().apply(setButton))
        }

        override fun iterator(): Iterator<Button> = buttons.iterator()
    }

    class Button : MessageKeyboardButton {
        var text: String = ""
        var url: String? = null
        var loginUrl: String? = null
        var callbackData: String? = null
        var webApp: WebAppInfo? = null
        var switchInlineQuery: String? = null
        var switchInlineQueryCurrentChat: String? = null
        var switchInlineQueryChosenChat: String? = null
        var pay: Boolean? = null

        override fun toKeyboardButton(): InlineKeyboardButton {
            return InlineKeyboardButton(
                text, url, callbackData, webApp, loginUrl, switchInlineQuery, switchInlineQueryCurrentChat,
                switchInlineQueryChosenChat, pay
            )
        }
    }

    override fun iterator(): Iterator<Row> = _rows.iterator()
}