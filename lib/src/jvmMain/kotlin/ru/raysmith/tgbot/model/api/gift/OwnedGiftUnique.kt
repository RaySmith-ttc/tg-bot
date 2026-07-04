package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** Describes a unique gift received and owned by a user or a chat. */
@Serializable
data class OwnedGiftUnique(

    /** Information about the unique gift */
    @SerialName("gift") val gift: UniqueGift,

    /** Unique identifier of the received gift for the bot; for gifts received on behalf of business accounts only */
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,

    /** Sender of the gift if it is a known user */
    @SerialName("sender_user") val senderUser: User? = null,

    /** Date the gift was sent in Unix time */
    @SerialName("send_date") val sendDate: Int,

    /**
     * *True*, if the gift is displayed on the account's profile page;
     * for gifts received on behalf of business accounts only
     * */
    @SerialName("is_saved") val isSaved: Boolean? = null,

    /**
     * *True*, if the gift can be transferred to another owner; for gifts received on behalf of business accounts only
     * */
    @SerialName("can_be_transferred") val canBeTransferred: Boolean? = null,

    /** Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift */
    @SerialName("transfer_star_count") val transferStarCount: Int? = null,

    /**
     * Point in time (Unix timestamp) when the gift can be transferred.
     * If it is in the past, then the gift can be transferred now.
     * */
    @SerialName("next_transfer_date") val nextTransferDate: Int? = null,
) : OwnedGift {

    /** Type of the gift, always “unique” */
    override val type: String = "unique"
}
