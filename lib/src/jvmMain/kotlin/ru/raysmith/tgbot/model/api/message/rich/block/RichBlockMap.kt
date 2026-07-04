package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.Location

/** A block with a map, corresponding to the custom HTML tag <tg-map>. */
@Serializable
data class RichBlockMap(

    /** Location of the center of the map */
    @SerialName("location") val location: Location,

    /** Map zoom level; 13-20 */
    @SerialName("zoom") val zoom: Int,

    /** Expected width of the map */
    @SerialName("width") val width: Int,

    /** Expected height of the map */
    @SerialName("height") val height: Int,

    /** Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “map” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "map"
}
