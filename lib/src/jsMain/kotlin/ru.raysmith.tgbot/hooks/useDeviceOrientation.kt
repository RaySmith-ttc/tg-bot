package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*

/**
 * This hook provides access to orientation data on the device.
 *
 * <img src="https://core.telegram.org/file/400780400662/2/6ziukR8E4pc.4269149/aa2ec0a86b39709a92" />
 */
fun useDeviceOrientation(): DeviceOrientationHookType {
    var deviceOrientation by useState(webApp.DeviceOrientation)
    var failed by useState<DeviceOrientationFailed?>(null)

    useEffectOnce {
        webApp.onEvent(EventType.deviceOrientationFailed) { payload: DeviceOrientationFailed ->
            failed = payload
        }
    }

    val onChange: (dynamic) -> Unit = {
        deviceOrientation = copyOf(webApp.DeviceOrientation)
    }

    val start = { params: DeviceOrientationStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)? ->
        deviceOrientation = copyOf(webApp.DeviceOrientation.start(params) { isTrackingStarted ->
            if (isTrackingStarted) {
                failed = null
                deviceOrientation = copyOf(webApp.DeviceOrientation)
                webApp.onEvent(EventType.deviceOrientationChanged, onChange)
            }
            callback?.invoke(isTrackingStarted)
        })
    }

    val stop: (callback: ((isTrackingStopped: Boolean) -> Unit)?) -> Unit = { callback ->
        deviceOrientation = copyOf(webApp.DeviceOrientation.stop { isTrackingStopped ->
            if (isTrackingStopped) {
                deviceOrientation = copyOf(webApp.DeviceOrientation)
                webApp.offEvent(EventType.deviceOrientationChanged, onChange)
            }
            callback?.invoke(isTrackingStopped)
        })
    }

    return useMemo(deviceOrientation, failed) {
        jso {
            this.failed = failed
            this.isStarted = deviceOrientation.isStarted
            this.absolute = deviceOrientation.absolute
            this.alpha = deviceOrientation.alpha
            this.beta = deviceOrientation.beta
            this.gamma = deviceOrientation.gamma
            this.start = start
            this.stop = stop
        }
    }
}

external interface DeviceOrientationHookType {

    /**
     * Not null if a request to start device orientation tracking fails.
     *
     * Possible values:
     * - [DeviceOrientationFailedType.UNSUPPORTED] – Device orientation tracking is not supported on this device or
     * platform
     */
    var failed: DeviceOrientationFailed?

    /** Indicates whether device orientation tracking is currently active. */
    var isStarted: Boolean

    /** A boolean that indicates whether or not the device is providing orientation data in absolute values. */
    var absolute: Boolean

    /** The rotation around the Z-axis, measured in radians. */
    var alpha: Float?

    /** The rotation around the X-axis, measured in radians. */
    var beta: Float?

    /** The rotation around the Y-axis, measured in radians. */
    var gamma: Float?

    /**
     * Starts tracking device orientation data using params of type DeviceOrientationStartParams.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully started.
     */
    var start: (params: DeviceOrientationStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)?) -> Unit

    /**
     * Stops tracking device orientation data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     */
    var stop: (callback: ((isTrackingStopped: Boolean) -> Unit)?) -> Unit
}