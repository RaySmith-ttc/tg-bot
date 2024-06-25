package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.payment.SuccessfulPayment

/** Describes a Telegram Star transaction. */
@Serializable
data class StarTransaction(

    /**
     * Unique identifier of the transaction. Coincides with the identifer of the original transaction for
     * refund transactions. Coincides with [SuccessfulPayment.telegramPaymentChargeId] for successful incoming
     * payments from users.
     * */
    @SerialName("id") val id: String,

    /** Number of Telegram Stars transferred by the transaction */
    @SerialName("amount") val amount: Int,

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

