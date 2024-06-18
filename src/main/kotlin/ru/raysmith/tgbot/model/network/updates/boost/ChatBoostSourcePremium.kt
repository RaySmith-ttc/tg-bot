package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription
 * to another user.
 * */
@Serializable
data class ChatBoostSourcePremium(

    /** User that boosted the chat */
    val user: User
) : ChatBoostSource() {

    /** Source of the boost, always “premium” */
    override val source: String = "premium"
}