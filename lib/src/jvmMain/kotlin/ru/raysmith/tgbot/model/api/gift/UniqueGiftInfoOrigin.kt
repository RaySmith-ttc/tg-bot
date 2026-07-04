package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UniqueGiftInfoOrigin {

    /** gifts upgraded from regular gifts */
    @SerialName("upgrade")
    UPGRADE,

    /** gifts transferred from other users or channels */
    @SerialName("transfer")
    TRANSFER,

    /** gifts bought from other users */
    @SerialName("resale")
    RESALE,

    /** upgrades purchased after the gift was sent */
    @SerialName("gifted_upgrade")
    GIFTED_UPGRADE,

    /** gifts bought or sold through gift purchase offers */
    @SerialName("offer")
    OFFER,
}
