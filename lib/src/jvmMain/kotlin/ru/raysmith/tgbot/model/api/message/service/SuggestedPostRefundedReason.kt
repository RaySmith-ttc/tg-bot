package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Reason for the refund */
@Serializable
enum class SuggestedPostRefundedReason {

    /** The post was deleted within 24 hours of being posted or removed from scheduled messages without being posted */
    @SerialName("post_deleted")
    POST_DELETED,

    /** The payer refunded their payment */
    @SerialName("payment_refunded")
    PAYMENT_REFUNDED,
}
