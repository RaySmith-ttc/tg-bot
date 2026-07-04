package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.payment.stars.RevenueWithdrawalState
import ru.raysmith.tgbot.model.api.payment.stars.RevenueWithdrawalStateFailed
import ru.raysmith.tgbot.model.api.payment.stars.RevenueWithdrawalStatePending
import ru.raysmith.tgbot.model.api.payment.stars.RevenueWithdrawalStateSucceeded

internal object RevenueWithdrawalStateSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<RevenueWithdrawalState>(RevenueWithdrawalState::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "pending" -> RevenueWithdrawalStatePending.serializer()
        "succeeded" -> RevenueWithdrawalStateSucceeded.serializer()
        "failed" -> RevenueWithdrawalStateFailed.serializer()
        else -> null
    }
}
