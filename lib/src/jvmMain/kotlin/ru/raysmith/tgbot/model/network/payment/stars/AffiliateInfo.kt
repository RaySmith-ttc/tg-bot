package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat

/** Contains information about the affiliate that received a commission via this transaction. */
@Serializable
data class AffiliateInfo(

    /** The bot or the user that received an affiliate commission if it was received by a bot or a user */
    @SerialName("affiliate_user")
    val affiliateUser: User? = null,

    /** The chat that received an affiliate commission if it was received by a chat */
    @SerialName("affiliate_chat")
    val affiliateChat: Chat? = null,

    /**
     * The number of Telegram Stars received by the affiliate for each 1000 Telegram Stars received by the bot from
     * referred users
     * */
    @SerialName("commission_per_mille")
    val commissionPerMille: Int,


    /**
     * Integer amount of Telegram Stars received by the affiliate from the transaction, rounded to 0;
     * can be negative for refunds
     * */
    @SerialName("amount")
    val amount: Int,

    /**
     * The number of 1/1000000000 shares of Telegram Stars received by the affiliate; from -999999999 to 999999999;
     * can be negative for refunds
     * */
    @SerialName("nanostar_amount")
    val nanostarAmount: Int? = null
)