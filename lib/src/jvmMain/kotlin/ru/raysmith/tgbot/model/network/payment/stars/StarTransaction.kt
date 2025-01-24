package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.payment.SuccessfulPayment

/**
 * Describes a Telegram Star transaction. Note that if the buyer initiates a chargeback with the payment provider from
 * whom they acquired Stars (e.g., Apple, Google) following this transaction, the refunded Stars will be deducted from
 * the bot's balance. This is outside of Telegram's control.
 *
 * */
@Serializable
data class StarTransaction(

    /**
     * Unique identifier of the transaction. Coincides with the identifer of the original transaction for
     * refund transactions. Coincides with [SuccessfulPayment.telegramPaymentChargeId] for successful incoming
     * payments from users.
     * */
    @SerialName("id") val id: String,

    /** Integer amount of Telegram Stars transferred by the transaction */
    @SerialName("amount") val amount: Int,

    /** The number of 1/1000000000 shares of Telegram Stars transferred by the transaction; from 0 to 999999999 */
    @SerialName("nanostar_amount") val nanostarAmount: Int? = null,

    /** Date the transaction was created in Unix time */
    @SerialName("date") val date: Int,

    /**
     * Source of an incoming transaction (e.g., a user purchasing goods or services,
     * Fragment refunding a failed withdrawal). Only for incoming transactions
     * */
    @SerialName("source") val source: TransactionPartner? = null,


    /**
     * Receiver of an outgoing transaction (e.g., a user for a purchase refund,
     * Fragment for a withdrawal). Only for outgoing transactions
     * */
    @SerialName("receiver") val receiver: TransactionPartner? = null,
)

