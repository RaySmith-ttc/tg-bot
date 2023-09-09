package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user allowing a bot to write messages after adding the
 * bot to the attachment menu or launching a Web App from a link.
 * */
@Serializable
data class WriteAccessAllowed(

    /** Name of the Web App which was launched from a link */
    @SerialName("web_app_name") val webAppName: String?,
)