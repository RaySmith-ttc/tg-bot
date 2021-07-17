package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a chat photo. */
data class ChatPhoto(
    @SerialName("small_file_id") val smallFileId: String,
    @SerialName("small_file_unique_id") val smallFileUniqueId: String,
    @SerialName("big_file_id") val bigFileId: String,
    @SerialName("big_file_unique_id") val bigFileUniqueId: String,
)
