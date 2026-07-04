package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a service message about a change in the price of paid messages within a chat. */
@Serializable
data class PaidMessagePriceChanged(

    /**
     * The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each
     * sent message
     * */
    @SerialName("paid_message_star_count") val paidMessageStarCount: Int,
)
