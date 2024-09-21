package ru.raysmith.tgbot.model.network.media

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a story. */
@Serializable
class Story(

    /** Chat that posted the story */
    val chat: Chat,

    /** Unique identifier for the story in the chat */
    val id: Int
)