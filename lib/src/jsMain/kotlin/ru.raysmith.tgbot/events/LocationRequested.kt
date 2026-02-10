package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject
import ru.raysmith.tgbot.LocationData

/**
 * @property locationData Contains the current location information
 * */
@JsPlainObject
external interface LocationRequested {
    val locationData: LocationData
}
