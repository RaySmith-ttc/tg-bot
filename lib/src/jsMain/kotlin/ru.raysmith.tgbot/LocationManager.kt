package ru.raysmith.tgbot

/**
 * This object controls location access on the device.
 * Before the first use of this object, it needs to be initialized using the [init] method.
 */
external interface LocationManager {

    /** Shows whether the [LocationManager] object has been initialized. */
    val isInited: Boolean

    /** Shows whether location services are available on the current device. */
    val isLocationAvailable: Boolean

    /** Shows whether permission to use location has been requested. */
    val isAccessRequested: Boolean

    /** Shows whether permission to use location has been granted. */
    val isAccessGranted: Boolean

    /**
     * A method that initializes the [LocationManager] object. It should be called before the object's first use.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called when the object is initialized.
     *
     * @since Bot API 8.0
     */
    val init: (callback: (() -> Unit)?) -> LocationManager

    /**
     * A method that requests location data. The `callback` function will be called with `null` as the first argument
     * if access to location was not granted, or an object of type [LocationData] as the first argument if access
     * was successful.
     *
     * @since Bot API 8.0
     */
    val getLocation: (callback: (locationData: LocationData?) -> Unit) -> LocationManager

    /**
     * A method that opens the location access settings for bots. Useful when you need to request location access from
     * users who haven't granted it yet.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g., a click inside the Mini App or on the main button).*
     *
     * @since Bot API 8.0
     */
    val openSettings: () -> LocationManager
}