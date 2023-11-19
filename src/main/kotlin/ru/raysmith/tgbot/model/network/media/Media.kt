package ru.raysmith.tgbot.model.network.media

import ru.raysmith.tgbot.core.BotContext

/** This interface represents media attachment */
interface Media {

    /** Identifier for this file, which can be used to download or reuse the file */
    val fileId: String

    /** Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file. */
    val fileUniqueId: String

    /** File size in bytes */
    val fileSize: Long?

    /** Original animation filename as defined by sender */
    val fileName: String?
}

context(BotContext<*>)
suspend fun Media.inputStream() = downloadFile(fileId)