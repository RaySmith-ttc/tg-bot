package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.network.TelegramApi

interface Permission

enum class ChatPermission : Permission {
    CAN_SEND_MESSAGES,
    CAN_SEND_MEDIA_MESSAGES,
    CAN_SEND_POLLS,
    CAN_SEND_OTHER_MESSAGES,
    CAN_ADD_WEB_PAGE_PREVIEWS,
    CAN_CHANGE_INFO,
    CAN_INVITE_USERS,
    CAN_PIN_MESSAGES
}

@Serializable
/** Describes actions that a non-administrator user is allowed to take in a chat. */
data class ChatPermissions(

    /** True, if the user is allowed to send text messages, contacts, locations and venues */
    @SerialName("can_send_messages") val canSendMessages: Boolean = false,

    /** True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages */
    @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean = false,

    /** True, if the user is allowed to send polls, implies can_send_messages */
    @SerialName("can_send_polls") val canSendPolls: Boolean = false,

    /** True, if the user is allowed to send animations, games, stickers and use inline bots, implies can_send_media_messages */
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean = false,

    /** True, if the user is allowed to add web page previews to their messages, implies can_send_media_messages */
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean = false,

    /** True, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups */
    @SerialName("can_change_info") val canChangeInfo: Boolean = false,

    /** True, if the user is allowed to invite new users to the chat */
    @SerialName("can_invite_users") val canInviteUsers: Boolean = false,

    /** True, if the user is allowed to pin messages. Ignored in public supergroups */
    @SerialName("can_pin_messages") val canPinMessages: Boolean = false

) {

    fun check(permission: ChatPermission) = when(permission) {
        ChatPermission.CAN_SEND_MESSAGES -> canSendMediaMessages
        ChatPermission.CAN_SEND_MEDIA_MESSAGES -> canSendMediaMessages
        ChatPermission.CAN_SEND_POLLS -> canSendPolls
        ChatPermission.CAN_SEND_OTHER_MESSAGES -> canSendOtherMessages
        ChatPermission.CAN_ADD_WEB_PAGE_PREVIEWS -> canAddWebPagePreviews
        ChatPermission.CAN_CHANGE_INFO -> canChangeInfo
        ChatPermission.CAN_INVITE_USERS -> canInviteUsers
        ChatPermission.CAN_PIN_MESSAGES -> canPinMessages
    }

    override fun toString(): String {
        return TelegramApi.json.encodeToString(this)
    }

    companion object {
        fun restrictionsLifted() = ChatPermissions(
            canSendMessages = true,
            canSendMediaMessages = true,
            canSendPolls = true,
            canSendOtherMessages = true,
            canAddWebPagePreviews = true,
            canChangeInfo = true,
            canInviteUsers = true,
            canPinMessages = true
        )
    }
}

