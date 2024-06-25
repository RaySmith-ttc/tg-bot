package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Contains a list of Telegram Star transactions. */
@Serializable
data class StarTransactions(

    /** The list of transactions */
    @SerialName("transactions") val transactions: List<StarTransaction>
)