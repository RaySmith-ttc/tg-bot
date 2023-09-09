package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Permission

@Serializable
enum class ChatPermission : Permission {

    /** *True*, if the user is allowed to send text messages, contacts, locations and venues */
    CAN_SEND_MESSAGES,

    /** *True*, if the user is allowed to send audios */
    CAN_SEND_AUDIOS,

    /** *True*, if the user is allowed to send documents */
    CAN_SEND_DOCUMENTS,

    /** *True*, if the user is allowed to send photos */
    CAN_SEND_PHOTOS,

    /** *True*, if the user is allowed to send videos */
    CAN_SEND_VIDEOS,

    /** *True*, if the user is allowed to send video notes */
    CAN_SEND_VIDEO_NOTES,

    /** *True*, if the user is allowed to send voice notes */
    CAN_SEND_VOICE_NOTES,

    /** *True*, if the user is allowed to send polls */
    CAN_SEND_POLLS,

    /** *True*, if the user is allowed to send animations, games, stickers and use inline bots */
    CAN_SEND_OTHER_MESSAGES,

    /** *True*, if the user is allowed to add web page previews to their messages, implies can_send_media_messages */
    CAN_ADD_WEB_PAGE_PREVIEWS,

    /** *True*, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups */
    CAN_CHANGE_INFO,

    /** *True*, if the user is allowed to invite new users to the chat */
    CAN_INVITE_USERS,

    /** *True*, if the user is allowed to pin messages. Ignored in public supergroups */
    CAN_PIN_MESSAGES,

    /** *True*, if the user is allowed to create forum topics. If omitted defaults to the value of [CAN_PIN_MESSAGES] */
    CAN_MANAGE_TOPICS
}

@Serializable
/** Describes actions that a non-administrator user is allowed to take in a chat. */
data class ChatPermissions(

    /** *True*, if the user is allowed to send text messages, contacts, locations and venues */
    @SerialName("can_send_messages") val canSendMessages: Boolean = false,

    /** *True*, if the user is allowed to send audios */
    @SerialName("can_send_audios") val canSendAudios: Boolean = false,

    /** *True*, if the user is allowed to send documents */
    @SerialName("can_send_documents") val canSendDocuments: Boolean = false,

    /** *True*, if the user is allowed to send photos */
    @SerialName("can_send_photos") val canSendPhotos: Boolean = false,

    /** *True*, if the user is allowed to send videos */
    @SerialName("can_send_videos") val canSendVideos: Boolean = false,

    /** *True*, if the user is allowed to send video notes */
    @SerialName("can_send_video_notes") val canSendVideoNotes: Boolean = false,

    /** *True*, if the user is allowed to send voice notes */
    @SerialName("can_send_voice_notes") val canSendVoiceNotes: Boolean = false,

    /** *True*, if the user is allowed to send polls */
    @SerialName("can_send_polls") val canSendPolls: Boolean = false,

    /** *True*, if the user is allowed to send animations, games, stickers and use inline bots */
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean = false,

    /** *True*, if the user is allowed to add web page previews to their messages, implies can_send_media_messages */
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean = false,

    /** *True*, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups */
    @SerialName("can_change_info") val canChangeInfo: Boolean = false,

    /** *True*, if the user is allowed to invite new users to the chat */
    @SerialName("can_invite_users") val canInviteUsers: Boolean = false,

    /** *True*, if the user is allowed to pin messages. Ignored in public supergroups */
    @SerialName("can_pin_messages") val canPinMessages: Boolean = false,

    /** *True*, if the user is allowed to create forum topics. If omitted defaults to the value of [canPinMessages] */
    @SerialName("can_manage_topics") val canManageTopics: Boolean = false,

) {

    fun check(permission: ChatPermission) = when(permission) {
        ChatPermission.CAN_SEND_MESSAGES -> canSendMessages
        ChatPermission.CAN_SEND_AUDIOS -> canSendAudios
        ChatPermission.CAN_SEND_DOCUMENTS -> canSendDocuments
        ChatPermission.CAN_SEND_PHOTOS -> canSendPhotos
        ChatPermission.CAN_SEND_VIDEOS -> canSendVideos
        ChatPermission.CAN_SEND_VIDEO_NOTES -> canSendVideoNotes
        ChatPermission.CAN_SEND_VOICE_NOTES -> canSendVoiceNotes
        ChatPermission.CAN_SEND_POLLS -> canSendPolls
        ChatPermission.CAN_SEND_OTHER_MESSAGES -> canSendOtherMessages
        ChatPermission.CAN_ADD_WEB_PAGE_PREVIEWS -> canAddWebPagePreviews
        ChatPermission.CAN_CHANGE_INFO -> canChangeInfo
        ChatPermission.CAN_INVITE_USERS -> canInviteUsers
        ChatPermission.CAN_PIN_MESSAGES -> canPinMessages
        ChatPermission.CAN_MANAGE_TOPICS -> canManageTopics
    }

    // TODO docs
    companion object {
        fun ofPermissions(permissions: List<ChatPermission>) = ChatPermissions(
            canSendMessages = permissions.contains(ChatPermission.CAN_SEND_MESSAGES),
            canSendAudios = permissions.contains(ChatPermission.CAN_SEND_AUDIOS),
            canSendDocuments = permissions.contains(ChatPermission.CAN_SEND_DOCUMENTS),
            canSendPhotos = permissions.contains(ChatPermission.CAN_SEND_PHOTOS),
            canSendVideos = permissions.contains(ChatPermission.CAN_SEND_VIDEOS),
            canSendVideoNotes = permissions.contains(ChatPermission.CAN_SEND_VIDEO_NOTES),
            canSendVoiceNotes = permissions.contains(ChatPermission.CAN_SEND_VOICE_NOTES),
            canSendPolls = permissions.contains(ChatPermission.CAN_SEND_POLLS),
            canSendOtherMessages = permissions.contains(ChatPermission.CAN_SEND_OTHER_MESSAGES),
            canAddWebPagePreviews = permissions.contains(ChatPermission.CAN_ADD_WEB_PAGE_PREVIEWS),
            canChangeInfo = permissions.contains(ChatPermission.CAN_CHANGE_INFO),
            canInviteUsers = permissions.contains(ChatPermission.CAN_INVITE_USERS),
            canPinMessages = permissions.contains(ChatPermission.CAN_PIN_MESSAGES),
            canManageTopics = permissions.contains(ChatPermission.CAN_MANAGE_TOPICS)
        )

        fun fullAccess() = ChatPermissions(
            canSendMessages = true,
            canSendAudios = true,
            canSendDocuments = true,
            canSendPhotos = true,
            canSendVideos = true,
            canSendVideoNotes = true,
            canSendVoiceNotes = true,
            canSendPolls = true,
            canSendOtherMessages = true,
            canAddWebPagePreviews = true,
            canChangeInfo = true,
            canInviteUsers = true,
            canPinMessages = true,
            canManageTopics = true
        )
    }
}

