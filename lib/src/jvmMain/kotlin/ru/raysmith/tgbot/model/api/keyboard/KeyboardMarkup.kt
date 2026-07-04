package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Polymorphic // TODO delete?
@Serializable
sealed class KeyboardMarkup
