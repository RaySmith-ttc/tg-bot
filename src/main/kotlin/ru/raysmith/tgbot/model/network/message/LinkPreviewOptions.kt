package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.Serializable

/** Describes the options used for link preview generation. */
@Serializable
data class LinkPreviewOptions(

    /** *True*, if the link preview is disabled */
    val isDisabled: Boolean? = null,

    /** URL to use for the link preview. If empty, then the first URL found in the message text will be used */
    val url: String? = null,

    /**
     * *True*, if the media in the link preview is supposed to be shrunk; ignored if the URL isn't explicitly specified
     * or media size change isn't supported for the preview
     * */
    val preferSmallMedia: Boolean? = null,

    /**
     * *True*, if the media in the link preview is supposed to be enlarged; ignored if the URL isn't explicitly
     * specified or media size change isn't supported for the preview
     * */
    val preferLargeMedia: Boolean? = null,

    /**
     * *True*, if the link preview must be shown above the message text; otherwise, the link preview will be shown
     * below the message text
     * */
    val showAboveText: Boolean? = null,
) {
    companion object {
        val DISABLED = LinkPreviewOptions(isDisabled = true)
    }
}