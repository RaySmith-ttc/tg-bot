package ru.raysmith.tgbot.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents an HTTP link. */
@Serializable
data class Link(

    /** URL of the link */
    @SerialName("url") val url: String,
)
