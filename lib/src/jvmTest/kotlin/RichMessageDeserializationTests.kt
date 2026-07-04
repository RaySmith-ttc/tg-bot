import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.model.api.message.rich.RichMessage
import ru.raysmith.utils.asResource
import kotlin.test.Test

class RichMessageDeserializationTests {

    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
        classDiscriminatorMode = ClassDiscriminatorMode.NONE
        prettyPrint = true
    }

    @Test
    fun deserialization() {
        val jsonText = "richMessage.json".asResource().readText()

        val richMessage = json.decodeFromString<RichMessage>(jsonText)
        println(richMessage)
    }

    @Test
    fun serialization() {
        val jsonText = "richMessage.json".asResource().readText()

        val richMessage = json.decodeFromString<RichMessage>(jsonText)
        val json = json.encodeToString(richMessage)
        println(json)
    }
}
