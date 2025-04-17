package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.EventType

/**
 * This hook provides access to gyroscope data on the device.
 *
 * <img src="https://core.telegram.org/file/400780400892/5/GDxCwbAAG7U.579631/7895611bc90a998a13" />
 */
fun useGyroscope(): GyroscopeHookType {

    var gyroscope by useState(webApp.Gyroscope)
    var failed by useState<GyroscopeFailed?>(null)

    useEffectOnce {
        webApp.onEvent(EventType.gyroscopeFailed) { payload: GyroscopeFailed ->
            failed = payload
        }
    }

    val onChange = {
        gyroscope = copyOf(webApp.Gyroscope)
    }

    val start: (params: GyroscopeStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)?) -> Unit = { params, callback ->
        gyroscope = copyOf(webApp.Gyroscope.start(params) { isTrackingStarted ->
            if (isTrackingStarted) {
                failed = null
                gyroscope = copyOf(webApp.Gyroscope)
                webApp.onEvent(EventType.gyroscopeChanged, onChange)
            }
            callback?.invoke(isTrackingStarted)
        })
    }

    val stop: (callback: ((isTrackingStopped: Boolean) -> Unit)?) -> Unit = { callback ->
        gyroscope = copyOf(webApp.Gyroscope.stop { isTrackingStopped ->
            if (isTrackingStopped) {
                gyroscope = copyOf(webApp.Gyroscope)
                webApp.offEvent(EventType.gyroscopeChanged, onChange)
            }
            callback?.invoke(isTrackingStopped)
        })
    }

    return useMemo(gyroscope, failed) {
        jso {
            this.failed = failed
            this.isStarted = gyroscope.isStarted
            this.x = gyroscope.x
            this.y = gyroscope.y
            this.z = gyroscope.z
            this.start = start
            this.stop = stop
        }
    }
}


external interface GyroscopeHookType {

    /**
     * Not null if a request to start gyroscope tracking fails.
     *
     * Possible values:
     * - [GyroscopeFailedType.UNSUPPORTED] – Gyroscope tracking is not supported on this device or platform
     */
    var failed: GyroscopeFailed?

    /** Indicates whether gyroscope tracking is currently active. */
    var isStarted: Boolean

    /** The current rotation rate around the X-axis, measured in rad/s. */
    var x: Float?

    /** The current rotation rate around the Y-axis, measured in rad/s. */
    var y: Float?

    /** The current rotation rate around the Z-axis, measured in rad/s. */
    var z: Float?

    /**
     * Starts tracking gyroscope data using params of type GyroscopeStartParams.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully started.
     */
    var start: (params: GyroscopeStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)?) -> Unit

    /**
     * Stops tracking gyroscope data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     */
    var stop: (callback: (isTrackingStopped: Boolean) -> Unit) -> Unit
}