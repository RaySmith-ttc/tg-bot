package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/**
 * This object represents one button of the reply keyboard. For simple text buttons String can be used instead of
 * this object to specify text of the button. Optional fields *[request_contact][KeyboardButton.requestContact]*,
 * *[request_location][KeyboardButton.requestLocation]*, and *request_poll*
 * are mutually exclusive.
 * */
data class KeyboardButton(
    /** Text of the button. If none of the optional fields are used, it will be sent as a message when the button is pressed */
    @SerialName("text") val text: String,

    /** If True, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only */
    @SerialName("request_contact") val requestContact: Boolean? = null,

    /** If True, the user's current location will be sent when the button is pressed. Available in private chats only */
    @SerialName("request_location") val requestLocation: Boolean? = null,
    // TODO [poll support] add request_poll field (https://core.telegram.org/bots/api#keyboardbutton)
) : IKeyboardButton