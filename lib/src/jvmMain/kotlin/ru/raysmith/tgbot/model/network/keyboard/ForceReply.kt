package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

/**
 * Upon receiving a message with this object, Telegram clients will display a reply interface to the user (act as if
 * the user has selected the bot's message and tapped 'Reply'). This can be extremely useful if you want to create
 * user-friendly step-by-step interfaces without having to sacrifice
 * [privacy mode](https://core.telegram.org/bots/features#privacy-mode). Not supported in channels and for messages
 * sent on behalf of a Telegram Business account.
 * */
@Serializable
data class ForceReply(

    /** Shows reply interface to the user, as if they manually selected the bot's message and tapped 'Reply' */
    @EncodeDefault @SerialName("force_reply") val forceReply: Boolean = true,

    /** The placeholder to be shown in the input field when the reply is active; 1-64 characters */
    @SerialName("input_field_placeholder") val inputFieldPlaceholder: String? = null,

    /**
     * Use this parameter if you want to force reply from specific users only. Targets:
     * - users that are @mentioned in the text of the [Message] object;
     * - if the bot's message is a reply to a message in the same chat and forum topic, sender of the original message.
     * */
    @SerialName("selective") val selective: Boolean? = null,
) : KeyboardMarkup()