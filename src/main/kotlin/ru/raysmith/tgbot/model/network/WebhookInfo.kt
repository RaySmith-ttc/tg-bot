package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.updates.UpdateType

/** Describes the current status of a webhook. */
@Serializable
data class WebhookInfo(

    /** Webhook URL, may be empty if webhook is not set up */
    @SerialName("url") val url: String,

    /** *True*, if a custom certificate was provided for webhook certificate checks */
    @SerialName("has_custom_certificate") val hasCustomCertificate: Boolean,

    /** Number of updates awaiting delivery */
    @SerialName("pending_update_count") val pendingUpdateCount: Int,

    /** Currently used webhook IP address */
    @SerialName("ip_address") val ipAddress: String?,

    /** Unix time for the most recent error that happened when trying to deliver an update via webhook */
    @SerialName("last_error_date") val lastErrorDate: Int?,

    /** Error message in human-readable format for the most recent error that happened when trying to deliver an update via webhook */
    @SerialName("last_error_message") val lastErrorMessage: String?,

    /** Unix time of the most recent error that happened when trying to synchronize available updates with Telegram datacenters */
    @SerialName("last_synchronization_error_date") val lastSynchronizationErrorDate: Int?,

    /** The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery */
    @SerialName("max_connections") val maxConnections: Int?,

    /** A list of update types the bot is subscribed to. Defaults to all update types except [UpdateType.CHAT_MEMBER] */
    @SerialName("allowed_updates") val allowedUpdates: List<UpdateType>?,

)