package ru.raysmith.tgbot.model.api.updates.boost

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription
 * to another user.
 * */
@Serializable
data class ChatBoostSourceGiftCode(

    /** User that boosted the chat */
    @SerialName("user") val user: User,
) : ChatBoostSource {

    /** Source of the boost, always “gift_code” */
    @EncodeDefault
    @SerialName("source")
    override val source: String = "gift_code"
}

