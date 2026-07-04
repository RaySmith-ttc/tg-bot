package ru.raysmith.tgbot.model.api.profile

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InputProfilePhotoSerializer

/**
 * This object describes a profile photo to set. Currently, it can be one of
 *
 * - [InputProfilePhotoStatic]
 * - [InputProfilePhotoAnimated]
 * */
@Serializable(InputProfilePhotoSerializer::class)
sealed interface InputProfilePhoto {

    /** Type of the profile photo */
    val type: String
}
