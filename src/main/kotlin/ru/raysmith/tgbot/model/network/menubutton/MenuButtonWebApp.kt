package ru.raysmith.tgbot.model.network.menubutton

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import ru.raysmith.tgbot.network.serializer.getJsonObject
import ru.raysmith.tgbot.network.serializer.getPrimitive

/** Represents a menu button, which launches a [Web App](https://core.telegram.org/bots/webapps). */
@Serializable(with = MenuButtonWebAppSerializer::class)
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

/** Contains information about a [Web App](https://core.telegram.org/bots/webapps). */
@Serializable
data class WebAppInfo(

    /** An HTTPS URL of a Web App to be opened with additional data as specified in
     * [Initializing Web Apps](https://core.telegram.org/bots/webapps#initializing-mini-apps) */
    @SerialName("url") val url: String
)

internal object MenuButtonWebAppSerializer : KSerializer<MenuButtonWebApp> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MenuButtonWebApp") {
        element<String>("type")
        element<String>("text")
        element<WebAppInfo>("web_app")
    }
    override fun deserialize(decoder: Decoder): MenuButtonWebApp {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        require(element is JsonObject)

        return MenuButtonWebApp(
            text = getPrimitive(element, "text"),
            webApp = getJsonObject(element, "web_app").let {  webAppJsonObject ->
                WebAppInfo(
                    url = getPrimitive(webAppJsonObject, "url")
                )
            }
        )
    }
    override fun serialize(encoder: Encoder, value: MenuButtonWebApp) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.text)
            encodeSerializableElement(descriptor, 2, WebAppInfo.serializer(), value.webApp)
        }
    }
}