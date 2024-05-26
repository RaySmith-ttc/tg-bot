package ru.raysmith.tgbot.model.bot.message.keyboard

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.keyboard.*
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo

@Serializable
class MessageReplyKeyboard : MessageKeyboard {
    @EncodeDefault
    @SerialName("class")
    override val classDiscriminator = "MessageReplyKeyboard"
    var isPersistent: Boolean? = null
    var resizeKeyboard: Boolean? = true // TODO ?
    var oneTimeKeyboard: Boolean? = null
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null
    private val rows: MutableList<Row> = mutableListOf()

    suspend fun row(text: String) = row { button(text) }
    suspend fun row(setRow: suspend Row.() -> Unit) {
        rows.add(Row().apply { setRow() })
    }

    override fun toMarkup(): ReplyKeyboardMarkup {
        return ReplyKeyboardMarkup(
            rows.map { it.buttons.map { b -> b.toKeyboardButton() } }, isPersistent, resizeKeyboard, oneTimeKeyboard,
            inputFieldPlaceholder, selective
        )
    }

    @Serializable
    class Row : MessageKeyboardRow<Button> {
        private val _buttons: MutableList<Button> = mutableListOf()
        override val buttons: List<Button> = _buttons

        override fun button(button: Button) {
            _buttons.add(button)
        }
        fun button(text: String) {
            _buttons.add(
                Button().apply {
                    this.text = text
                }
            )
        }
        fun button(setButton: Button.() -> Unit) {
            _buttons.add(Button().apply(setButton))
        }

        override fun iterator() = buttons.iterator()
    }

    @Serializable
    class Button : MessageKeyboardButton {
        var text: String = ""
        var requestUser: KeyboardButtonRequestUser? = null
        var requestChat:  KeyboardButtonRequestChat? = null
        var requestContact: Boolean? = null
        var requestLocation: Boolean? = null
        var requestPoll: KeyboardButtonPollType? = null
        var webApp: WebAppInfo? = null

        override fun toKeyboardButton(): KeyboardButton {
            return KeyboardButton(text, requestUser, requestChat, requestContact, requestLocation, requestPoll, webApp)
        }
    }
}