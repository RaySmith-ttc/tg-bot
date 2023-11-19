@file:Suppress("GrazieInspection")

package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.PhotoSize
import ru.raysmith.tgbot.network.TelegramService2

/** This object represents a Telegram user or bot. */
@Serializable
data class User(

    /** Unique identifier for this user or bot. This number may have more than 32 significant bits and some
     * programming languages may have difficulty/silent defects in interpreting it. But it has at most 52
     * significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     * */
    @SerialName("id") val id: ChatId.ID,

    /** *True*, if this user is a bot */
    @SerialName("is_bot") val isBot: Boolean,

    /** User's or bot's first name */
    @SerialName("first_name") val firstName: String,

    /** User's or bot's last name */
    @SerialName("last_name") val lastName: String? = null,

    /** User's or bot's username */
    @SerialName("username") val username: String? = null,

    /** [IETF language tag](https://en.wikipedia.org/wiki/IETF_language_tag) of the user's language */
    @SerialName("language_code") val languageCode: String? = null,

    /** *True*, if this user is a Telegram Premium user */
    @SerialName("is_premium") val isPremium: Boolean? = null,

    /** *True*, if this user added the bot to the attachment menu */
    @SerialName("added_to_attachment_menu") val addedToAttachmentMenu: Boolean? = null,

    /** *True*, if the bot can be invited to groups. Returned only in [getMe][BotContext.getMe]. */
    @SerialName("can_join_groups") val canJoinGroups: Boolean? = null,

    /** *True*, if privacy mode is disabled for the bot. Returned only in [getMe][BotContext.getMe]. */
    @SerialName("can_read_all_group_messages") val canReadAllGroupMessages: Boolean? = null,

    /** *True*, if the bot supports inline queries. Returned only in [getMe][BotContext.getMe]. */
    @SerialName("supports_inline_queries") val supportsInlineQueries: Boolean? = null,
) {

    /**
     * Use this method to get a list of profile pictures for a user. Returns a list of list of [PhotoSize] object.
     *
     * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
     * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     *
     * @see TelegramService2.getUserProfilePhotos
     * */
    context(BotContext<*>)
    suspend fun getProfilePhotos(offset: Int? = null, limit: Int? = null): List<List<PhotoSize>> {
        return getUserProfilePhotos(id, offset, limit).photos
    }

    /**
     * Returns list of list of [PhotoSize] of all profile pictures for a user.
     *
     * @see TelegramService2.getUserProfilePhotos
     * */
    context(BotContext<*>)
    suspend fun getAllProfilePhotos(): List<List<PhotoSize>> = buildList {
        var offset = 0
        do {
            val res = getUserProfilePhotos(id, offset)
            addAll(res.photos)
            offset += 100
        } while (size < res.totalCount)
    }

    /**
     * Return full name of the user.
     *
     * @param includeUsername If true the nickname will be added in brackets
     * */
    fun fullname(includeUsername: Boolean = false): String = buildString {
        append(firstName)
        if (lastName != null) {
            append(" ")
        }
        append(lastName ?: "")
        if (includeUsername && username != null) {
            if (this.isNotEmpty()) {
                append(" ")
            }
            append("(")
            append(username)
            append(")")
        }
    }

}