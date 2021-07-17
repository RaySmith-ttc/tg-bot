package ru.raysmith.tgbot.model.network.updates

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.ChosenInlineResult
import ru.raysmith.tgbot.model.network.InlineQuery
import ru.raysmith.tgbot.model.network.message.Message

/**
 * This object represents an incoming update.
 * At most **one** of the optional parameters can be present in any given update
 * */
@Serializable
data class Update(

    /**
     * The update's unique identifier. Update identifiers start from a certain positive number and increase sequentially.
     * This ID becomes especially handy if you're using Webhooks, since it allows you to ignore repeated updates or
     * to restore the correct update sequence, should they get out of order. If there are no new updates for at least
     * a week, then identifier of the next update will be chosen randomly instead of sequentially.
     * */
    @SerialName("update_id") val updateId: Int,

    /** New incoming message of any kind — text, photo, sticker, etc. */
    @SerialName("message") val message: Message? = null,

    /** New version of a message that is known to the bot and was edited */
    @SerialName("edited_message") val editedMessage: Message? = null,

    /** New incoming channel post of any kind — text, photo, sticker, etc. */
    @SerialName("channel_post") val channelPost: Message? = null,

    /** New version of a channel post that is known to the bot and was edited */
    @SerialName("edited_channel_post") val editedChannelPost: Message? = null,

    /** New incoming inline query */
    @SerialName("inline_query") val inlineQuery: InlineQuery? = null,

    /**
     * The result of an inline query that was chosen by a user and sent to their chat partner.
     * Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
     *
     * @see <a href="https://core.telegram.org/bots/inline#collecting-feedback">Collecting feedback</a>
     * */
    @SerialName("chosen_inline_result") val chosenInlineResult: ChosenInlineResult? = null,

    /** New incoming callback query */
    @SerialName("callback_query") val callbackQuery: CallbackQuery? = null,

    // TODO [payment support] add shipping_query field (https://core.telegram.org/bots/api#update)
    // TODO [payment support] add pre_checkout_query field (https://core.telegram.org/bots/api#update)

    // TODO [poll support]
//    /** New poll state. Bots receive only updates about stopped polls and polls, which are sent by the bot */
//    @SerialName("poll") val poll: Poll? = null,

    // TODO [poll support]
//    /** A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself. */
//    @SerialName("poll_answer") val pollAnswer: PollAnswer? = null,

    // TODO [chat member support] add my_chat_member field (https://core.telegram.org/bots/api#update)
    // TODO [chat member support] add chat_member field (https://core.telegram.org/bots/api#update)

)

