package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

/**
 * This object represents a custom keyboard with reply options (see Introduction to bots for details and examples).
 * Not supported in channels and for messages sent on behalf of a Telegram Business account.
 * */
@Serializable
data class ReplyKeyboardMarkup(

    /** Array of button rows, each represented by an Array of [KeyboardButton][KeyboardButton] objects */
    @SerialName("keyboard") @Required val keyboard: List<List<KeyboardButton>>,

    /**
     * Requests clients to always show the keyboard when the regular keyboard is hidden. Defaults to false,
     * in which case the custom keyboard can be hidden and opened with a keyboard icon.
     * */
    @SerialName("is_persistent") val isPersistent: Boolean? = null,

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
     * - users that are @mentioned in the text of the [Message] object;
     * - if the bot's message is a reply to a message in the same chat and forum topic, sender of the original message.
     *
     * Example: A user requests to change the bot's language, bot replies to the request with a keyboard to select the
     * new language. Other users in the group don't see the keyboard.
     * */
    @SerialName("selective") val selective: Boolean? = null
) : KeyboardMarkup()