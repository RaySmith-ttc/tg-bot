package ru.raysmith.tgbot.model.network.bisiness

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.sticker.Sticker

/** Contains information about the start page settings of a Telegram Business account. */
@Serializable
data class BusinessIntro(

    /** Title text of the business intro */
    @SerialName("title") val title: String? = null,

    /** Message text of the business intro */
    @SerialName("message") val message: String? = null,

    /** Sticker of the business intro */
    @SerialName("sticker") val sticker: Sticker? = null,
)