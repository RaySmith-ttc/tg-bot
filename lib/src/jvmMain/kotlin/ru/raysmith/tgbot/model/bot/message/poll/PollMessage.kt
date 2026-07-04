package ru.raysmith.tgbot.model.bot.message.poll

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.api.media.input.InputPollMedia
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.PollType
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.*
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard

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
) : IMessage<Message>, KeyboardCreator, BotHolder, ThreadMessage, EffectableMessage, BusinessMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var allowPaidBroadcast: Boolean? = null
    override var directMessagesTopicId: Int? = null

    var isAnonymous: Boolean? = null
    var type: PollType? = null
    var allowsMultipleAnswers: Boolean? = null
    var allowsRevoting: Boolean? = null
    var shuffleOptions: Boolean? = null
    var allowAddingOptions: Boolean? = null
    var membersOnly: Boolean? = null
    var countryCodes: List<String>? = null
    var correctOptionIds: List<Int>? = null
    var openPeriod: Int? = null
    var closeDate: Int? = null
    var isClosed: Boolean? = null
    var media: InputPollMedia? = null

    var explanationMedia: InputPollMedia? = null

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

    private val tweDescription = object : TextWithEntities(bot, MessageTextType.POLL_DESCRIPTION) {
        fun getMessageTextExposed() = getMessageText()
        fun getParseModeIfNeedExposed() = getParseModeIfNeed()
        fun getEntitiesExposed() = getEntities()
    }

    /** Description of the poll to be sent, 0-1024 characters after entities parsing */
    suspend fun description(block: suspend TextWithEntities.() -> Unit) {
        tweQuestion.apply { block() }
    }

    private val options = mutableListOf<PollOptionTextWithEntities>()

    /** Poll question, 1-300 characters */
    suspend fun option(block: suspend TextWithEntities.() -> Unit) {
        options.add(PollOptionTextWithEntities(bot).apply { block() })
    }

    /** Use this method to send a native poll. On success, the sent [Message] is returned. */
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
        allowsRevoting = allowsRevoting,
        shuffleOptions = shuffleOptions,
        allowAddingOptions = allowAddingOptions,
        membersOnly = membersOnly,
        countryCodes = countryCodes,
        correctOptionIds = correctOptionIds,
        explanation = tweExplanation.getMessageTextExposed(),
        explanationParseMode = tweExplanation.getParseModeIfNeedExposed(),
        explanationEntities = tweExplanation.getEntitiesExposed(),
        explanationMedia = explanationMedia,
        openPeriod = openPeriod,
        closeDate = closeDate,
        isClosed = isClosed,
        description = tweDescription.getMessageTextExposed(),
        descriptionParseMode = tweDescription.getParseModeIfNeedExposed(),
        descriptionEntities = tweDescription.getEntitiesExposed(),
        media = media,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup(),
    )
}
