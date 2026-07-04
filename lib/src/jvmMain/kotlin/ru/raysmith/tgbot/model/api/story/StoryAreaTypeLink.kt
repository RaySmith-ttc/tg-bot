package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a story area pointing to an HTTP or tg:// link. Currently, a story can have up to 3 link areas. */
@Serializable
data class StoryAreaTypeLink(

    /** HTTP or tg:// URL to be opened when the area is clicked */
    @SerialName("url") val url: String,
) : StoryAreaType {

    /** Type of the area, always “link” */
    override val type: String = "link"
}

