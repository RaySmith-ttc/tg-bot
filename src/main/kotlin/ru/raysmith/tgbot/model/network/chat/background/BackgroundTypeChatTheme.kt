package ru.raysmith.tgbot.model.network.chat.background

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The background is taken directly from a built-in chat theme. */
@Serializable
data class BackgroundTypeChatTheme(

    /** Name of the chat theme, which is usually an emoji */
    @SerialName("theme_name") val themeName: String,
) : BackgroundType() {

    /** Type of the background, always “chat_theme” */
    override val type: String = "chat_theme"
}