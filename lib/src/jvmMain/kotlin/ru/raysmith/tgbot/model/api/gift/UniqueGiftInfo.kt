package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.Currency

/** Describes a service message about a unique gift that was sent or received. */
@Serializable
data class UniqueGiftInfo(

    /** Information about the gift */
    @SerialName("gift") val gift: UniqueGift,

    /** Origin of the gift */
    @SerialName("origin") val origin: UniqueGiftInfoOrigin,

    /**
     * For gifts bought from other users, the currency in which the payment for the gift was done.
     * Currently, one of [Currency.XTR] for Telegram Stars or [Currency.TON] for toncoins.
     * */
    @SerialName("last_resale_currency") val lastResaleCurrency: Currency? = null,

    /** For gifts bought from other users, the price paid for the gift in either Telegram Stars or nanotoncoins */
    @SerialName("last_resale_amount") val lastResaleAmount: Int? = null,

    /**
     * Unique identifier of the received gift for the bot;
     * only present for gifts received on behalf of business accounts
     * */
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,

    /** Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift */
    @SerialName("transfer_star_count") val transferStarCount: Int? = null,

    /**
     * Point in time (Unix timestamp) when the gift can be transferred.
     * If it is in the past, then the gift can be transferred now.
     * */
    @SerialName("next_transfer_date") val nextTransferDate: Int? = null,
)
