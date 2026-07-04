package ru.raysmith.tgbot.model.api.media.input

sealed interface InputWithMedia { // TODO move to model/bot; delete?
    val media: InputFile
}
