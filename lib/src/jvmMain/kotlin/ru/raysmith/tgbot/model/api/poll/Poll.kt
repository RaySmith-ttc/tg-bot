package ru.raysmith.tgbot.model.api.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.PollType

/** This object contains information about a poll. */
@Serializable
data class Poll(

    /** Unique poll identifier */
    @SerialName("id") val id: String,

    /** Poll question, 1-300 characters */
    @SerialName("question") val question: String,

    /**
     * Special entities that appear in the question. Currently, only custom emoji entities are allowed in poll questions
     * */
    @SerialName("question_entities") val questionEntities: List<MessageEntity>? = null,

    /** List of poll options */
    @SerialName("options") val options: List<PollOption>,

    /** Total number of users that voted in the poll */
    @SerialName("total_voter_count") val totalVoterCount: Int,

    /** *True*, if the poll is closed */
    @SerialName("is_closed") val isClosed: Boolean,

    /** *True*, if the poll is anonymous */
    @SerialName("is_anonymous") val isAnonymous: Boolean,

    /** Poll type, currently can be "regular" or "quiz" */
    @SerialName("type") val type: PollType,

    /** *True*, if the poll allows multiple answers */
    @SerialName("allows_multiple_answers") val allowsMultipleAnswers: Boolean,

    /** *True*, if the poll allows to change the chosen answer options */
    @SerialName("allows_revoting") val allowsRevoting: Boolean,

    /**
     * *True*, if voting is limited to users who have been members of the chat where the poll was originally sent
     * for more than 24 hours
     * */
    @SerialName("members_only") val membersOnly: Boolean,

    /**
     * A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes
     * indicating the countries from which users can vote in the poll. The country code “FT” is used for users with
     * anonymous numbers. If omitted, then users from any country can participate in the poll.
     * */
    @SerialName("country_codes") val countryCodes: List<String>,

    /**
     * Array of 0-based identifiers of the correct answer options. Available only for polls in quiz mode which are
     * closed or were sent (not forwarded) by the bot or to the private chat with the bot.
     * */
    @SerialName("correct_option_ids") val correctOptionIds: List<Int>? = null,

    /**
     * Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll,
     * 0-200 characters
     * */
    @SerialName("explanation") val explanation: String? = null,

    /** Special entities like usernames, URLs, bot commands, etc. that appear in the explanation */
    @SerialName("explanation_entities") val explanationEntities: List<MessageEntity>? = null,

    /** Media added to the quiz explanation */
    @SerialName("explanation_media") val explanationMedia: PollMedia? = null,

    /** Amount of time in seconds the poll will be active after creation */
    @SerialName("open_period") val openPeriod: Int? = null,

    /** Point in time (Unix timestamp) when the poll will be automatically closed */
    @SerialName("close_date") val closeDate: Int? = null,

    /** Description of the poll; for polls inside the [Message] object only */
    @SerialName("description") val description: String? = null,

    /** Special entities like usernames, URLs, bot commands, etc. that appear in the description */
    @SerialName("description_entities") val descriptionEntities: List<MessageEntity>? = null,

    /** Media added to the poll description; for polls inside the [Message] object only */
    @SerialName("media") val media: PollMedia? = null,
)


