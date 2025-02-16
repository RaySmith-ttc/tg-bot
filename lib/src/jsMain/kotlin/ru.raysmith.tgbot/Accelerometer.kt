package ru.raysmith.tgbot

/**
 * This object provides access to accelerometer data on the device.
 *
 * <img src="https://core.telegram.org/file/400780400808/3/4R4bxuff6H0.529743/2a9f6212eaed26d194" />
 * */
external interface Accelerometer {

    /** Indicates whether accelerometer tracking is currently active. */
    val isStarted: Boolean

    /** The current acceleration in the X-axis, measured in m/s². */
    val x: Float?

    /** The current acceleration in the Y-axis, measured in m/s². */
    val y: Float?

    /** The current acceleration in the Z-axis, measured in m/s². */
    val z: Float?

    /**
     * Starts tracking accelerometer data using params of type [AccelerometerStartParams].
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully started.
     *
     * @since Bot API 8.0
     * */
    val start: (params: AccelerometerStartParams, callback: (isTrackingStarted: Boolean) -> Unit) -> Accelerometer

    /**
     * Stops tracking accelerometer data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     *
     * @since Bot API 8.0
     * */
    val stop: (callback: (isTrackingStopped: Boolean) -> Unit) -> Accelerometer
}