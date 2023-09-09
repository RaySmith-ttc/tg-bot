package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.PollType

/**
 * This object represents type of a poll, which is allowed to be created and sent when the corresponding
 * button is pressed.
 * */
@Serializable
data class KeyboardButtonPollType(

    /**
     * If [PollType.QUIZ] is passed, the user will be allowed to create only polls in the quiz mode.
     * If [PollType.REGULAR] is passed, only regular polls will be allowed.
     * Otherwise, the user will be allowed to create a poll of any type.
     * */
    @SerialName("type") val type: PollType? = null
)