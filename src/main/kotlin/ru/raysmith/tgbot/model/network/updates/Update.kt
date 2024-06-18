package ru.raysmith.tgbot.model.network.updates

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.*
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatJoinRequest
import ru.raysmith.tgbot.model.network.chat.member.ChatMemberUpdated
import ru.raysmith.tgbot.model.network.inline.InlineQuery
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.reaction.MessageReactionCountUpdated
import ru.raysmith.tgbot.model.network.message.reaction.MessageReactionUpdated
import ru.raysmith.tgbot.model.network.payment.PreCheckoutQuery
import ru.raysmith.tgbot.model.network.payment.ShippingQuery
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostRemoved
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostUpdated

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

    /**
     * A reaction to a message was changed by a user. The bot must be an administrator in the chat and must explicitly
     * specify "message_reaction" in the list of *allowed_updates* to receive these updates.
     * The update isn't received for reactions set by bots.
     * */
    @SerialName("message_reaction") val messageReaction: MessageReactionUpdated? = null,

    /**
     * Reactions to a message with anonymous reactions were changed. The bot must be an administrator in the chat and
     * must explicitly specify "message_reaction_count" in the list of *allowed_updates* to receive these updates.
     * The updates are grouped and can be sent with delay up to a few minutes.
     * */
    @SerialName("message_reaction_count") val messageReactionCount: MessageReactionCountUpdated? = null,

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

    /** New incoming shipping query. Only for invoices with flexible price */
    @SerialName("shipping_query") val shippingQuery: ShippingQuery? = null,

    /** New incoming pre-checkout query. Contains full information about checkout */
    @SerialName("pre_checkout_query") val preCheckoutQuery: PreCheckoutQuery? = null,

    /** New poll state. Bots receive only updates about stopped polls and polls, which are sent by the bot */
    @SerialName("poll") val poll: Poll? = null,

    /** A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself. */
    @SerialName("poll_answer") val pollAnswer: PollAnswer? = null,

    /**
     * The bot's chat member status was updated in a chat. For private chats, this update is received only when the
     * bot is blocked or unblocked by the user.
     * */
    @SerialName("my_chat_member") val myChatMember: ChatMemberUpdated? = null,

    /**
     * A chat member's status was updated in a chat. The bot must be an administrator in the chat and must
     * explicitly specify “chat_member” in the list of allowed_updates to receive these updates.
     * */
    @SerialName("chat_member") val chatMember: ChatMemberUpdated? = null,

    /**
     * A request to join the chat has been sent. The bot must have the can_invite_users administrator
     * right in the chat to receive these updates.
     * */
    @SerialName("chat_join_request") val chatJoinRequest: ChatJoinRequest? = null,

    /** A chat boost was added or changed. The bot must be an administrator in the chat to receive these updates. */
    @SerialName("chat_boost") val chatBoost: ChatBoostUpdated? = null,

    /** A boost was removed from a chat. The bot must be an administrator in the chat to receive these updates. */
    @SerialName("removed_chat_boost") val removedChatBoost: ChatBoostRemoved? = null,

) {

    /** Type of update. Null if unknown */
    val type: UpdateType? = when {
        message != null -> UpdateType.MESSAGE
        editedMessage != null -> UpdateType.EDITED_MESSAGE
        channelPost != null -> UpdateType.CHANNEL_POST
        editedChannelPost != null -> UpdateType.EDITED_CHANNEL_POST
        inlineQuery != null -> UpdateType.INLINE_QUERY
        chosenInlineResult != null -> UpdateType.CHOSEN_INLINE_RESULT
        callbackQuery != null -> UpdateType.CALLBACK_QUERY
        poll != null -> UpdateType.POLL
        pollAnswer != null -> UpdateType.POLL_ANSWER
        myChatMember != null -> UpdateType.MY_CHAT_MEMBER
        chatMember != null -> UpdateType.CHAT_MEMBER
        shippingQuery != null -> UpdateType.SHIPPING_QUERY
        preCheckoutQuery != null -> UpdateType.PRE_CHECKOUT_QUERY
        chatJoinRequest != null -> UpdateType.CHAT_JOIN_REQUEST
        messageReaction != null -> UpdateType.MESSAGE_REACTION
        messageReactionCount != null -> UpdateType.MESSAGE_REACTION_COUNT
        else -> null
    }

    /** Find [from][User] by current update. Always `null` for [UpdateType.POLL]  */
    fun findFrom(): User? = when(type) {
        UpdateType.MESSAGE -> message!!.from
        UpdateType.EDITED_MESSAGE -> editedMessage!!.from
        UpdateType.CHANNEL_POST -> channelPost!!.from
        UpdateType.EDITED_CHANNEL_POST -> editedChannelPost!!.from
        UpdateType.INLINE_QUERY -> inlineQuery!!.from
        UpdateType.CHOSEN_INLINE_RESULT -> chatMember!!.from
        UpdateType.CALLBACK_QUERY -> callbackQuery!!.from
        UpdateType.POLL -> null
        UpdateType.POLL_ANSWER -> pollAnswer!!.user
        UpdateType.MY_CHAT_MEMBER -> myChatMember!!.from
        UpdateType.CHAT_MEMBER -> chatMember!!.from
        UpdateType.SHIPPING_QUERY -> shippingQuery!!.from
        UpdateType.PRE_CHECKOUT_QUERY -> preCheckoutQuery!!.from
        UpdateType.CHAT_JOIN_REQUEST -> chatJoinRequest!!.from
        UpdateType.MESSAGE_REACTION -> messageReaction!!.user
        UpdateType.MESSAGE_REACTION_COUNT -> null
        null -> null
    }

    /** Find [chat][Chat] id by current update. Always `null` for [UpdateType.POLL] and [UpdateType.POLL_ANSWER] updates */
    fun findChatId(): ChatId? = when(type) {
        UpdateType.MESSAGE -> message!!.getChatId()
        UpdateType.EDITED_MESSAGE -> editedMessage!!.getChatId()
        UpdateType.CHANNEL_POST -> channelPost!!.getChatId()
        UpdateType.EDITED_CHANNEL_POST -> editedChannelPost!!.getChatId()
        UpdateType.INLINE_QUERY -> inlineQuery!!.getChatId()
        UpdateType.CHOSEN_INLINE_RESULT -> chosenInlineResult!!.getChatId()
        UpdateType.CALLBACK_QUERY -> callbackQuery!!.getChatId()
        UpdateType.POLL -> null
        UpdateType.POLL_ANSWER -> null
        UpdateType.MY_CHAT_MEMBER -> myChatMember!!.getChatId()
        UpdateType.CHAT_MEMBER -> chatMember!!.getChatId()
        UpdateType.SHIPPING_QUERY -> shippingQuery!!.getChatId()
        UpdateType.PRE_CHECKOUT_QUERY -> preCheckoutQuery!!.getChatId()
        UpdateType.CHAT_JOIN_REQUEST -> chatJoinRequest!!.getChatId()
        UpdateType.MESSAGE_REACTION -> messageReaction!!.getChatId()
        UpdateType.MESSAGE_REACTION_COUNT -> messageReactionCount!!.getChatId()
        null -> null
    }


    /**
     * Indicates whether an update handler was found and processing was started.
     *
     * If you are handling updates using the dsl [handleUnknown][ru.raysmith.tgbot.core.BaseEventHandlerFactory.handleUnknown] method, explicitly set the value yourself.  */
    var isHandled: Boolean = false
}

