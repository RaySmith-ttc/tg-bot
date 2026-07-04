package ru.raysmith.tgbot.model.api.message.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a rich message to be sent. Exactly one of the fields [html] or [markdown] must be used. */
@Serializable
data class InputRichMessage(

    /**
     * Content of the rich message to send described using HTML formatting. See
     * [rich message formatting options](https://core.telegram.org/bots/api#rich-message-formatting-options) for more
     * details.
     * */
    @SerialName("html") val html: String? = null,

    /**
     * Content of the rich message to send described using Markdown formatting. See
     * [rich message formatting options](https://core.telegram.org/bots/api#rich-message-formatting-options) for more
     * details.
     * */
    @SerialName("markdown") val markdown: String? = null,

    /** Pass *True* if the rich message must be shown right-to-left */
    @SerialName("is_rtl") val isRtl: Boolean? = null,

    /**
     * Pass *True* to skip automatic detection of entities (e.g., URLs, email addresses, username mentions, hashtags
     * cashtags, bot commands, or phone numbers) in the text
     * */
    @SerialName("skip_entity_detection") val skipEntityDetection: Boolean? = null,
)
