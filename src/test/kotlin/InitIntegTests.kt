import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatType
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
                Assertions.assertThat(it.message).isEqualTo("Not Found")
            }

//            launch {
//                start {
//                    handleMessage {
//                        throw Exception("Shouldn't reach")
//                    }
//                }
//            }
        }

//            bot.newUpdate(Update(1, message = Message(1, chat = getPrivateChat(), date = getDate())))

        println("asdasd")
        delay(100)
        bot.stop()
//        runBlocking {
//            delay(1000)
//        }
    }

}