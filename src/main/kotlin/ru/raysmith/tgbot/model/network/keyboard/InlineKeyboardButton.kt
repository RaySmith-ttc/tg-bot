package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo

interface IKeyboardButton
@Serializable
/** This object represents one button of an inline keyboard. You **must** use exactly one of the optional fields. */
data class InlineKeyboardButton(

    /** Label text on the button */
    @SerialName("text") val text: String,

    /** Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes */
    @SerialName("callback_data") val callbackData: String? = null,

    /**
     * Description of the [Web App](https://core.telegram.org/bots/webapps) that will be launched when the user
     * presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method
     * [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery).
     * Available only in private chats between a user and the bot.
     * */
    @SerialName("web_app") val webApp: WebAppInfo? = null,

    /** HTTP or tg:// url to be opened when button is pressed */
    @SerialName("url") val url: String? = null,

    /**
     * An HTTP URL used to automatically authorize the user. Can be used as a replacement for the Telegram Login Widget.
     * @see <a href="https://core.telegram.org/widgets/login">Telegram Login Widget</a>
     * */
    @SerialName("login_url") val loginUrl: String? = null,

    /**
     * If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the
     * bot's username and the specified inline query in the input field. Can be empty, in which case just the bot's
     * username will be inserted.
     *
     * Note: This offers an easy way for users to start using your bot in inline mode when they are currently in a
     * private chat with it. Especially useful when combined with switch_pm… actions – in this case the user will be
     * automatically returned to the chat they switched from, skipping the chat selection screen.
     *
     * TODO add link switch_pm… to answerInlineQuery.switch_pm_parameter field
     * */
    @SerialName("switch_inline_query") val switchInlineQuery: String? = null,

    // TODO [game support] add callback_game (https://core.telegram.org/bots/api#inlinekeyboardbutton)

    /**
     * Specify True, to send a Pay button.
     *
     * NOTE: This type of button **must** always be the first button in the first row.
     *
     * @see <a href="https://core.telegram.org/bots/api#payments">https://core.telegram.org/bots/api#payments</a>
     * */
    @SerialName("pay") val pay: Boolean? = null
) : IKeyboardButton