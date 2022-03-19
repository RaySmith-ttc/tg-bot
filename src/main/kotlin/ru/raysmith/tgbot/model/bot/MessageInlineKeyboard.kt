package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.IKeyboardButton
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.Pagination

interface MessageKeyboardRow <T : MessageKeyboardButton> : Iterable<T> {
    fun getRow(): List<T>
    fun button(button: T)
}
interface MessageKeyboardButton {
    fun toKeyboardButton(): IKeyboardButton
}

// TODO [kodoctor] remove serializable & row & button; move inner classes
@Serializable
@KeyboardDsl
class MessageInlineKeyboard(private val rows: MutableList<MessageInlineKeyboardRow> = mutableListOf()) : MessageKeyboard, Iterable<MessageInlineKeyboard.MessageInlineKeyboardRow> {
    fun getRows(): List<MessageInlineKeyboardRow> = rows

    override fun toMarkup(): KeyboardMarkup {
        return InlineKeyboardMarkup(rows.map { it.getRow().map { b -> b.toKeyboardButton() } })
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

    fun createDatePicker(datePicker: DatePicker, data: String? = null) {
        datePicker.setupMarkup(this, data)
    }

    fun row(row: MessageInlineKeyboardRow) = rows.add(row)
    fun row(text: String, callbackData: String) = row { button(text, callbackData) }
    fun row(setRow: MessageInlineKeyboardRow.() -> Unit) {
        rows.add(
            MessageInlineKeyboardRow()
                .apply(setRow)
        )
    }

    @Serializable
    class MessageInlineKeyboardRow : MessageKeyboardRow<MessageInlineKeyboardRow.MessageInlineKeyboardButton> {
        private val row: MutableList<MessageInlineKeyboardButton> = mutableListOf()

        override fun getRow(): List<MessageInlineKeyboardButton> = row

        override fun button(button: MessageInlineKeyboardButton) {
            row.add(button)
        }
        fun button(text: String, callbackData: String) {
            row.add(
                MessageInlineKeyboardButton().apply {
                    this.text = text;
                    this.callbackData = callbackData
                }
            )
        }
        fun button(setButton: MessageInlineKeyboardButton.() -> Unit) {
            row.add(MessageInlineKeyboardButton().apply(setButton))
        }

        override fun iterator(): Iterator<MessageInlineKeyboardButton> = row.iterator()

        @Serializable
        class MessageInlineKeyboardButton : MessageKeyboardButton {
            var text: String = ""
            var url: String? = null
            var loginUrl: String? = null
            var callbackData: String? = null
            var switchInlineQuery: String? = null
            var pay: Boolean? = null

            override fun toKeyboardButton(): InlineKeyboardButton {
                require(text.isNotEmpty()) { "Button text must be is not empty" }
                return InlineKeyboardButton(text, callbackData, url, loginUrl, switchInlineQuery, pay)
            }
        }
    }

    override fun iterator(): Iterator<MessageInlineKeyboardRow> = rows.iterator()
}