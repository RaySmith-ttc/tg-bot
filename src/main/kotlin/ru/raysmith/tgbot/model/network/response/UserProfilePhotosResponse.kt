package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import ru.raysmith.tgbot.model.network.UserProfilePhotos

data class UserProfilePhotosResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: UserProfilePhotos
)