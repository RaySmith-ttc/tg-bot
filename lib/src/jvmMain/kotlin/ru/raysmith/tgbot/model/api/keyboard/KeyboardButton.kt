package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.menubutton.WebAppInfo
import ru.raysmith.tgbot.model.api.message.Message

/**
 * This object represents one button of the reply keyboard. At most one of the fields other than [text],
 * [iconCustomEmojiId], and [style] must be used to specify the type of the button.
 * For simple text buttons, *String* can be used instead of this object to specify the button text.
 * */ // TODO check how alternative method with simple text is working; maybe implement in API
@Serializable
data class KeyboardButton(
    /**
     * Text of the button. If none of the optional fields are used,
     * it will be sent as a message when the button is pressed
     * */
    @SerialName("text") val text: String,

    /**
     * Unique identifier of the custom emoji shown before the text of the button. Can only be used by bots that
     * purchased additional usernames on [Fragment](https://fragment.com/) or in the messages directly sent by the bot
     * to private, group and supergroup chats if the owner of the bot has a Telegram Premium subscription.
     * */
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null,

    /** Style of the button. If omitted, then an app-specific style is used. */
    @SerialName("style") val style: ButtonStyle? = null,

    /**
     * If specified, pressing the button will open a list of suitable users. Identifiers of selected users will be
     * sent to the bot in a [users_shared][Message.usersShared] service message. Available in private chats only.
     * */
    @SerialName("request_users") val requestUsers: KeyboardButtonRequestUsers? = null,

    /**
     * If specified, pressing the button will open a list of suitable chats. Tapping on a chat will send its identifier
     * to the bot in a [chatShared][Message.chatShared] service message. Available in private chats only.
     * */
    @SerialName("request_chat") val requestChat: KeyboardButtonRequestChat? = null,

    /**
     * If specified, pressing the button will ask the user to create and share a bot that will be managed by the current
     * bot. Available for bots that enabled management of other bots in the [@BotFather](https://t.me/botfather) Mini
     * App. Available in private chats only.
     * */
    @SerialName("request_managed_bot") val requestManagedBot: KeyboardButtonRequestManagedBot? = null,

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

