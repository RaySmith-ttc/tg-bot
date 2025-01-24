package ru.raysmith.tgbot

/**
 * This object describes the parameters for the file download request.
 *
 * > **Note:** To ensure consistent file download behavior across platforms, it is recommended to include the HTTP header
 * `Content-Disposition: attachment; filename="<file_name>"` in the server response. This header helps prompt the
 * download action and suggests a file name for the downloaded file, especially on web platforms where forced downloads
 * cannot always be guaranteed.
 * */
external interface DownloadFileParams {

    /** The HTTPS URL of the file to be downloaded. */
    val url: String

    /** TThe suggested name for the downloaded file. */
    @JsName("file_name")
    val fileName: String
}