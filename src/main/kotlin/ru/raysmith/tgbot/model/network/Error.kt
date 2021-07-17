package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents an error */
data class Error(
    /** In case of an unsuccessful request, 'ok' equals false */
    @SerialName("ok") val ok: Boolean,

    /** Explain */
    @SerialName("description") val description: String,

    /** Error code */
    @SerialName("error_code") val errorCode: Int
)