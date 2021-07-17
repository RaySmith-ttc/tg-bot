package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** Describes actions that a non-administrator user is allowed to take in a chat. */
data class ChatPermissions(

    /** True, if the user is allowed to send text messages, contacts, locations and venues */
    @SerialName("can_send_messages") val canSendMessages: Boolean,

    /** True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages */
    @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean,

    /** True, if the user is allowed to send polls, implies can_send_messages */
    @SerialName("can_send_polls") val canSendPolls: Boolean,

    /** True, if the user is allowed to send animations, games, stickers and use inline bots, implies can_send_media_messages */
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean,

    /** True, if the user is allowed to add web page previews to their messages, implies can_send_media_messages */
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean,

    /** True, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups */
    @SerialName("can_change_info") val canChangeInfo: Boolean,

    /** True, if the user is allowed to invite new users to the chat */
    @SerialName("can_invite_users") val canInviteUsers: Boolean,

    /** True, if the user is allowed to pin messages. Ignored in public supergroups */
    @SerialName("can_pin_messages") val canPinMessages: Boolean

)
