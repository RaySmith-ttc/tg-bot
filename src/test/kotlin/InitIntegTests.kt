import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatType
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.updates.Update
import java.time.Instant

open class BotTests {
    fun getPrivateChat(id: Long = 1) = Chat(
        id, ChatType.PRIVATE
    )

    fun getDate() = Instant.now().epochSecond.toInt()
}

class InitIntegTests : BotTests() {

    @Test
    fun errorWhileOnError() = runBlocking {
        val bot = Bot(token = "bot123").apply {
            onError {
//                throw Exception("from onError")
            }
            start {
                handleMessage {
//                    throw Exception("from handleMessage")
                }
            }
        }

        bot.newUpdate(Update(1, message = Message(1, chat = getPrivateChat(), date = getDate())))
        bot.stop()
    }

}