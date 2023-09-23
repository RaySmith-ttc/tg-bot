package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO [js] add link to requestWriteAccess (find in all project)
/**
 * This object represents a service message about a user allowing a bot to write messages after adding it to the
 * attachment menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the
 * method requestWriteAccess.
 * */
@Serializable
data class WriteAccessAllowed(

    /**
     * True, if the access was granted after the user accepted an explicit request from
     * a Web App sent by the method requestWriteAccess
     * */
    @SerialName("from_request") val fromRequest: Boolean? = null,

    /** Name of the Web App which was launched from a link */
    @SerialName("web_app_name") val webAppName: String? = null,

    /** True, if the access was granted when the bot was added to the attachment or side menu */
    @SerialName("from_attachment_menu") val fromAttachmentMenu: Boolean? = null,
)