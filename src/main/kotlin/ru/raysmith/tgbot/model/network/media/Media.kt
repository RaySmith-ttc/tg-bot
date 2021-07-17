package ru.raysmith.tgbot.model.network.media

/** This interface represents media attachment */
interface Media {

    /** Identifier for this file, which can be used to download or reuse the file */
    val fileId: String

    /** Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file. */
    val fileUniqueId: String

    /** File size */
    val fileSize: Int?

    /** Original animation filename as defined by sender */
    val fileName: String?
}