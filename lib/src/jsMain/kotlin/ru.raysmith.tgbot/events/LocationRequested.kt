package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.LocationData

/**
 * @property locationData Contains the current location information
 * */
external interface LocationRequested {

    /** Contains the current location information */
    val locationData: LocationData
}