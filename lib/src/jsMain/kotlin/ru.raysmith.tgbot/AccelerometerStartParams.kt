package ru.raysmith.tgbot

/** This object defines the parameters for starting accelerometer tracking. */
external interface AccelerometerStartParams {

    /**
     * The refresh rate in milliseconds, with acceptable values ranging from 20 to 1000.
     * Set to `1000` by default. Note that [refreshRate] may not be supported on all platforms,
     * so the actual tracking frequency may differ from the specified value.
     * */
    @JsName("refresh_rate")
    var refreshRate: Int?
}