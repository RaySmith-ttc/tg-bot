package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about the color scheme for a user's name, message replies and link previews based on
 * a unique gift.
 * */
@Serializable
data class UniqueGiftColors(

    /** Custom emoji identifier of the unique gift's model */
    @SerialName("model_custom_emoji_id") val modelCustomEmojiId: String,

    /** Custom emoji identifier of the unique gift's symbol */
    @SerialName("symbol_custom_emoji_id") val symbolCustomEmojiId: String,

    /** Main color used in light themes; RGB format */
    @SerialName("light_theme_main_color") val lightThemeMainColor: Int,

    /** List of 1-3 additional colors used in light themes; RGB format */
    @SerialName("light_theme_other_colors") val lightThemeOtherColors: List<Int>,

    /** Main color used in dark themes; RGB format */
    @SerialName("dark_theme_main_color") val darkThemeMainColor: Int,

    /** List of 1-3 additional colors used in dark themes; RGB format */
    @SerialName("dark_theme_other_colors") val darkThemeOtherColors: List<Int>,
)
