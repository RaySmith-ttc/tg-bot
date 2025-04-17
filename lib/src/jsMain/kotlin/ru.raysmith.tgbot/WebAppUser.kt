package ru.raysmith.tgbot

/**
 * This object contains the data of the Mini App user.
 */
external interface WebAppUser {

    /** A unique identifier for the user or bot. */
    val id: Long

    /** *True*, if this user is a bot. Returns in the [WebAppInitData.receiver] field only. */
    @JsName("is_bot")
    val isBot: Boolean?

    /** First name of the user or bot. */
    @JsName("first_name")
    val firstName: String

    /** Last name of the user or bot. */
    @JsName("last_name")
    val lastName: String?

    /** Username of the user or bot. */
    val username: String?

    /**
     * [IETF language tag](https://en.wikipedia.org/wiki/IETF_language_tag) of the user's language.
     * Returns in user field only.
     * */
    @JsName("language_code")
    val languageCode: String?

    /** *True*, if this user is a Telegram Premium user. */
    @JsName("is_premium")
    val isPremium: Boolean?

    /** *True*, if this user added the bot to the attachment menu. */
    @JsName("added_to_attachment_menu")
    val addedToAttachmentMenu: Boolean?

    /** *True*, if this user allowed the bot to message them. */
    @JsName("allows_write_to_pm")
    val allowsWriteToPm: Boolean?

    /** URL of the user’s profile photo. The photo can be in .jpeg or .svg formats. */
    @JsName("photo_url")
    val photoUrl: String?
}