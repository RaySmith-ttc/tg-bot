package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.UserShared

/**
 * This object defines the criteria used to request a suitable user.
 * The identifier of the selected user will be shared with the bot when the corresponding button is pressed.
 * [More about requesting users »](https://core.telegram.org/bots/features#chat-and-user-selection)
 * */
@Serializable
data class KeyboardButtonRequestUser(

    /**
     * Identifier of the request, which will be received back in the [UserShared] object.
     * Must be unique within the message
     * */
    @SerialName("request_id") val requestId: Int,

    /**
     * Pass *True* to request a bot, pass *False* to request a regular user.
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("user_is_bot") val userIsBot: Boolean? = null,

    /**
     * Pass *True* to request a premium user, pass *False* to request a non-premium user.
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("user_is_premium") val userIsPremium: Boolean? = null,
)