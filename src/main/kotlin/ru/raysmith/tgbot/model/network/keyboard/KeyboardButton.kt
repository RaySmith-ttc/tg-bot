package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.model.network.message.Message

/**
 * This object represents one button of the reply keyboard. For simple text buttons String can be used instead of
 * this object to specify text of the button. Optional fields *[request_contact][KeyboardButton.requestContact]*,
 * *[request_location][KeyboardButton.requestLocation]*, and *request_poll*
 * are mutually exclusive.
 * */
@Serializable
data class KeyboardButton(
    /**
     * Text of the button. If none of the optional fields are used,
     * it will be sent as a message when the button is pressed
     * */
    @SerialName("text") val text: String,

    /**
     * If specified, pressing the button will open a list of suitable users.
     * Tapping on any user will send their identifier to the bot in a [userShared][Message.usersShared] service message.
     * Available in private chats only.
     * */
    val requestUsers: KeyboardButtonRequestUsers? = null,

    /**
     * If specified, pressing the button will open a list of suitable chats.
     * Tapping on a chat will send its identifier to the bot in a [chatShared][Message.chatShared] service message.
     * Available in private chats only.
     * */
    @SerialName("request_chat") val requestChat: KeyboardButtonRequestChat? = null,

    /**
     * If True, the user's phone number will be sent as a contact when the button is pressed.
     * Available in private chats only
     * */
    @SerialName("request_contact") val requestContact: Boolean? = null,

    /** If True, the user's current location will be sent when the button is pressed. Available in private chats only */
    @SerialName("request_location") val requestLocation: Boolean? = null,

    /**
     * If specified, the user will be asked to create a poll and send it to the bot when the button is pressed.
     * Available in private chats only.
     * */
    @SerialName("request_poll") val requestPoll: KeyboardButtonPollType? = null,

    @SerialName("web_app") val webApp: WebAppInfo? = null
) : IKeyboardButton