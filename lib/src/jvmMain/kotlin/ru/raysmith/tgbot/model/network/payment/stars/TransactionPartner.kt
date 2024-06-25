package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.TransactionPartnerSerializer

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions.
 * Currently, it can be one of
 *
 * - [TransactionPartnerFragment]
 * - [TransactionPartnerUser]
 * - [TransactionPartnerOther]
 * */
@Serializable(with = TransactionPartnerSerializer::class)
sealed class TransactionPartner {

    /** Type of the transaction partner */
    abstract val type: String
}

