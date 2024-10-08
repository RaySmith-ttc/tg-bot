package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that has some additional privileges. */
@Serializable
data class ChatMemberAdministrator(
    @SerialName("user") override val user: User,

    /** *True*, if the bot is allowed to edit administrator privileges of that user */
    @SerialName("can_be_edited") val canBeEdited: Boolean,

    /** *True*, if the user's presence in the chat is hidden */
    @SerialName("is_anonymous") val isAnonymous: Boolean,

    /**
     * True, if the administrator can access the chat event log, get boost list, see hidden supergroup and channel
     * members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
     * */
    @SerialName("can_manage_chat") val canManageChat: Boolean,

    /** *True*, if the administrator can delete messages of other users */
    @SerialName("can_delete_messages") val canDeleteMessages: Boolean,

    /** *True*, if the administrator can manage voice chats */
    @SerialName("can_manage_voice_chats") val canManageVoiceChats: Boolean,

    /** *True*, if the administrator can restrict, ban or unban chat members, or access supergroup statistics */
    @SerialName("can_restrict_members") val canRestrictMembers: Boolean,

    /**
     * True, if the administrator can add new administrators with a subset of their own privileges
     * or demote administrators that he has promoted, directly or indirectly (promoted by administrators
     * that were appointed by the user)
     * */
    @SerialName("can_promote_members") val canPromoteMembers: Boolean,

    /** *True*, if the user is allowed to change the chat title, photo and other settings */
    @SerialName("can_change_info") val canChangeInfo: Boolean,

    /** *True*, if the user is allowed to invite new users to the chat */
    @SerialName("can_invite_users") val canInviteUsers: Boolean,

    /**
     * *True*, if the administrator can post messages in the channel, or access channel statistics; for channels only
     * */
    @SerialName("can_post_messages") val canPostMessages: Boolean? = null,

    /** *True*, if the administrator can edit messages of other users and can pin messages; for channels only */
    @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,

    /** *True*, if the user is allowed to pin messages; for groups and supergroups */
    @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,

    /** *True*, if the administrator can post stories in the channel; channels only */
    @SerialName("can_post_stories") val canPostStories: Boolean? = null,

    /** *True*, if the administrator can edit stories posted by other users; channels only */
    @SerialName("can_edit_stories") val canEditStories: Boolean? = null,

    /** *True*, if the administrator can delete stories posted by other users; channels only */
    @SerialName("can_delete_stories") val canDeleteStories: Boolean? = null,

    /** *True*, if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only */
    @SerialName("can_manage_topics") val canManageTopics: Boolean? = null,

    /** Custom title for this user */
    @SerialName("custom_title") val customTitle: String? = null,
) : ChatMember() {

    /** The member's status in the chat, always “administrator” */
    override val status: String = "administrator"
}