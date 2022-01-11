import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.raysmith.utils.PropertiesFactory

class ResourceReader {

    @Test
    fun read() {
        val token = PropertiesFactory.from("bot.properties")["token"]
        assertThat(token)
            .isEqualTo("1729711415:AAEIpA1PUEwVEatOUtVhJqi1VE47fxmWZ6E")
    }
}