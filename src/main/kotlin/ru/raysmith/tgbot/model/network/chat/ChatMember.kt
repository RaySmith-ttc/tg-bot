package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.network.serializer.ChatMemberSerializer

@Serializable(with = ChatMemberSerializer::class)
sealed class ChatMember {

    @Serializable
    /** Represents a chat member that owns the chat and has all administrator privileges. */
    data class ChatMemberOwner(
        /** The member's status in the chat, always “creator” */
        @SerialName("status") val status: String,

        /** Information about the user */
        @SerialName("user") val user: User,

        /** True, if the user's presence in the chat is hidden */
        @SerialName("is_anonymous") val isAnonymous: Boolean,

        /** Custom title for this user */
        @SerialName("custom_title") val customTitle: String? = null,
    ) : ChatMember()

    @Serializable
    /** Represents a chat member that has some additional privileges. */
    data class ChatMemberAdministrator(
        /** The member's status in the chat, always “administrator” */
        @SerialName("status") val status: String,

        /** Information about the user */
        @SerialName("user") val user: User,

        /** True, if the bot is allowed to edit administrator privileges of that user */
        @SerialName("can_be_edited") val canBeEdited: Boolean,

        /** True, if the user's presence in the chat is hidden */
        @SerialName("is_anonymous") val isAnonymous: Boolean,

        /**
         * True, if the administrator can access the chat event log, chat statistics, message statistics in channels,
         * see channel members, see anonymous administrators in supergroups and ignore slow mode.
         * Implied by any other administrator privilege
         * */
        @SerialName("can_manage_chat") val canManageChat: Boolean,

        /** True, if the administrator can delete messages of other users */
        @SerialName("can_delete_messages") val canDeleteMessages: Boolean,

        /** True, if the administrator can manage voice chats */
        @SerialName("can_manage_voice_chats") val canManageVoiceChats: Boolean,

        /** True, if the administrator can restrict, ban or unban chat members */
        @SerialName("can_restrict_members") val canRestrictMembers: Boolean,

        /**
         * True, if the administrator can add new administrators with a subset of their own privileges
         * or demote administrators that he has promoted, directly or indirectly (promoted by administrators
         * that were appointed by the user)
         * */
        @SerialName("can_promote_members") val canPromoteMembers: Boolean,

        /** True, if the user is allowed to change the chat title, photo and other settings */
        @SerialName("can_change_info") val canChangeInfo: Boolean,

        /** True, if the user is allowed to invite new users to the chat */
        @SerialName("can_invite_users") val canInviteUsers: Boolean,

        /** True, if the administrator can post in the channel; channels only */
        @SerialName("can_post_messages") val canPostMessages: Boolean? = null,

        /** True, if the administrator can edit messages of other users and can pin messages; channels only */
        @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,

        /** True, if the user is allowed to pin messages; groups and supergroups only */
        @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,

        /** Custom title for this user */
        @SerialName("custom_title") val customTitle: String? = null,
    ) : ChatMember()

    @Serializable
    /** Represents a chat member that has no additional privileges or restrictions. */
    data class ChatMemberMember(
        /** The member's status in the chat, always “member” */
        @SerialName("status") val status: String,

        /** Information about the user */
        @SerialName("user") val user: User,
    ) : ChatMember()

    @Serializable
    /** Represents a chat member that is under certain restrictions in the chat. Supergroups only. */
    data class ChatMemberRestricted(
        /** The member's status in the chat, always “restricted” */
        @SerialName("status") val status: String,

        /** Information about the user */
        @SerialName("user") val user: User,

        /** True, if the user is allowed to change the chat title, photo and other settings */
        @SerialName("is_member") val isMember: Boolean,

        /** True, if the user is a member of the chat at the moment of the request */
        @SerialName("can_change_info") val canChangeInfo: Boolean,

        /** True, if the user is allowed to invite new users to the chat */
        @SerialName("can_invite_users") val canInviteUsers: Boolean,

        /** True, if the user is allowed to pin messages */
        @SerialName("can_pin_messages") val canPinMessages: Boolean,

        /** True, if the user is allowed to send text messages, contacts, locations and venues */
        @SerialName("can_send_messages") val canSendMessages: Boolean,

        /** True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes */
        @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean,

        /** True, if the user is allowed to send polls */
        @SerialName("can_send_polls") val canSendPolls: Boolean,

        /** True, if the user is allowed to send animations, games, stickers and use inline bots */
        @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean,

        /** True, if the user is allowed to add web page previews to their messages */
        @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean,

        /** Date when restrictions will be lifted for this user; unix time. If 0, then the user is restricted forever */
        @SerialName("until_date") val untilDate: Int,
    ) : ChatMember()

    @Serializable
    /** Represents a chat member that isn't currently a member of the chat, but may join it themselves. */
    data class ChatMemberLeft(
        /** The member's status in the chat, always “left” */
        @SerialName("status") val status: String,

        /** Information about the user */
        @SerialName("user") val user: User,
    ) : ChatMember()

    @Serializable
    /** Represents a chat member that was banned in the chat and can't return to the chat or view chat messages. */
    data class ChatMemberBanned(
        /** The member's status in the chat, always “kicked” */
        @SerialName("status") val status: String,

        /** Information about the user */
        @SerialName("user") val user: User,

        /** Date when restrictions will be lifted for this user; unix time. If 0, then the user is banned forever */
        @SerialName("until_date") val untilDate: Int,
    ) : ChatMember()
}