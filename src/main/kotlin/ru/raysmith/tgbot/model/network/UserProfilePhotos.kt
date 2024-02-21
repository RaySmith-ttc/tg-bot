package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.PhotoSize

/** This object represent a user's profile pictures. */
@Serializable
data class UserProfilePhotos(

    /** Total number of profile pictures the target user has */
    @SerialName("total_count") val totalCount: Int,

    /** Requested profile pictures (in up to 4 sizes each) */
    @SerialName("photos") val photos: List<List<PhotoSize>>
)