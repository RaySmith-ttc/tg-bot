package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.message.MessageEntity

/** Describes a regular gift owned by a user or a chat. */
@Serializable
data class OwnedGiftRegular(

    /** Information about the regular gift */
    @SerialName("gift") val gift: Gift,

    /** Unique identifier of the gift for the bot; for gifts received on behalf of business accounts only */
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,

    /** Sender of the gift if it is a known user */
    @SerialName("sender_user") val senderUser: User? = null,

    /** Date the gift was sent in Unix time */
    @SerialName("send_date") val sendDate: Int,

    /** Text of the message that was added to the gift */
    @SerialName("text") val text: String? = null,

    /** Special entities that appear in the text */
    @SerialName("entities") val entities: List<MessageEntity>? = null,

    /**
     * *True*, if the sender and gift text are shown only to the gift receiver;
     * otherwise, everyone will be able to see them
     * */
    @SerialName("is_private") val isPrivate: Boolean? = null,

    /**
     * *True*, if the gift is displayed on the account's profile page;
     * for gifts received on behalf of business accounts only
     * */
    @SerialName("is_saved") val isSaved: Boolean? = null,

    /** *True*, if the gift can be upgraded to a unique gift; for gifts received on behalf of business accounts only */
    @SerialName("can_be_upgraded") val canBeUpgraded: Boolean? = null,

    /** *True*, if the gift was refunded and isn't available anymore */
    @SerialName("was_refunded") val wasRefunded: Boolean? = null,

    /**
     * Number of Telegram Stars that can be claimed by the receiver instead of the gift;
     * omitted if the gift cannot be converted to Telegram Stars; for gifts received on behalf of business accounts only
     * */
    @SerialName("convert_star_count") val convertStarCount: Int? = null,

    /** Number of Telegram Stars that were paid for the ability to upgrade the gift */
    @SerialName("prepaid_upgrade_star_count") val prepaidUpgradeStarCount: Int? = null,

    /**
     * *True*, if the gift's upgrade was purchased after the gift was sent;
     * for gifts received on behalf of business accounts only
     * */
    @SerialName("is_upgrade_separate") val isUpgradeSeparate: Boolean? = null,

    /** Unique number reserved for this gift when upgraded. See the number field in [UniqueGift]. */
    @SerialName("unique_gift_number") val uniqueGiftNumber: Int? = null,
) : OwnedGift {

    /** Type of the gift, always “regular” */
    @EncodeDefault
    @SerialName("type") override val type: String = "regular"
}
