package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat

/** This object describes a unique gift that was upgraded from a regular gift. */
@Serializable
data class UniqueGift(

    /** Identifier of the regular gift from which the gift was upgraded */
    @SerialName("gift_id") val giftId: String,

    /** Human-readable name of the regular gift from which this unique gift was upgraded */
    @SerialName("base_name") val baseName: String,

    /** Unique name of the gift. This name can be used in `https://t.me/nft/...` links and story areas. */
    @SerialName("name") val name: String,

    /** Unique number of the upgraded gift among gifts upgraded from the same regular gift */
    @SerialName("number") val number: Int,

    /** Model of the gift */
    @SerialName("model") val model: UniqueGiftModel,

    /** Symbol of the gift */
    @SerialName("symbol") val symbol: UniqueGiftSymbol,

    /** Backdrop of the gift */
    @SerialName("backdrop") val backdrop: UniqueGiftBackdrop,

    /** *True*, if the original regular gift was exclusively purchaseable by Telegram Premium subscribers */
    @SerialName("is_premium") val isPremium: Boolean? = null,

    /** *True*, if the gift was used to craft another gift and isn't available anymore */
    @SerialName("is_burned") val isBurned: Boolean? = null,

    /** *True*, if the gift is assigned from the TON blockchain and can't be resold or transferred in Telegram */
    @SerialName("is_from_blockchain") val isFromBlockchain: Boolean? = null,

    /**
     * The color scheme that can be used by the gift's owner for the chat's name, replies to messages and link previews;
     * for business account gifts and gifts that are currently on sale only
     * */
    @SerialName("colors") val colors: UniqueGiftColors? = null,

    /** Information about the chat that published the gift */
    @SerialName("publisher_chat") val publisherChat: Chat? = null,
)
