package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes actions that a non-administrator user is allowed to take in a chat. */
@Serializable
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
    companion object {
        // TODO docs
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

