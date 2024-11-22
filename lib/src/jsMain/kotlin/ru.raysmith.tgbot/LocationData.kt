package ru.raysmith.tgbot

/**
 * This object contains data about the current location.
 */
external interface LocationData {

    /** Latitude in degrees. */
    val latitude: Float

    /** Longitude in degrees. */
    val longitude: Float

    /** Altitude above sea level in meters. `null` if altitude data is not available on the device. */
    val altitude: Float?

    /**
     * The direction the device is moving in degrees (0 = North, 90 = East, 180 = South, 270 = West).
     * `null` if course data is not available on the device.
     * */
    val course: Float?

    /** The speed of the device in m/s. `null` if speed data is not available on the device. */
    val speed: Float?

    /**
     * Accuracy of the latitude and longitude values in meters.
     * `null` if horizontal accuracy data is not available on the device.
     * */
    @JsName("horizontal_accuracy")
    val horizontalAccuracy: Float?

    /** Accuracy of the altitude value in meters. `null` if vertical accuracy data is not available on the device. */
    @JsName("vertical_accuracy")
    val verticalAccuracy: Float?

    /** Accuracy of the course value in degrees. `null` if course accuracy data is not available on the device. */
    @JsName("course_accuracy")
    val courseAccuracy: Float?

    /** Accuracy of the speed value in m/s. `null` if speed accuracy data is not available on the device. */
    @JsName("speed_accuracy")
    val speedAccuracy: Float?
}