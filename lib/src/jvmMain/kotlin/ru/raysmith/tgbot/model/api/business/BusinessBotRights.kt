package ru.raysmith.tgbot.model.api.business

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the rights of a business bot. */
@Serializable
data class BusinessBotRights(

    /**
     * *True*, if the bot can send and edit messages in the private chats that had incoming messages in the last 24
     * hours
     * */
    @SerialName("can_reply") val canReply: Boolean? = null,

    /** *True*, if the bot can mark incoming private messages as read */
    @SerialName("can_read_messages") val canReadMessages: Boolean? = null,

    /** *True*, if the bot can delete messages sent by the bot */
    @SerialName("can_delete_sent_messages") val canDeleteSentMessages: Boolean? = null,

    /** *True*, if the bot can delete all private messages in managed chats */
    @SerialName("can_delete_all_messages") val canDeleteAllMessages: Boolean? = null,

    /** *True*, if the bot can edit the first and last name of the business account */
    @SerialName("can_edit_name") val canEditName: Boolean? = null,

    /** *True*, if the bot can edit the bio of the business account */
    @SerialName("can_edit_bio") val canEditBio: Boolean? = null,

    /** *True*, if the bot can edit the profile photo of the business account */
    @SerialName("can_edit_profile_photo") val canEditProfilePhoto: Boolean? = null,

    /** *True*, if the bot can edit the username of the business account */
    @SerialName("can_edit_username") val canEditUsername: Boolean? = null,

    /** *True*, if the bot can change the privacy settings pertaining to gifts for the business account */
    @SerialName("can_change_gift_settings") val canChangeGiftSettings: Boolean? = null,

    /** *True*, if the bot can view gifts and the amount of Telegram Stars owned by the business account */
    @SerialName("can_view_gifts_and_stars") val canViewGiftsAndStars: Boolean? = null,

    /** *True*, if the bot can convert regular gifts owned by the business account to Telegram Stars */
    @SerialName("can_convert_gifts_to_stars") val canConvertGiftsToStars: Boolean? = null,

    /** *True*, if the bot can transfer and upgrade gifts owned by the business account */
    @SerialName("can_transfer_and_upgrade_gifts") val canTransferAndUpgradeGifts: Boolean? = null,

    /**
     * *True*, if the bot can transfer Telegram Stars received by the business account to its own account, or use them
     * to upgrade and transfer gifts
     * */
    @SerialName("can_transfer_stars") val canTransferStars: Boolean? = null,

    /** *True*, if the bot can post, edit and delete stories on behalf of the business account */
    @SerialName("can_manage_stories") val canManageStories: Boolean? = null,
)
