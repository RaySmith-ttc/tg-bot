package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.ChatShared
import ru.raysmith.tgbot.model.network.chat.ChatAdministratorRights

/**
 * This object defines the criteria used to request a suitable chat.
 * The identifier of the selected chat will be shared with the bot when the corresponding button is pressed.
 * [More about requesting users »](https://core.telegram.org/bots/features#chat-and-user-selection)
 * */
@Serializable
data class KeyboardButtonRequestChat(

    /**
     * Identifier of the request, which will be received back in the [ChatShared] object.
     * Must be unique within the message
     * */
    @SerialName("request_id") val requestId: Int,

    /** *True* to request a channel chat, pass *False* to request a group or a supergroup chat. */
    @SerialName("chat_is_channel") val chatIsChannel: Boolean? = null,

    /**
     * Pass *True* to request a forum supergroup, pass *False* to request a non-forum chat.
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("chat_is_forum") val chatIsForum: Boolean? = null,

    /**
     * Pass **True to request a supergroup or a channel with a username, pass *False* to request a chat
     * without a username. If not specified, no additional restrictions are applied.
     * */
    @SerialName("chat_has_username") val chatHasUsername: Boolean? = null,

    /**
     * Pass *True* to request a chat owned by the user. Otherwise, no additional restrictions are applied.
     * */
    @SerialName("chat_is_created") val chatIsCreated: Boolean? = null,

    /**
     * Required administrator rights of the user in the chat. The rights must be a superset of [botAdministratorRights].
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("user_administrator_rights") val userAdministratorRights: ChatAdministratorRights? = null,

    /**
     * Required administrator rights of the bot in the chat. The rights must be a subset of [userAdministratorRights].
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("bot_administrator_rights") val botAdministratorRights: ChatAdministratorRights? = null,

    /** Pass *True* to request a chat with the bot as a member. Otherwise, no additional restrictions are applied. */
    @SerialName("bot_is_member") val botIsMember: Boolean? = null,
)

