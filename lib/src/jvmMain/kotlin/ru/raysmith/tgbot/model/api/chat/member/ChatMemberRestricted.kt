package ru.raysmith.tgbot.model.api.chat.member

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** Represents a chat member that is under certain restrictions in the chat. Supergroups only. */
@Serializable
data class ChatMemberRestricted(

    /** Tag of the member */
    @SerialName("tag") val tag: String? = null,

    /** Information about the user */
    @SerialName("user")  override val user: User,

    /** *True*, if the user is allowed to change the chat title, photo and other settings */
    @SerialName("is_member") val isMember: Boolean,

    /**
     * *True*, if the user is allowed to send text messages, rich messages, contacts, giveaways, giveaway winners, locations and venues
     * */
    @SerialName("can_send_messages") val canSendMessages: Boolean,

    /** *True*, if the user is allowed to send audios */
    @SerialName("can_send_audios") val canSendAudios: Boolean,

    /** *True*, if the user is allowed to send documents */
    @SerialName("can_send_documents") val canSendDocuments: Boolean,

    /** *True*, if the user is allowed to send photos */
    @SerialName("can_send_photos") val canSendPhotos: Boolean,

    /** *True*, if the user is allowed to send videos */
    @SerialName("can_send_videos") val canSendVideos: Boolean,

    /** *True*, if the user is allowed to send video notes */
    @SerialName("can_send_video_notes") val canSendVideoNotes: Boolean,

    /** *True*, if the user is allowed to send voice notes */
    @SerialName("can_send_voice_notes") val canSendVoiceNotes: Boolean,

    /** *True*, if the user is allowed to send polls and checklists */
    @SerialName("can_send_polls") val canSendPolls: Boolean,

    /** *True*, if the user is allowed to send animations, games, stickers and use inline bots */
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean,

    /** *True*, if the user is allowed to add web page previews to their messages */
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean,

    /** *True*, if the user is allowed to react to messages */
    @SerialName("can_react_to_messages") val canReactToMessages: Boolean,

    /** *True*, if the user is allowed to edit their own tag */
    @SerialName("can_edit_tag") val canEditTag: Boolean,

    /** *True*, if the user is a member of the chat at the moment of the request */
    @SerialName("can_change_info") val canChangeInfo: Boolean,

    /** *True*, if the user is allowed to invite new users to the chat */
    @SerialName("can_invite_users") val canInviteUsers: Boolean,

    /** *True*, if the user is allowed to pin messages */
    @SerialName("can_pin_messages") val canPinMessages: Boolean,

    /** *True*, if the user is allowed to create forum topics */
    @SerialName("can_manage_topics") val canManageTopics: Boolean,

    /**
     * Date when restrictions will be lifted for this user. If [isRestrictedForever] returns true, then the user is
     * banned forever.
     * */
    @SerialName("until_date") @Serializable(ZonedDateTimeAsUnixSerializer::class) val untilDate: ZonedDateTime,
) : ChatMember {

    /** The member's status in the chat, always “restricted” */
    @Required
    @EncodeDefault
    @SerialName("status")
    override val status: String = "restricted"

    // TODO find other date fields and provide utils functions
    fun isRestrictedForever() = untilDate.toEpochSecond() == 0L
}
