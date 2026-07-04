package ru.raysmith.tgbot.model.api.message

import kotlinx.serialization.Serializable

/** Type of the message. Describes an expected behavior of the message  */
@Serializable
enum class MessageType {
    TEXT,
    COMMAND
}
