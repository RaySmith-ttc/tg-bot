package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.PollType

/** This object contains information about a poll. */
@Serializable
data class Poll(

    /** Unique poll identifier */
    val id: String,

    /** Poll question, 1-300 characters */
    val question: String,

    /** List of poll options */
    val options: List<PollOption>,

    /** Total number of users that voted in the poll */
    @SerialName("total_voter_count") val totalVoterCount: Int,

    /** _True_, if the poll is closed */
    @SerialName("is_closed") val isClosed: Boolean,

    /** _True_, if the poll is anonymous */
    @SerialName("is_anonymous") val isAnonymous: Boolean,

    /** Poll type, currently can be "regular" or "quiz" */
    @SerialName("type") val type: PollType,

    /** _True_, if the poll allows multiple answers */
    @SerialName("allows_multiple_answers") val allowsMultipleAnswers: Boolean,

    /**
     * 0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are closed,
     * or was sent (not forwarded) by the bot or to the private chat with the bot.
     * */
    @SerialName("correct_option_id") val correctOptionId: Int? = null,

    /**
     * Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll,
     * 0-200 characters
     * */
    @SerialName("explanation") val explanation: String? = null,

    /** Special entities like usernames, URLs, bot commands, etc. that appear in the explanation */
    @SerialName("explanation_entities") val explanationEntities: List<MessageEntity>? = null,

    /** Amount of time in seconds the poll will be active after creation */
    @SerialName("open_period") val openPeriod: Int? = null,

    /** Point in time (Unix timestamp) when the poll will be automatically closed */
    @SerialName("close_date") val closeDate: Int? = null,
)


