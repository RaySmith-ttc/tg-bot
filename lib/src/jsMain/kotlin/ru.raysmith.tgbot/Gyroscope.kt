package ru.raysmith.tgbot

/**
 * This object provides access to gyroscope data on the device.
 *
 * <img src="https://core.telegram.org/file/400780400892/5/GDxCwbAAG7U.579631/7895611bc90a998a13" />
 */
external interface Gyroscope {

    /** Indicates whether gyroscope tracking is currently active. */
    val isStarted: Boolean

    /** The current rotation rate around the X-axis, measured in rad/s. */
    val x: Float

    /** The current rotation rate around the Y-axis, measured in rad/s. */
    val y: Float

    /** The current rotation rate around the Z-axis, measured in rad/s. */
    val z: Float

    /**
     * Starts tracking gyroscope data using params of type GyroscopeStartParams.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully started.
     *
     * @since Bot API 8.0
     */
    val start: (params: GyroscopeStartParams, callback: (isTrackingStarted: Boolean) -> Unit) -> Gyroscope

    /**
     * Stops tracking gyroscope data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     *
     * @since Bot API 8.0
     */
    val stop: (callback: (isTrackingStopped: Boolean) -> Unit) -> Gyroscope
}