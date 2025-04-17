package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes data sent from a [Web App](https://core.telegram.org/bots/webapps) to the bot.
 *
 * @property data The data. Be aware that a bad client can send arbitrary data in this field.
 * @property buttonText Text of the *web_app* keyboard button from which the Web App was opened.
 *  Be aware that a bad client can send arbitrary data in this field.
 * */
@Serializable
data class WebAppData(
    @SerialName("data") val data: String,
    @SerialName("button_text") val buttonText: String
)