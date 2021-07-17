package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 *
 * @param type See [MessageEntityType]
 * @param offset Offset in UTF-16 code units to the start of the entity
 * @param length Length of the entity in UTF-16 code units
 * @param url For “text_link” only, url that will be opened after user taps on the text
 * @param user For “text_mention” only, the mentioned user
 * @param language For “pre” only, the programming language of the entity text
 *
 * @see <a href="https://core.telegram.org/bots/api#messageentity">api#messageentity</a>
 * */

@Serializable
data class MessageEntity(

    /** See [MessageEntityType] */
    @SerialName("type") val type: MessageEntityType,

    /** Offset in UTF-16 code units to the start of the entity */
    @SerialName("offset") val offset: Int,

    /** Length of the entity in UTF-16 code units */
    @SerialName("length") val length: Int,

    /** For “text_link” only, url that will be opened after user taps on the text */
    @SerialName("url") val url: String? = null,

    /** For “text_mention” only, the mentioned user */
    @SerialName("user") val user: User? = null,

    /** For “pre” only, the programming language of the entity text */
    @SerialName("language") val language: String? = null,
)

