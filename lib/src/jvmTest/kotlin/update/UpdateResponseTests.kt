package update

import helper.FileLoader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.asParameter

class UpdateResponseTests {

    @Test
    fun `update with entities`() {
        val actualUpdate = TelegramApi.json.decodeFromString<Update>(FileLoader.read("updateWithEntities.json"))

        assertThat(actualUpdate.message!!.getAllEntity(MessageEntityType.URL))
            .hasSize(1).first().isEqualTo("https://telegram.org")
        assertThat(actualUpdate.message!!.getAllEntity(MessageEntityType.PHONE_NUMBER))
            .hasSize(1).first().isEqualTo("+1-212-555-0123")
    }

    @Test
    fun updateTypesAsParameter() {
        val param = listOf(UpdateType.MESSAGE, UpdateType.CALLBACK_QUERY).asParameter()

        assertThat(param).isEqualTo("[\"message\",\"callback_query\"]")
    }
}