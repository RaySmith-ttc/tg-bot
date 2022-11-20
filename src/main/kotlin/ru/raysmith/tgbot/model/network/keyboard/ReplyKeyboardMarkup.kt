package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.*
import kotlinx.serialization.json.encodeToJsonElement
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramApi

@Serializable
/** This object represents a custom keyboard with reply options (see Introduction to bots for details and examples). */
data class ReplyKeyboardMarkup(
    /** Array of button rows, each represented by an Array of [KeyboardButton][KeyboardButton] objects */
    @SerialName("keyboard") @Required val keyboard: List<List<KeyboardButton>>,

    /**
     * Requests clients to resize the keyboard vertically for optimal fit (e.g., make the keyboard smaller
     * if there are just two rows of buttons). Defaults to *false*, in which case the custom keyboard is always
     * of the same height as the app's standard keyboard.
     * */
    @SerialName("resize_keyboard") val resizeKeyboard: Boolean? = null,

    /**
     * Requests clients to hide the keyboard as soon as it's been used. The keyboard will still be available, but
     * clients will automatically display the usual letter-keyboard in the chat – the user can press a special button
     * in the input field to see the custom keyboard again. Defaults to *false*.
     * */
    @SerialName("one_time_keyboard") val oneTimeKeyboard: Boolean? = null,

    /** The placeholder to be shown in the input field when the keyboard is active; 1-64 characters */
    @SerialName("input_field_placeholder") val inputFieldPlaceholder: String? = null,

    /**
     * Use this parameter if you want to show the keyboard to specific users only. Targets:
     * - users that are &#064;mentioned in the *text* of the [Message] object;
     * - if the bot's message is a reply (has *reply_to_message_id*), sender of the original message.
     *
     * *Example:* A user requests to change the bot's language, bot replies to the request with a keyboard
     * to select the new language. Other users in the group don't see the keyboard.
     * */
    @SerialName("selective") val selective: Boolean? = null
) : KeyboardMarkup() {
    override fun toString(): String {
        return TelegramApi.json.encodeToString(this)
    }
}