package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object describes the backdrop of a unique gift. */
@Serializable
data class UniqueGiftBackdrop(

    /** Name of the backdrop */
    @SerialName("name") val name: String,

    /** Colors of the backdrop */
    @SerialName("colors") val colors: UniqueGiftBackdropColors,

    /** The number of unique gifts that receive this backdrop for every 1000 gifts upgraded */
    @SerialName("rarity_per_mille") val rarityPerMille: Int,
)
