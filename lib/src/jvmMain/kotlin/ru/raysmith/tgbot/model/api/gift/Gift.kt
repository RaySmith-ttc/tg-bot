package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.api.sticker.Sticker

/** This object represents a gift that can be sent by the bot. */
@Serializable
data class Gift(

    /** Unique identifier of the gift */
    @SerialName("id") val id: String,

    /** The sticker that represents the gift */
    @SerialName("sticker") val sticker: Sticker,

    /** The number of Telegram Stars that must be paid to send the sticker */
    @SerialName("star_count") val starCount: Int,

    /** The number of Telegram Stars that must be paid to upgrade the gift to a unique one */
    @SerialName("upgrade_star_count") val upgradeStarCount: Int? = null,

    /** *True*, if the gift can only be purchased by Telegram Premium subscribers */
    @SerialName("is_premium") val isPremium: Boolean? = null,

    /** *True*, if the gift can be used (after being upgraded) to customize a user's appearance */
    @SerialName("has_colors") val hasColors: Boolean? = null,

    /** The total number of gifts of this type that can be sent by all users; for limited gifts only */
    @SerialName("total_count") val totalCount: Int? = null,

    /** The number of remaining gifts of this type that can be sent by all users; for limited gifts only */
    @SerialName("remaining_count") val remainingCount: Int? = null,

    /** The total number of gifts of this type that can be sent by the bot; for limited gifts only */
    @SerialName("personal_total_count") val personalTotalCount: Int? = null,

    /** The number of remaining gifts of this type that can be sent by the bot; for limited gifts only */
    @SerialName("personal_remaining_count") val personalRemainingCount: Int? = null,

    /** Background of the gift */
    @SerialName("background") val background: GiftBackground? = null,

    /** The total number of different unique gifts that can be obtained by upgrading the gift */
    @SerialName("unique_gift_variant_count") val uniqueGiftVariantCount: Int? = null,

    /** Information about the chat that published the gift */
    @SerialName("publisher_chat") val publisherChat: Chat? = null,
)
