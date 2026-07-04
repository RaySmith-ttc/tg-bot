package ru.raysmith.tgbot.model.api.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.Audio

/** This object represents the audios displayed on a user's profile. */
@Serializable
data class UserProfileAudios(

    /** Total number of profile audios for the target user */
    @SerialName("total_count") val totalCount: Int,

    /** Requested profile audios */
    @SerialName("audios") val audios: List<Audio>,
)
