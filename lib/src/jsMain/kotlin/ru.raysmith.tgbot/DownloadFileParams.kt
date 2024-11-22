package ru.raysmith.tgbot

/** This object describes the parameters for the file download request. */
external interface DownloadFileParams {

    /** The HTTPS URL of the file to be downloaded. */
    val url: String

    /** TThe suggested name for the downloaded file. */
    @JsName("file_name")
    val fileName: String
}