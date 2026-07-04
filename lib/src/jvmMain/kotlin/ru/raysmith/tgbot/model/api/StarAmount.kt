package ru.raysmith.tgbot.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes an amount of Telegram Stars. */
@Serializable
data class StarAmount(

    /** Integer amount of Telegram Stars, rounded to 0; can be negative */
    @SerialName("amount") val amount: Int,

    /**
     * The number of 1/1000000000 shares of Telegram Stars; from -999999999 to 999999999;
     * can be negative if and only if amount is non-positive
     * */
    @SerialName("nanostar_amount") val nanostarAmount: Int? = null,
)
