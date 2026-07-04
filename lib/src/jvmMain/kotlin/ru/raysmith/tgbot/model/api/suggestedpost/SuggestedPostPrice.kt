package ru.raysmith.tgbot.model.api.suggestedpost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.Currency

/** Describes the price of a suggested post. */
@Serializable
data class SuggestedPostPrice(

    /** Currency in which the post will be paid. Currently, must be one of [Currency.XTR] or [Currency.TON]. */
    @SerialName("currency") val currency: Currency,

    /**
     * The amount of the currency that will be paid for the post in the smallest units of the currency,
     * i.e. Telegram Stars or nanotoncoins. Currently, price in Telegram Stars must be between 5 and 100000,
     * and price in nanotoncoins must be between 10000000 and 10000000000000.
     * */
    @SerialName("amount") val amount: Long,
)
