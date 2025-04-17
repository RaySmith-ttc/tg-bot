package ru.raysmith.tgbot

/**
 * This object contains data that is transferred to the Mini App when it is opened.
 * It is empty if the Mini App was launched from a keyboard button or from inline mode.
 */
external interface WebAppInitData {

    /**
     * A unique identifier for the Mini App session, required for sending messages via the `answerWebAppQuery` method.
     * */
    @JsName("query_id")
    val queryId: String?

    /** An object containing data about the current user. */
    val user: WebAppUser?

    /**
     * An object containing data about the chat partner of the current user in the chat where the bot was launched
     * via the attachment menu.
     * */
    val receiver: WebAppUser?

    /** An object containing data about the chat where the bot was launched via the attachment menu. */
    val chat: WebAppChat?

    /**
     * Type of the chat from which the Mini App was opened. Can be either [ChatType.sender] for a private chat with the
     * user opening the link, [ChatType.private], [ChatType.group], [ChatType.supergroup], or [ChatType.channel].
     * Returned only for Mini Apps launched from direct links.
     * */
    @JsName("chat_type")
    val chatType: String?

    /** Global identifier, uniquely corresponding to the chat from which the Mini App was opened. */
    @JsName("chat_instance")
    val chatInstance: String?

    /**
     * The value of the `startattach` parameter, passed
     * [via link](https://core.telegram.org/bots/webapps#adding-bots-to-the-attachment-menu).
     *
     * The value of the `start_param` parameter will also be passed in the GET-parameter `tgWebAppStartParam`, so the
     * Mini App can load the correct interface right away.
     * */
    @JsName("start_param")
    val startParam: String?

    /**
     * Time in seconds, after which a message can be sent via the
     * [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery) method.
     * */
    @JsName("can_send_after")
    val canSendAfter: Int?

    /** Unix time when the form was opened. */
    @JsName("auth_date")
    val authDate: Int

    /**
     * A hash of all passed parameters, which the bot server can use to
     * [check their validity](https://core.telegram.org/bots/webapps#validating-data-received-via-the-mini-app).
     * */
    val hash: String

    /**
     * A signature of all passed parameters (except hash), which the third party can use to
     * [check their validity](https://core.telegram.org/bots/webapps#validating-data-received-via-the-mini-app).
     * */
    val signature: String
}