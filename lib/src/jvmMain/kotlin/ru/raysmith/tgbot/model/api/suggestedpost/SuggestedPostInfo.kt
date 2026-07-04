package ru.raysmith.tgbot.model.api.suggestedpost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import kotlin.time.Duration

/**
 * Contains information about a suggested post.
 *
 * @property state State of the suggested post
 * @property price Proposed price of the post. If the field is omitted, then the post is unpaid.
 * @property sendDate Proposed send date of the post. If the field is omitted, then the post can be published at any
 *  time within 30 days at the sole discretion of the user or administrator who approves it.
 * */
@Serializable
data class SuggestedPostInfo(
    @SerialName("state") val state: SuggestedPostInfoState,
    @SerialName("price") val price: SuggestedPostPrice? = null,
    @SerialName("send_date") val sendDate: Int? = null,
) {
    constructor(
        state: SuggestedPostInfoState,
        price: SuggestedPostPrice, sendDate:
        Duration
    ) : this(
        state,
        price,
        sendDate.inWholeSeconds.toInt()
    )
}


