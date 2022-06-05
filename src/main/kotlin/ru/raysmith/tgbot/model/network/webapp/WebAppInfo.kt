package ru.raysmith.tgbot.model.network.webapp

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName

/** Contains information about a [Web App](https://core.telegram.org/bots/webapps). */
@kotlinx.serialization.Serializable
data class WebAppInfo(

    /** An HTTPS URL of a Web App to be opened with additional data as specified in
     * [Initializing Web Apps](https://core.telegram.org/bots/webapps#initializing-web-apps) */
    @SerialName("url") val url: String
)

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 * - [MenuButtonCommands]
 * - [MenuButtonWebApp]
 * - [MenuButtonDefault]
 *
 * If a menu button other than MenuButtonDefault is set for a private chat, then it is applied in the chat.
 * Otherwise the default menu button is applied. By default, the menu button opens the list of bot commands.
 * */
@kotlinx.serialization.Serializable
sealed class MenuButton {
    abstract val type: String
}

/** Represents a menu button, which opens the bot's list of commands. */
@kotlinx.serialization.Serializable
class MenuButtonCommands : MenuButton() {

    /** Type of the button, must be *commands* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "commands"
}

/** Describes that no specific value for the menu button was set. */
@kotlinx.serialization.Serializable
class MenuButtonDefault : MenuButton() {

    /** Type of the button, must be *default* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "default"
}

/** Represents a menu button, which launches a [Web App](https://core.telegram.org/bots/webapps). */
@kotlinx.serialization.Serializable
data class MenuButtonWebApp(

    /** Text on the button */
    @SerialName("text")
    val text: String,

    /**
     * Description of the Web App that will be launched when the user presses the button.
     * The Web App will be able to send an arbitrary message on behalf of the user using the method
     * [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery).
     * */
    @SerialName("web_app")
    val webApp: WebAppInfo
) : MenuButton() {

    /** Type of the button, must be *web_app* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "web_app"
}