package ru.raysmith.tgbot.model.network.menubutton

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Contains information about a [Web App](https://core.telegram.org/bots/webapps). */
@Serializable
data class WebAppInfo(

    /** An HTTPS URL of a Web App to be opened with additional data as specified in
     * [Initializing Web Apps](https://core.telegram.org/bots/webapps#initializing-mini-apps) */
    @SerialName("url") val url: String
)