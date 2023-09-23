package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the rights of an administrator in a chat. */
@Serializable
data class ChatAdministratorRights(

    /** *True*, if the user's presence in the chat is hidden */
    @SerialName("is_anonymous") val isAnonymous: Boolean = false,

    /**
     * True, if the administrator can access the chat event log, chat statistics, boost list in channels, message statistics in channels,
     * see channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any
     * other administrator privilege
     * */
    @SerialName("can_manage_chat") val canManageChat: Boolean = false,

    /** *True*, if the administrator can delete messages of other users */
    @SerialName("can_delete_messages") val canDeleteMessages: Boolean = false,

    /** *True*, if the administrator can manage video chats */
    @SerialName("can_manage_video_chats") val canManageVideoChats: Boolean = false,

    /** *True*, if the administrator can restrict, ban or unban chat members */
    @SerialName("can_restrict_members") val canRestrictMembers: Boolean = false,

    /**
     * True, if the administrator can add new administrators with a subset of their own privileges or demote
     * administrators that he has promoted, directly or indirectly
     * (promoted by administrators that were appointed by the user)
     * */
    @SerialName("can_promote_members") val canPromoteMembers: Boolean = false,

    /** *True*, if the user is allowed to change the chat title, photo and other settings */
    @SerialName("can_change_info") val canChangeInfo: Boolean = false,

    /** *True*, if the user is allowed to invite new users to the chat */
    @SerialName("can_invite_users") val canInviteUsers: Boolean = false,

    /** *True*, if the administrator can post messages in the channel; channels only */
    @SerialName("can_post_messages") val canPostMessages: Boolean? = null,

    /** *True*, if the administrator can edit messages of other users and can pin messages; channels only */
    @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,

    /** *True*, if the user is allowed to pin messages; groups and supergroups only */
    @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,

    /** *True*, if the administrator can post stories in the channel; channels only */
    @SerialName("can_post_stories") val canPostStories: Boolean? = null,

    /** *True*, if the administrator can edit stories posted by other users; channels only */
    @SerialName("can_edit_stories") val canEditStories: Boolean? = null,

    /** *True*, if the administrator can delete stories posted by other users; channels only */
    @SerialName("can_delete_stories") val canDeleteStories: Boolean? = null,

    /** *True*, if the user is allowed to create, rename, close, and reopen forum topics; supergroups only */
    @SerialName("can_manage_topics") val canManageTopics: Boolean? = null,
)