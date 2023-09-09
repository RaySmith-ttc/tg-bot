package ru.raysmith.tgbot.model.network.chat.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.EnumFallbackSerializer

@Serializable(with = IconColorSerializer::class)
enum class IconColor(val value: Int) {
    @SerialName("7322096") BLUE(0x6FB9F0),
    @SerialName("16766590") YELLOW(0xFFD67E),
    @SerialName("13338331") PURPLE(0xCB86DB),
    @SerialName("9367192") GREEN(0x8EEE98),
    @SerialName("16749490") PINK(0xFF93B2),
    @SerialName("16478047") RED(0xFB6F5F),

    /** Fallback for compatability with new versions */
    UNKNOWN(0)
}

internal object IconColorSerializer : EnumFallbackSerializer<IconColor>("IconColor", IconColor.entries, IconColor.UNKNOWN)