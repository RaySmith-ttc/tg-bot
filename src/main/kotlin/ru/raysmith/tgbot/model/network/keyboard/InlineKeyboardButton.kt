package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.network.API

interface IKeyboardButton
/** This object represents one button of an inline keyboard. You **must** use exactly one of the optional fields. */
@Serializable
data class InlineKeyboardButton(

    /** Label text on the button */
    @SerialName("text") val text: String,

    /**
     * HTTP or tg:// URL to be opened when the button is pressed. Links `tg://user?id=<user_id>` can be used to mention
     * a user by their ID without using a username, if this is allowed by their privacy settings.
     * */
    @SerialName("url") val url: String? = null,

    /** Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes */
    @SerialName("callback_data") val callbackData: String? = null,

    /**
     * Description of the [Web App](https://core.telegram.org/bots/webapps) that will be launched when the user
     * presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method
     * [answerWebAppQuery][API.answerWebAppQuery].
     * Available only in private chats between a user and the bot.
     * */
    @SerialName("web_app") val webApp: WebAppInfo? = null,

    /**
     * An HTTP URL used to automatically authorize the user. Can be used as a replacement for the
     * [Telegram Login Widget](https://core.telegram.org/widgets/login).
     * */
    @SerialName("login_url") val loginUrl: String? = null,

    /**
     * If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the
     * bot's username and the specified inline query in the input field. Can be empty, in which case just the bot's
     * username will be inserted.
     * */
    @SerialName("switch_inline_query") val switchInlineQuery: String? = null,

    /**
     * If set, pressing the button will insert the bot's username and the specified inline query in the current chat's
     * input field. May be empty, in which case only the bot's username will be inserted.
     *
     * This offers a quick way for the user to open your bot in inline mode in the same chat - good
     * for selecting something from multiple options.
     * */
    @SerialName("switch_inline_query_current_chat") val switchInlineQueryCurrentChat: String? = null,

    /**
     * f set, pressing the button will prompt the user to select one of their chats of the specified type, open that
     * chat and insert the bot's username and the specified inline query in the input field
     * */
    @SerialName("switch_inline_query_chosen_chat") val switchInlineQueryChosenChat: String? = null,

    // TODO [game support] add callback_game (https://core.telegram.org/bots/api#inlinekeyboardbutton)

    /**
     * Specify True, to send a [Pay button](https://core.telegram.org/bots/api#payments).
     *
     * NOTE: This type of button **must** always be the first button in the first row.
     * */
    @SerialName("pay") val pay: Boolean? = null

) : IKeyboardButton