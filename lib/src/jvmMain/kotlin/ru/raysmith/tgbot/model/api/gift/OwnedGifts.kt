package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Contains the list of gifts received and owned by a user or a chat. */
@Serializable
data class OwnedGifts(

    /** The total number of gifts owned by the user or the chat */
    @SerialName("total_count") val totalCount: Int,

    /** The list of gifts */
    @SerialName("gifts") val gifts: List<OwnedGift>,

    /** Offset for the next request. If empty, then there are no more results. */
    @SerialName("next_offset") val nextOffset: String? = null,
)
