package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** This object contains information about a paid media purchase */
@Serializable
data class PaidMediaPurchased(

    /** User who purchased the media */
    @SerialName("from") val from: User,

    /** Bot-specified paid media payload */
    @SerialName("paid_media_payload") val paidMediaPayload: String
)