package ru.raysmith.tgbot.model.api.inline.content

import kotlinx.serialization.Serializable

/**
 * This object represents the content of a message to be sent as a result of an inline query.
 * Telegram clients currently support the following types:
 *
 * - [InputTextMessageContent]
 * - [InputRichMessageContent]
 * - [InputLocationMessageContent]
 * - [InputVenueMessageContent]
 * - [InputContactMessageContent]
 * - [InputInvoiceMessageContent]
 * */
@Serializable
sealed interface InputMessageContent
