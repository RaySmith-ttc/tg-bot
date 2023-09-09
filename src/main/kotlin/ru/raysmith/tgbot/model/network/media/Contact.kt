package ru.raysmith.tgbot.model.network.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId

@Serializable
data class Contact(
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("user_id") val userId: ChatId.ID? = null,
    @SerialName("vcard") val vcard: String? = null,
)