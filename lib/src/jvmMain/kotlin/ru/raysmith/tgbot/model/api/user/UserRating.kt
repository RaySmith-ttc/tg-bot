package ru.raysmith.tgbot.model.api.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object describes the rating of a user based on their Telegram Star spendings. */
@Serializable
data class UserRating(

    /**
     * Current level of the user, indicating their reliability when purchasing digital goods and services.
     * A higher level suggests a more trustworthy customer; a negative level is likely reason for concern.
     * */
    @SerialName("level") val level: Int,

    /** Numerical value of the user's rating; the higher the rating, the better */
    @SerialName("rating") val rating: Int,

    /** The rating value required to get the current level */
    @SerialName("current_level_rating") val currentLevelRating: Int,

    /** The rating value required to get to the next level; omitted if the maximum level was reached */
    @SerialName("next_level_rating") val nextLevelRating: Int? = null,
)
