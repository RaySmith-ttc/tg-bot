package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.api.media.Media
import java.io.InputStream

/** This interface represents media attachment */
interface MediaWithFile : Media {

    /** Identifier for this file, which can be used to download or reuse the file */
    val fileId: String

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used
     * to download or reuse the file.
     * */
    val fileUniqueId: String

    /** File size in bytes */
    val fileSize: Long?

    /** Original animation filename as defined by sender */
    val fileName: String?

    /** Downloads the file and returns [InputStream] or null if a path of the file is null */
    context(ctx: BotContext<*>)
    suspend fun inputStream() = ctx.downloadFile(fileId)
}
