package ru.raysmith.tgbot.model.bot.message

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.model.network.message.PollType
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.withSafeLength

class PollMessage(
    val question: String, val options: List<String>,
    override val service: TelegramService,
    override val fileService: TelegramFileService
) : IMessage<MessageResponse>, KeyboardCreator {
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var protectContent: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    var isAnonymous: Boolean? = null
    var type: PollType? = null
    var allowsMultipleAnswers: Boolean? = null
    var correctOptionId: Int? = null
    var explanationParseMode: String? = null
    var explanationEntities: String? = null
    var openPeriod: Int? = null
    var closeDate: Int? = null
    var isClosed: Boolean? = null

    private var _explanation: MessageText? = null

    /** Simple caption text to send the message */
    var explanation: String? = null

    fun hasExplanation() = explanation != null || _explanation != null

    /** Whether test should be truncated if caption length is greater than 200 */
    var safeTextLength: Boolean = Bot.config.safeTextLength

    /**
     * Sets a explanation as [MessageText] object
     * */
    fun explanationWithEntities(setText: MessageText.() -> Unit) {
        _explanation = MessageText(MessageTextType.CAPTION).apply(setText)
    }

    fun getExplanationText(): String? =
        _explanation?.getTextString()
            ?: explanation?.let { if (safeTextLength) it.withSafeLength(MessageTextType.POLL_EXPLANATION) else it }

    override fun send(chatId: ChatId): MessageResponse {
        return service.sendPoll(
            chatId = chatId,
            question = question,
            options = Json.encodeToString(options),
            isAnonymous = isAnonymous,
            type = type,
            allowsMultipleAnswers = allowsMultipleAnswers,
            correctOptionId = correctOptionId,
            explanation = getExplanationText(),
            explanationParseMode = explanationParseMode,
            explanationEntities = _explanation?.getEntitiesString(),
            openPeriod = openPeriod,
            closeDate = closeDate,
            isClosed = isClosed,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }
}