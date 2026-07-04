package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.TransactionPartnerSerializer

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions.
 * Currently, it can be one of
 *
 * - [TransactionPartnerUser]
 * - [TransactionPartnerChat]
 * - [TransactionPartnerAffiliateProgram]
 * - [TransactionPartnerFragment]
 * - [TransactionPartnerTelegramAds]
 * - [TransactionPartnerTelegramApi]
 * - [TransactionPartnerOther]
 * */
@Serializable(TransactionPartnerSerializer::class)
sealed interface TransactionPartner {

    /** Type of the transaction partner */
    val type: String
}

