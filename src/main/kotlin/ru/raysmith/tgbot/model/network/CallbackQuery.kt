package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.exceptions.UnknownChatIdException
import ru.raysmith.tgbot.model.network.message.Message

@Serializable
/**
 * This object represents an incoming callback query from a callback button in an inline keyboard.
 * If the button that originated the query was attached to a message sent by the bot, the field message will be present.
 * If the button was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present.
 * Exactly one of the fields data or game_short_name will be present.
 *
 * NOTE: After the user presses a callback button, Telegram clients will display a progress bar until you call answerCallbackQuery.
 * It is, therefore, necessary to react by calling answerCallbackQuery even if no notification to the user is needed (e.g., without specifying any of the optional parameters).
 *
 * @see <a href="https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating">inline keyboard</a>
 * @see <a href="https://core.telegram.org/bots/api#inline-mode">inline mode</a>
 * @see <a href="https://core.telegram.org/bots/api#answercallbackquery">answerCallbackQuery</a> TODO link to answer method
 *
 * */
data class CallbackQuery(
    /** Unique identifier for this query */
    @SerialName("id") val id: String,

    /** Sender */
    @SerialName("from") val from: User,

    /** Message with the callback button that originated the query. Note that message content and message date will not be available if the message is too old */
    @SerialName("message") val message: Message? = null,

    /** Identifier of the message sent via the bot in inline mode, that originated the query. */
    @SerialName("inline_message_id") val inlineMessageId: String? = null,

    /**
     * Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent. Useful for high scores in games.
     * @see <a href="https://core.telegram.org/bots/api#games">games</>*/
    @SerialName("chat_instance") val chatInstance: String,

    /** Data associated with the callback button. Be aware that a bad client can send arbitrary data in this field. */
    @SerialName("data") val `data`: String? = null,

    /**
     * Short name of a Game to be returned, serves as the unique identifier for the game
     * @see <a href="https://core.telegram.org/bots/api#games">games</>
     * */
    @SerialName("game_short_name") val gameShortName: String? = null,
)  : ChatIdHolder {
    companion object {
        val EMPTY_CALLBACK_DATA = Bot.config.emptyCallbackQuery
    }

    override fun getChatId() = message?.chat?.id
    override fun getChatIdOrThrow() = message?.chat?.id ?: throw UnknownChatIdException()
}