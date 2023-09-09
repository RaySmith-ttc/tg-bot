package ru.raysmith.tgbot.model.bot.message.keyboard

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.keyboard.*
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo

@Serializable
class MessageReplyKeyboard : MessageKeyboard {
    var isPersistent: Boolean? = null
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
        return ReplyKeyboardMarkup(rows.map { it.getRow() }, isPersistent, resizeKeyboard, oneTimeKeyboard, inputFieldPlaceholder, selective).also {
            println(it)
        }
    }

    @Serializable
    class MessageReplyKeyboardRow {
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
            var requestUser: KeyboardButtonRequestUser? = null
            var requestChat:  KeyboardButtonRequestChat? = null
            var requestContact: Boolean? = null
            var requestLocation: Boolean? = null
            var requestPoll: KeyboardButtonPollType? = null
            var webApp: WebAppInfo? = null

            internal fun toKeyboardButton(): KeyboardButton {
                require(text.isNotEmpty()) { "Button text must be is not empty" }
                return KeyboardButton(text, requestUser, requestChat, requestContact, requestLocation, requestPoll, webApp)
            }
        }
    }
}