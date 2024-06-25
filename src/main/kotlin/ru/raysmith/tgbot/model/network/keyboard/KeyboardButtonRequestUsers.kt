package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.UsersShared

/**
 * This object defines the criteria used to request suitable users.
 * Information about the selected users will be shared with the bot when the corresponding button is pressed.
 * [More about requesting users »](https://core.telegram.org/bots/features#chat-and-user-selection)
 * */
@Serializable
data class KeyboardButtonRequestUsers(

    /**
     * Identifier of the request, that will be received back in the [UsersShared] object.
     * Must be unique within the message
     * */
    @SerialName("request_id") val requestId: Int,

    /**
     * Pass *True* to request bots, pass *False* to request regular users.
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("user_is_bot") val userIsBot: Boolean? = null,

    /**
     * Pass *True* to request premium users, pass *False* to request non-premium users.
     * If not specified, no additional restrictions are applied.
     * */
    @SerialName("user_is_premium") val userIsPremium: Boolean? = null,

    /** The maximum number of users to be selected; 1-10. Defaults to 1. */
    @SerialName("max_quantity") val maxQuantity: Int? = null,

    /** Pass *True* to request the users' first and last names */
    @SerialName("request_name") val requestName: Boolean? = null,

    /** Pass *True* to request the users' usernames */
    @SerialName("request_username") val requestUsername: Boolean? = null,

    /** Pass *True* to request the users' photos */
    @SerialName("request_photo") val requestPhoto: Boolean? = null,
)