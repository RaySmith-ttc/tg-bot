package ru.raysmith.tgbot.model.network.inline.content

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a message to be sent as a result of an inline query.
 * Telegram clients currently support the following 5 types:
 *
 * - [InputTextMessageContent]
 * - [InputLocationMessageContent]
 * - [InputVenueMessageContent]
 * - [InputContactMessageContent]
 * - [InputInvoiceMessageContent]
 * */
@Polymorphic
@Serializable
sealed class InputMessageContent