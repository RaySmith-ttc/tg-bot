package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.TransactionPartnerOtherSerializer

/**Describes a transaction with an unknown source or recipient. */
@Serializable(with = TransactionPartnerOtherSerializer::class)
data object TransactionPartnerOther : TransactionPartner() {

    /** Type of the transaction partner, always “other” */
    override val type: String = "other"
}