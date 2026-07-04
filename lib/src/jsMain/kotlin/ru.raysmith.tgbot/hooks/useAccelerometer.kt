package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.EventType

/**
 * This hook provides access to accelerometer data on the device.
 *
 * @see <img src="https://core.telegram.org/file/400780400808/3/4R4bxuff6H0.529743/2a9f6212eaed26d194" />
 * */
fun useAccelerometer(): AccelerometerHookType {

    var accelerometer by useState(webApp.Accelerometer)
    var failed by useState<AccelerometerFailed?>(null)

    useEffectOnce {
        webApp.onEvent(EventType.accelerometerFailed) { payload: AccelerometerFailed ->
            failed = payload
        }
    }

    val onChange = {
        accelerometer = copyOf(webApp.Accelerometer)
    }

    val start: (params: AccelerometerStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)?) -> Unit = { params, callback ->
        accelerometer = copyOf(webApp.Accelerometer.start(params) { isTrackingStarted ->
            if (isTrackingStarted) {
                failed = null
                accelerometer = copyOf(webApp.Accelerometer)
                webApp.onEvent(EventType.accelerometerChanged, onChange)
            }
            callback?.invoke(isTrackingStarted)
        })
    }

    val stop: (callback: ((isTrackingStopped: Boolean) -> Unit)?) -> Unit = { callback ->
        accelerometer = copyOf(webApp.Accelerometer.stop { isTrackingStopped ->
            if (isTrackingStopped) {
                accelerometer = copyOf(webApp.Accelerometer)
                webApp.offEvent(EventType.accelerometerChanged, onChange)
            }
            callback?.invoke(isTrackingStopped)
        })
    }

    return useMemo(accelerometer, failed) {
        AccelerometerHookType(
            failed = failed,
            isStarted = accelerometer.isStarted,
            x = accelerometer.x,
            y = accelerometer.y,
            z = accelerometer.z,
            start = start,
            stop = stop
        )
    }
}

@JsPlainObject
external interface AccelerometerHookType {

    /**
     * Not null if a request to start accelerometer mode fails.
     *
     * Possible values:
     * - [AccelerometerFailedType.UNSUPPORTED] – Accelerometer tracking is not supported on this device or platform
     */
    val failed: AccelerometerFailed?

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
     * */
    val start: (params: AccelerometerStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)?) -> Unit

    /**
     * Stops tracking accelerometer data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     * */
    val stop: (callback: ((isTrackingStopped: Boolean) -> Unit)?) -> Unit
}
