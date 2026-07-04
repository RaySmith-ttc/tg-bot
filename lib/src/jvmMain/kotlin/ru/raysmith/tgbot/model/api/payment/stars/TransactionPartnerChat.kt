package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.api.gift.Gift

/** Describes a transaction with a chat. */
@Serializable
data class TransactionPartnerChat(

    /** Information about the chat */
    @SerialName("chat") val chat: Chat,

    /** Optional. The gift sent to the chat by the bot */
    @SerialName("gift") val gift: Gift? = null,
) : TransactionPartner {

    /** Type of the transaction partner, always “chat” */
    @EncodeDefault
    @SerialName("type") override val type: String = "chat"
}
