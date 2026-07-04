package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.api.menubutton.WebAppInfo
import ru.raysmith.tgbot.network.API

interface IKeyboardButton
/**
 * This object represents one button of an inline keyboard. Exactly one of the fields other than [text],
 * [iconCustomEmojiId], and [style] must be used to specify the type of the button.
 * */
@Serializable
data class InlineKeyboardButton(

    /** Label text on the button */
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
     * HTTP or tg:// URL to be opened when the button is pressed. Links `tg://user?id=<user_id>` can be used to mention
     * a user by their identifier without using a username, if this is allowed by their privacy settings.
     * */
    @SerialName("url") val url: String? = null,

    /** Data to be sent in a callback query to the bot when the button is pressed, 1-64 bytes */
    @SerialName("callback_data") val callbackData: String? = null,

    /**
     * Description of the [Web App](https://core.telegram.org/bots/webapps) that will be launched when the user
     * presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method
     * [answerWebAppQuery][API.answerWebAppQuery].
     * Available only in private chats between a user and the bot.
     * Not supported for messages sent on behalf of a business account.
     * */
    @SerialName("web_app") val webApp: WebAppInfo? = null,

    /**
     * An HTTP URL used to automatically authorize the user. Can be used as a replacement for the
     * [Telegram Login Widget](https://core.telegram.org/widgets/login).
     * */
    @SerialName("login_url") val loginUrl: LoginUrl? = null,

    /**
     * If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the
     * bot's username and the specified inline query in the input field. May be empty, in which case just the bot's
     * username will be inserted. Not supported for messages sent in channel direct messages chats and on behalf of a
     * business account.
     * */
    @SerialName("switch_inline_query") val switchInlineQuery: String? = null,

    /**
     * If set, pressing the button will insert the bot's username and the specified inline query in the current chat's
     * input field. May be empty, in which case only the bot's username will be inserted.
     *
     * This offers a quick way for the user to open your bot in inline mode in the same chat - good for selecting
     * something from multiple options. Not supported in channels and for messages sent in channel direct messages
     * chats and on behalf of a business account.
     * */
    @SerialName("switch_inline_query_current_chat") val switchInlineQueryCurrentChat: String? = null,

    /**
     * If set, pressing the button will prompt the user to select one of their chats of the specified type, open that
     * chat and insert the bot's username and the specified inline query in the input field. Not supported for messages
     * sent in channel direct messages chats and on behalf of a business account.
     * */
    @SerialName("switch_inline_query_chosen_chat") val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null,

    /** Description of the button that copies the specified text to the clipboard */
    @SerialName("copy_text") val copyText: CopyTextButton? = null,

    // TODO [game support] add callback_game (https://core.telegram.org/bots/api#inlinekeyboardbutton)

    /**
     * Specify True, to send a [Pay button](https://core.telegram.org/bots/api#payments).
     * Substrings “⭐” and [Currency.XTR] in the buttons's text will be replaced with a Telegram Star icon.
     *
     * NOTE: This type of button **must** always be the first button in the first row.
     * */
    @SerialName("pay") val pay: Boolean? = null

) : IKeyboardButton
