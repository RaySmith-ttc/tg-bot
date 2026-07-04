package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.api.StarAmount
import ru.raysmith.tgbot.model.api.message.Message

/** Describes a service message about a successful payment for a suggested post. */
@Serializable
data class SuggestedPostPaid(

    /**
     * Message containing the suggested post. Note that the [Message] object in this field will not contain the
     * [replyToMessage][Message.replyToMessage] field even if it itself is a reply.
     * */
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null,

    /** Currency in which the payment was made. Currently, one of [Currency.XTR] or [Currency.TON]. */
    @SerialName("currency") val currency: Currency,

    /** The amount of the currency that was received by the channel in nanotoncoins; for payments in toncoins only */
    @SerialName("amount") val amount: Int? = null,

    /** The amount of Telegram Stars that was received by the channel; for payments in Telegram Stars only */
    @SerialName("star_amount") val starAmount: StarAmount? = null,
)
