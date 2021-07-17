package ru.raysmith.tgbot.model.network.message

/** Type of the message. Describes an expected behavior of the message  */
enum class MessageType {
    TEXT,
    COMMAND,
    INLINE_DATA
}