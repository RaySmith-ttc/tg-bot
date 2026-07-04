package ru.raysmith.tgbot.model.api.media.input

/** Represents an [InputFile] that can't be sent twice without reuploading to the telegram server */
sealed interface NotReusableInputFile : InputFile
