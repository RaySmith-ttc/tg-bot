package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffect
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.EventType
import web.scheduling.VoidFunction

/**
 * This hook controls location access on the device.
 * Before the first use of this object, it needs to be initialized using the [init] method.
 */
fun useLocationManager(): LocationManagerHookType {
    var locationManager by useState(webApp.LocationManager)
    var locationData by useState<LocationData?>(null)

    useEffect(locationManager.isInited) {
        if (locationManager.isInited) {
            webApp.onEvent(EventType.locationManagerUpdated) {
                locationManager = copyOf(webApp.LocationManager)
            }
        }
    }

    val init = { callback: (() -> Unit)? ->
        locationManager = copyOf(webApp.LocationManager.init {
            locationManager = copyOf(webApp.LocationManager)
            callback?.invoke()
        })
    }

    val getLocation = { callback: ((LocationData?) -> Unit)? ->
        locationManager = copyOf(webApp.LocationManager.getLocation { ld ->
            locationManager = copyOf(webApp.LocationManager)
            locationData = ld
            callback?.invoke(ld)
        })
    }

    val openSettings = {
        locationManager = copyOf(webApp.LocationManager.openSettings())
    }

    return useMemo(locationManager, locationData) {
        jso {
            this.locationData = locationData
            isInited = locationManager.isInited
            isLocationAvailable = locationManager.isLocationAvailable
            isAccessRequested = locationManager.isAccessRequested
            isAccessGranted = locationManager.isAccessGranted
            this.init = init
            this.getLocation = getLocation
            this.openSettings = openSettings
        }
    }
}

external interface LocationManagerHookType {

    /** This object contains data about the current location. */
    var locationData: LocationData?

    /** Shows whether the [LocationManager] object has been initialized. */
    var isInited: Boolean

    /** Shows whether location services are available on the current device. */
    var isLocationAvailable: Boolean

    /** Shows whether permission to use location has been requested. */
    var isAccessRequested: Boolean

    /** Shows whether permission to use location has been granted. */
    var isAccessGranted: Boolean

    /**
     * A method that initializes the [LocationManager] object. It should be called before the object's first use.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called when the object is initialized.
     */
    var init: (callback: VoidFunction?) -> Unit

    /** A method that requests and update location data. */
    var getLocation: (callback: ((locationData: LocationData?) -> Unit)?) -> Unit

    /**
     * A method that opens the location access settings for bots. Useful when you need to request location access from
     * users who haven't granted it yet.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g., a click inside the Mini App or on the main button).*
     */
    var openSettings: VoidFunction
}