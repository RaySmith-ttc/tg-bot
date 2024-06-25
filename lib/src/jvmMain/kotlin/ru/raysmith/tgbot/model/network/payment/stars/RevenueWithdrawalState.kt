package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.RevenueWithdrawalStateSerializer

/**
 * This object describes the state of a revenue withdrawal operation. Currently, it can be one of
 *
 * - [RevenueWithdrawalStatePending]
 * - [RevenueWithdrawalStateSucceeded]
 * - [RevenueWithdrawalStateFailed]
 * */
@Serializable(with = RevenueWithdrawalStateSerializer::class)
sealed class RevenueWithdrawalState {

    /** Type of the state */
    abstract val type: String
}