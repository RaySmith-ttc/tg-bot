package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.EventType
import web.function.VoidFunction

/**
 * This hook provides access to orientation data on the device.
 *
 * <img src="https://core.telegram.org/file/400780400662/2/6ziukR8E4pc.4269149/aa2ec0a86b39709a92" />
 */
fun useDeviceOrientation(): DeviceOrientationHookType {
    var isOrientationLocked by useState(webApp.isOrientationLocked)
    var deviceOrientation by useState(webApp.DeviceOrientation)
    var failed by useState<DeviceOrientationFailed?>(null)

    useEffectOnce {
        webApp.onEvent(EventType.deviceOrientationFailed) { payload ->
            failed = payload
        }
    }

    val onChange = {
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

    val lockOrientation = {
        webApp.lockOrientation()
        isOrientationLocked = webApp.isOrientationLocked
    }

    val unlockOrientation = {
        webApp.unlockOrientation()
        isOrientationLocked = webApp.isOrientationLocked
    }

    return useMemo(deviceOrientation, failed, isOrientationLocked) {
        DeviceOrientationHookType(
            failed = failed,
            isStarted = deviceOrientation.isStarted,
            absolute = deviceOrientation.absolute,
            alpha = deviceOrientation.alpha,
            beta = deviceOrientation.beta,
            gamma = deviceOrientation.gamma,
            start = start,
            stop = stop,
            isOrientationLocked = isOrientationLocked,
            lockOrientation = lockOrientation,
            unlockOrientation = unlockOrientation,
        )
    }
}

@JsPlainObject
external interface DeviceOrientationHookType {

    /**
     * Not null if a request to start device orientation tracking fails.
     *
     * Possible values:
     * - [DeviceOrientationFailedType.UNSUPPORTED] – Device orientation tracking is not supported on this device or
     * platform
     */
    val failed: DeviceOrientationFailed?

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
     */
    val start: (params: DeviceOrientationStartParams, callback: ((isTrackingStarted: Boolean) -> Unit)?) -> Unit

    /**
     * Stops tracking device orientation data.
     * If an optional `callback` parameter is provided,
     * the `callback` function will be called with a boolean indicating whether tracking was successfully stopped.
     */
    val stop: (callback: ((isTrackingStopped: Boolean) -> Unit)?) -> Unit

    /**
     * *True*, if the Mini App’s orientation is currently locked.
     * *False*, if orientation changes freely based on the device’s rotation.
     * */
    val isOrientationLocked : Boolean

    /**
     * A method that locks the Mini App’s orientation to its current mode (either portrait or landscape). Once locked,
     * the orientation remains fixed, regardless of device rotation. This is useful if a stable orientation is needed
     * during specific interactions.
     * */
    val lockOrientation: VoidFunction

    /**
     * A method that unlocks the Mini App’s orientation, allowing it to follow the device's rotation freely. Use this to
     * restore automatic orientation adjustments based on the device orientation.
     * */
    val unlockOrientation: VoidFunction
}
