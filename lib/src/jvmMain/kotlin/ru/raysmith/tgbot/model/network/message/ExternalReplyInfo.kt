package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.Location
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.model.network.Venue
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.giveaway.Giveaway
import ru.raysmith.tgbot.model.network.giveaway.GiveawayWinners
import ru.raysmith.tgbot.model.network.media.*
import ru.raysmith.tgbot.model.network.media.paid.PaidMediaInfo
import ru.raysmith.tgbot.model.network.message.origin.MessageOrigin
import ru.raysmith.tgbot.model.network.sticker.Sticker

/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum
 * topic.
 * */
@Serializable
data class ExternalReplyInfo(

    /** Origin of the message replied to by the given message */
    @SerialName("origin") val origin: MessageOrigin,

    /** Chat the original message belongs to. Available only if the chat is a supergroup or a channel. */
    @SerialName("chat") val chat: Chat? = null,

    /**
     * Unique message identifier inside the original chat. Available only if the original chat is a supergroup or
     * a channel.
     * */
    @SerialName("message_id") val messageId: Int? = null,

    /** Options used for link preview generation for the original message, if it is a text message */
    @SerialName("link_preview_options") val linkPreviewOptions: LinkPreviewOptions? = null,

    /** Message is an animation, information about the animation */
    @SerialName("animation") val animation: Animation? = null,

    /** Message is an audio file, information about the file */
    @SerialName("audio") val audio: Audio? = null,

    /** Message is a general file, information about the file */
    @SerialName("document") val document: Document? = null,

    /** Message contains paid media; information about the paid media */
    @SerialName("paid_media") val paidMedia: PaidMediaInfo? = null,

    /** Message is a photo, available sizes of the photo */
    @SerialName("photo") val photo: List<PhotoSize>? = null,

    /** Message is a sticker, information about the sticker */
    @SerialName("sticker") val sticker: Sticker? = null,

    /** Message is a forwarded story */
    @SerialName("story") val story: Story? = null,

    /** Message is a video, information about the video */
    @SerialName("video") val video: Video? = null,

    /**
     * Message is a [video note](https://telegram.org/blog/video-messages-and-telescope),
     * information about the video message
     * */
    @SerialName("video_note") val videoNote: VideoNote? = null,

    /** Message is a voice message, information about the file */
    @SerialName("voice") val voice: Voice? = null,

    /** *True*, if the message media is covered by a spoiler animation */
    @SerialName("has_media_spoiler") val hasMediaSpoiler: Boolean? = null,

    /** Message is a shared contact, information about the contact */
    @SerialName("contact") val contact: Contact? = null,

    /** Message is a dice with random value */
    @SerialName("dice") val dice: Dice? = null,

    // TODO [game support]
//    /** Message is a game, information about the game. [More about games »](https://core.telegram.org/bots/api#games) */
//    @SerialName("game) val game: Game? = null,

    /** Message is a scheduled giveaway, information about the giveaway */
    @SerialName("giveaway") val giveaway: Giveaway? = null,

    /** A giveaway with public winners was completed */
    @SerialName("giveaway_winners") val giveawayWinners: GiveawayWinners? = null,

    /**
     * Message is an invoice for a payment, information about the invoice.
     * [More about payments »](https://core.telegram.org/bots/api#payments)
     * */
    @SerialName("invoice") val invoice: Invoice? = null,

    /** Message is a shared location, information about the location */
    @SerialName("location") val location: Location? = null,

    /** Message is a native poll, information about the poll */
    @SerialName("poll") val poll: Poll? = null,

    /** Message is a venue, information about the venue */
    @SerialName("venue") val venue: Venue? = null,
)
