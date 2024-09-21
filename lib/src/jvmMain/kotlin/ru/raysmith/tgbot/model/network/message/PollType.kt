package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PollType {
    @SerialName("quiz") QUIZ,
    @SerialName("regular") REGULAR
}