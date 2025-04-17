package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.InvoiceClosedStatus.Companion.cancelled
import ru.raysmith.tgbot.events.InvoiceClosedStatus.Companion.failed
import ru.raysmith.tgbot.events.InvoiceClosedStatus.Companion.paid
import ru.raysmith.tgbot.events.InvoiceClosedStatus.Companion.pending
import seskar.js.JsValue

/**
 * @property url Invoice link provided
 * @property status Status
 * */
external interface InvoiceClosed {

    /** Invoice link provided */
    val url: String

    /** Status */
    val status: InvoiceClosedStatus
}

/**
 * The status of the closed invoice
 *
 * @property paid Invoice was paid successfully
 * @property cancelled User closed this invoice without paying
 * @property failed User tried to pay, but the payment was failed
 * @property pending The payment is still processing. The bot will receive a service message about a
 * */
external interface InvoiceClosedStatus {
    companion object {

        /** Invoice was paid successfully */
        @JsValue("paid")
        val paid: InvoiceClosedStatus

        /** User closed this invoice without paying */
        @JsValue("cancelled")
        val cancelled: InvoiceClosedStatus

        /** User tried to pay, but the payment was failed */
        @JsValue("failed")
        val failed: InvoiceClosedStatus

        /**
         * The payment is still processing. The bot will receive a service message about a
         * [successful payment](https://core.telegram.org/bots/api#successfulpayment) when the payment is
         * successfully paid.
         * */
        @JsValue("pending")
        val pending: InvoiceClosedStatus
    }
}