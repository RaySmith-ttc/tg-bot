package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents an inline keyboard button that copies specified text to the clipboard */
@Serializable
data class CopyTextButton(

    /** The text to be copied to the clipboard; 1-256 characters */
    @SerialName("text") val text: String

)
