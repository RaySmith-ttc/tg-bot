package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a story area pointing to a unique gift. Currently, a story can have at most 1 unique gift area. */
@Serializable
data class StoryAreaTypeUniqueGift(

    /** Unique name of the gift */
    @SerialName("name") val name: String,
) : StoryAreaType {

    /** Type of the area, always “unique_gift” */
    override val type: String = "unique_gift"
}
