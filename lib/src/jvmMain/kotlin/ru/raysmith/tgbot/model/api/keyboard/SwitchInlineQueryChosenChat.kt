package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat, with an
 * optional default inline query.
 * */
@Serializable
data class SwitchInlineQueryChosenChat(

    /**
     * The default inline query to be inserted in the input field.
     * If left empty, only the bot's username will be inserted.
     * */
    @SerialName("query") val query: String? = null,

    /** *True*, if private chats with users can be chosen */
    @SerialName("allow_user_chats") val allowUserChats: Boolean? = null,

    /** *True*, if private chats with bots can be chosen */
    @SerialName("allow_bot_chats") val allowBotChats: Boolean? = null,

    /** *True*, if group and supergroup chats can be chosen */
    @SerialName("allow_group_chats") val allowGroupChats: Boolean? = null,

    /** *True*, if channel chats can be chosen */
    @SerialName("allow_channel_chats") val allowChannelChats: Boolean? = null,
)
