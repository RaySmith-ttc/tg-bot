package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable

/**Describes a transaction with an unknown source or recipient. */
@Serializable
data object TransactionPartnerOther : TransactionPartner() {

    /** Type of the transaction partner, always “other” */
    override val type: String = "other"
}