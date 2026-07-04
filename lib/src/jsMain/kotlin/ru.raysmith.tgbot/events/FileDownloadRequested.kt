package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject
import ru.raysmith.tgbot.events.FileDownloadRequestedStatus.Companion.cancelled
import ru.raysmith.tgbot.events.FileDownloadRequestedStatus.Companion.downloading
import seskar.js.JsValue

/**
 * @property status Status of the file download request
 * */
@JsPlainObject
external interface FileDownloadRequested {
    val status: FileDownloadRequestedStatus
}

/**
 * Status of the file download request
 *
 * @property downloading The file download has started
 * @property cancelled User declined this request
 * */
sealed external interface FileDownloadRequestedStatus {
    companion object {

        /** The file download has started */
        @JsValue("downloading")
        val downloading: FileDownloadRequestedStatus

        /** User declined this request */
        @JsValue("cancelled")
        val cancelled: FileDownloadRequestedStatus
    }
}
