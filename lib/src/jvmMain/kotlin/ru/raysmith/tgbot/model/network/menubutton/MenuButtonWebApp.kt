package ru.raysmith.tgbot.model.network.menubutton

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents a menu button, which launches a [Web App](https://core.telegram.org/bots/webapps). */
@Serializable
data class MenuButtonWebApp(

    /** Text on the button */
    @SerialName("text")
    val text: String,

    /**
     * Description of the Web App that will be launched when the user presses the button.
     * The Web App will be able to send an arbitrary message on behalf of the user using the method
     * [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery). Alternatively, a `t.me` link to a
     * Web App of the bot can be specified in the object instead of the Web App's URL,
     * in which case the Web App will be opened as if the user pressed the link.
     * */
    @SerialName("web_app")
    val webApp: WebAppInfo
) : MenuButton() {

    /** Type of the button, must be *web_app* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "web_app"
}

