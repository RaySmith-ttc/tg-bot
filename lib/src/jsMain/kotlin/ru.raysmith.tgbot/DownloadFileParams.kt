package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

/**
 * This object describes the parameters for the file download request.
 *
 * > **Note:** To ensure consistent file download behavior across platforms, include the HTTP headers `Content-
 * Disposition: attachment; filename="<file_name>"` and `Access-Control-Allow-Origin: https://web.telegram.org` in the
 * server response. Without these headers the download may not work as expected, especially on web platforms.
 *
 * @property url The HTTPS URL of the file to be downloaded.
 * @property fileName The suggested name for the downloaded file.
 * */
@JsPlainObject
external interface DownloadFileParams {
    val url: String

    @JsName("file_name")
    val fileName: String
}
