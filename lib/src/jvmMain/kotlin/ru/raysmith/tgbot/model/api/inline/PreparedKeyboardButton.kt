package ru.raysmith.tgbot.model.api.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a keyboard button to be used by a user of a Mini App. */
@Serializable
data class PreparedKeyboardButton(

    /** Unique identifier of the keyboard button */
    @SerialName("id") val id: String,
)
