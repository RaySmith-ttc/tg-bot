package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.bisiness.BusinessIntro
import ru.raysmith.tgbot.model.network.bisiness.BusinessLocation
import ru.raysmith.tgbot.model.network.bisiness.BusinessOpeningHours
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.reaction.ReactionType
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypeEmoji
import ru.raysmith.tgbot.network.API

/**  This object contains full information about a chat. */
@Serializable
data class ChatFullInfo (

    /** Unique identifier for this chat. */
    @SerialName("id") override val id: ChatId.ID,

    /** Type of chat, can be either [ChatType.PRIVATE], [ChatType.GROUP], [ChatType.SUPERGROUP] or [ChatType.CHANNEL] */
    @SerialName("type") override val type: ChatType,

    /** Title, for supergroups, channels and group chats */
    @SerialName("title") val title: String? = null,

    /** Username, for private chats, supergroups and channels if available */
    @SerialName("username") override val username: String? = null,

    /** First name of the other party in a private chat */
    @SerialName("first_name") val firstName: String? = null,

    /** Last name of the other party in a private chat */
    @SerialName("last_name") val lastName: String? = null,

    /**
     * *True*, if the supergroup chat is a forum
     * (has [topics](https://telegram.org/blog/topics-in-groups-collectible-usernames#topics-in-groups) enabled)
     * */
    @SerialName("is_forum") val isForum: Boolean? = null,

    /**
     * Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header,
     * and link preview. See [accent colors][AccentColors] for more details.
     * */
    @SerialName("accent_color_id") val accentColorId: Int? = null,

    /** The maximum number of reactions that can be set on a message in the chat */
    @SerialName("max_reaction_count") val maxReactionCount: Int,

    /** Chat photo. Returned only in [getChat][API.getChat]. */
    @SerialName("photo") val photo: ChatPhoto? = null,

    /**
     * If non-empty, the list of all
     * [active chat usernames](https://telegram.org/blog/topics-in-groups-collectible-usernames#collectible-usernames);
     * for private chats, supergroups and channels. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("active_usernames") val activeUsernames: List<String>? = null,

    /** For private chats, the date of birth of the user */
    @SerialName("birthdate ") val birthdate : Birthdate? = null,

    /** For private chats with business accounts, the intro of the business */
    @SerialName("business_intro ") val businessIntro : BusinessIntro? = null,

    /** For private chats with business accounts, the location of the business */
    @SerialName("business_location ") val businessLocation : BusinessLocation? = null,

    /** For private chats with business accounts, the opening hours of the business */
    @SerialName("business_opening_hours ") val businessOpeningHours : BusinessOpeningHours? = null,

    /** For private chats, the personal channel of the user */
    @SerialName("personal_chat") val personalChat : Chat? = null,

    /**
     * List of available reactions allowed in the chat. If omitted, then all [emoji reactions][ReactionTypeEmoji.emoji]
     * are allowed.
     * */
    @SerialName("available_reactions") val availableReactions: List<ReactionType>? = null,

    /** Custom emoji identifier of the emoji chosen by the chat for the reply header and link preview background */
    @SerialName("background_custom_emoji_id") val backgroundCustomEmojiId: String? = null,

    /**
     * Identifier of the accent color for the chat's profile background.
     * See [profile accent colors][ProfileAccentColors] for more details.
     * */
    @SerialName("profile_accent_color_id") val profileAccentColorId: String? = null,

    /** Custom emoji identifier of the emoji chosen by the chat for its profile background */
    @SerialName("profile_background_custom_emoji_id") val profileBackgroundCustomEmojiId: String? = null,

    /**
     * Custom emoji identifier of emoji status of the other party in a private chat.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("emoji_status_custom_emoji_id") val emojiStatusCustomEmojiId: String? = null,

    /**
     * Expiration date of the emoji status of the other party in a private chat in Unix time, if any.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("emoji_status_expiration_date") val emojiStatusExpirationDate: Int? = null,

    /**
     * Bio of the other party in a private chat. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("bio") val bio: String? = null,

    /**
     * *True*, if privacy settings of the other party in the private chat allows to use `tg://user?id=<user_id>`
     * links only in chats with the user. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_private_forwards") val hasPrivateForwards: Boolean? = null,

    /**
     * *True*, if the privacy settings of the other party restrict sending voice and video note messages in the
     * private chat. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_restricted_voice_and_video_messages") val hasRestrictedVoiceAndVideoMessages: Boolean? = null,

    /**
     * *True*, if users need to join the supergroup before they can send messages.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("join_to_send_messages") val joinToSendMessages: Boolean? = null,

    /**
     * *True*, if all users directly joining the supergroup need to be approved by supergroup administrators.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("join_by_request") val joinByRequest: Boolean? = null,

    /**
     * Description, for groups, supergroups and channel chats. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("description") val description: String? = null,

    /**
     * Primary invite link, for groups, supergroups and channel chats. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("invite_link") val inviteLink: String? = null,

    /**
     * The most recent pinned message (by sending date). Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("pinned_message") val pinnedMessage: Message? = null,

    /**
     * Default chat member permissions, for groups and supergroups. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("permissions") val permissions: ChatPermissions? = null,

    /**
     * For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("slow_mode_delay") val slowModeDelay: Int? = null,

    /**
     * For supergroups, the minimum number of boosts that a non-administrator user needs to add in order to ignore
     * slow mode and chat permissions
     * */
    @SerialName("unrestrict_boost_count") val unrestrictBoostCount: Int? = null,

    /**
     * The time after which all messages sent to the chat will be automatically deleted; in seconds.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Int? = null,

    /**
     * True, if aggressive anti-spam checks are enabled in the supergroup.
     * The field is only available to chat administrators. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_aggressive_anti_spam_enabled") val hasAggressiveAntiSpamEnabled: Boolean? = null,

    /**
     * True, if non-administrators can only get the list of bots and administrators in the chat.
     * The field is only available to chat administrators. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_hidden_members") val hasHiddenMembers: Boolean? = null,

    /**
     * True, if messages from the chat can't be forwarded to other chats.
     * The field is only available to chat administrators. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,

    /** *True*, if new chat members will have access to old messages; available only to chat administrators */
    @SerialName("has_visible_history") val hasVisibleHistory: Boolean? = null,

    /**
     * For supergroups, name of group sticker set. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("sticker_set_name") val stickerSetName: String? = null,

    /**
     * True, if the bot can change the group sticker set. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null,

    /**
     * True, if the bot can change the group sticker set. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("custom_emoji_sticker_set_name") val customEmojiStickerSetName: Boolean? = null,

    /**
     * Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa;
     * for supergroups and channel chats. This identifier may be greater than 32 bits and some programming
     * languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits,
     * so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("linked_chat_id") val linkedChatId: Int? = null,

    /**
     * For supergroups, the location to which the supergroup is connected.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("location") val location: ChatLocation? = null,
) : IChat