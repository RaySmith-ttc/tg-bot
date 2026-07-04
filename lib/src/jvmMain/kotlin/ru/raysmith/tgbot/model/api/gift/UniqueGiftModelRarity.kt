package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UniqueGiftModelRarity {

    @SerialName("uncommon")
    UNCOMMON,

    @SerialName("rare")
    RARE,

    @SerialName("epic")
    EPIC,

    @SerialName("legendary")
    LEGENDARY,
}
