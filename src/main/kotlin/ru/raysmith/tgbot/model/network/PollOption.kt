package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object contains information about one answer option in a poll. */
@Serializable
data class PollOption(
    /** Option text, 1-100 characters */
    @SerialName("text") val text: String,

    /** Number of users that voted for this option */
    @SerialName("voter_count") val voterCount: String,
)