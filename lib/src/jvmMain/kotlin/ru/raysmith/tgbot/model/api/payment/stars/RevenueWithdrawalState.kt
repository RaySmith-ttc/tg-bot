package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.RevenueWithdrawalStateSerializer

/**
 * This object describes the state of a revenue withdrawal operation. Currently, it can be one of
 *
 * - [RevenueWithdrawalStatePending]
 * - [RevenueWithdrawalStateSucceeded]
 * - [RevenueWithdrawalStateFailed]
 * */
@Serializable(RevenueWithdrawalStateSerializer::class)
sealed interface RevenueWithdrawalState {

    /** Type of the state */
    val type: String
}
