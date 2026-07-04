package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.sticker.Sticker

/** This object describes the model of a unique gift. */
@Serializable
data class UniqueGiftModel(

    /** Name of the model */
    @SerialName("name") val name: String,

    /** The sticker that represents the unique gift */
    @SerialName("sticker") val sticker: Sticker,

    /** The number of unique gifts that receive this model for every 1000 gift upgrades. Always 0 for crafted gifts. */
    @SerialName("rarity_per_mille") val rarityPerMille: Int,

    /** Rarity of the model if it is a crafted model */
    @SerialName("rarity") val rarity: UniqueGiftModelRarity? = null,
)
