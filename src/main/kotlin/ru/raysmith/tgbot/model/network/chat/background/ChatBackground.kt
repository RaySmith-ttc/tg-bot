package ru.raysmith.tgbot.model.network.chat.background

import kotlinx.serialization.Serializable

/** This object represents a chat background. */
@Serializable
data class ChatBackground(

    /** Type of the background */
    val type: BackgroundType
)


