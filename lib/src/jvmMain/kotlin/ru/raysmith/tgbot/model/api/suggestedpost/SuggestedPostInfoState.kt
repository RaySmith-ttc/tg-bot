package ru.raysmith.tgbot.model.api.suggestedpost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SuggestedPostInfoState {

    @SerialName("pending")
    PENDING,

    @SerialName("approved")
    APPROVED,

    @SerialName("declined")
    DECLINED,
}
