package ru.raysmith.tgbot

/**
 * This object provides access to orientation data on the device.
 *
 * <img src="https://core.telegram.org/file/400780400662/2/6ziukR8E4pc.4269149/aa2ec0a86b39709a92" />
 */
external interface DeviceOrientation {

    /** Indicates whether device orientation tracking is currently active. */
    val isStarted: Boolean

    /** A boolean that indicates whether or not the device is providing orientation data in absolute values. */
    val absolute: Boolean

    /** The rotation around the Z-axis, measured in radians. */
    val alpha: Float?

    /** The rotation around the X-axis, measured in radians. */
    val beta: Float?

    /** The rotation around the Y-axis, measured in radians. */
    val gamma: Float?

    /**
     * Starts tracking device orientation data using params of type DeviceOrientationStartParams.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully started.
     *
     * @since Bot API 8.0
     */
    val start: (params: DeviceOrientationStartParams, callback: (isTrackingStarted: Boolean) -> Unit) -> DeviceOrientation

    /**
     * Stops tracking device orientation data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     *
     * @since Bot API 8.0
     */
    val stop: (callback: (isTrackingStopped: Boolean) -> Unit) -> DeviceOrientation
}