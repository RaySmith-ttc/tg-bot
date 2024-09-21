package ru.raysmith.tgbot.model.bot.message.poll

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.ExtendedMessage
import ru.raysmith.tgbot.model.bot.message.TextWithEntities
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.PollType
import ru.raysmith.tgbot.model.network.message.ReplyParameters

internal class PollOptionTextWithEntities(bot: Bot) : TextWithEntities(bot, MessageTextType.POLL_OPTION) {
    fun getMessageTextExposed() = getMessageText()
    fun getParseModeIfNeedExposed() = getParseModeIfNeed()
    fun getEntitiesExposed() = getEntities()
}

/**
 * Use this class to configure and [send] a native poll.
 *
 * @param bot Bot for which sending is performed
 * */
class PollMessage(
    override val bot: Bot,
) : IMessage<Message>, KeyboardCreator, BotHolder, ExtendedMessage<Message> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
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

    private val tweExplanation =  object : TextWithEntities(bot, MessageTextType.POLL_EXPLANATION) {
        fun getMessageTextExposed() = getMessageText()
        fun getParseModeIfNeedExposed() = getParseModeIfNeed()
        fun getEntitiesExposed() = getEntities()
    }

    /** Poll question, 1-300 characters */
    suspend fun explanation(block: suspend TextWithEntities.() -> Unit) {
        tweExplanation.apply { block() }
    }

    private val tweQuestion = object : TextWithEntities(bot, MessageTextType.POLL_QUESTION) {
        fun getMessageTextExposed() = getMessageText()
        fun getParseModeIfNeedExposed() = getParseModeIfNeed()
        fun getEntitiesExposed() = getEntities()
    }

    /** Poll question, 1-300 characters */
    suspend fun question(block: suspend TextWithEntities.() -> Unit) {
        tweQuestion.apply { block() }
    }

    private val options = mutableListOf<PollOptionTextWithEntities>()

    /** Poll question, 1-300 characters */
    suspend fun option(block: suspend TextWithEntities.() -> Unit) {
        options.add(PollOptionTextWithEntities(bot).apply { block() })
    }

    /** Use this method to send a native poll. On success, the sent Message is returned. */
    override suspend fun send(chatId: ChatId) = sendPoll(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        question = tweQuestion.getMessageTextExposed(),
        questionParseMode = tweQuestion.getParseModeIfNeedExposed(),
        questionEntities = tweQuestion.getEntitiesExposed(),
        options = options.map {
            InputPollOption(it.getMessageTextExposed(), it.getParseModeIfNeedExposed(), it.getEntitiesExposed())
        },
        isAnonymous = isAnonymous,
        type = type,
        allowsMultipleAnswers = allowsMultipleAnswers,
        correctOptionId = correctOptionId,
        explanation = tweExplanation.getMessageTextExposed(),
        explanationParseMode = tweExplanation.getParseModeIfNeedExposed(),
        explanationEntities = tweExplanation.getEntitiesExposed(),
        openPeriod = openPeriod,
        closeDate = closeDate,
        isClosed = isClosed,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}