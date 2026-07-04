package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.sticker.Sticker

/** This object describes the symbol shown on the pattern of a unique gift. */
@Serializable
data class UniqueGiftSymbol(

    /** Name of the symbol */
    @SerialName("name") val name: String,

    /** The sticker that represents the unique gift */
    @SerialName("sticker") val sticker: Sticker,

    /** The number of unique gifts that receive this model for every 1000 gifts upgraded */
    @SerialName("rarity_per_mille") val rarityPerMille: Int,
)
