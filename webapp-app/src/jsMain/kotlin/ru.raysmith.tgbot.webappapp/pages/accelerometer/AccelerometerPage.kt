package ru.raysmith.tgbot.webappapp.pages.accelerometer

import js.objects.jso
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Stack
import mui.material.Typography
import mui.system.responsive
import mui.system.sx
import react.FC
import react.create
import ru.raysmith.tgbot.AccelerometerFailedType
import ru.raysmith.tgbot.hooks.useAccelerometer
import ru.raysmith.tgbot.webappapp.components.ButtonsControl
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.AlignItems

val AccelerometerPage = FC {
    val accelerometer = useAccelerometer()

    BaseSubPageLayout {
        title = "Accelerometer"

        Stack {
            spacing = responsive(1)
            sx { alignItems = AlignItems.center }

            if (accelerometer.failed != null) {
                Alert {
                    +when(accelerometer.failed!!.error) {
                        AccelerometerFailedType.UNSUPPORTED -> "Accelerometer tracking is not supported on this device or platform"
                        else -> "Unknown error"
                    }
                    severity = AlertColor.error.unsafeCast<String>()
                }
            } else {
                DataDisplayTable {
                    DataDisplayTableRow {
                        title = "isStarted"
                        value = DataDisplayCheckbox.create { checked = accelerometer.isStarted }
                    }
                    DataDisplayTableRow {
                        title = "x"
                        value = Typography.create { +accelerometer.x.toString().take(16) }
                    }
                    DataDisplayTableRow {
                        title = "y"
                        value = Typography.create { +accelerometer.y.toString().take(16) }
                    }
                    DataDisplayTableRow {
                        title = "z"
                        value = Typography.create { +accelerometer.z.toString().take(16) }
                    }
                }
            }

            ControlsPaperStack {
                title = "Tracking controls"
                ButtonsControl {
                    value = accelerometer.isStarted
                    onStart = {
                        accelerometer.start(jso { refreshRate = 100 }) {
                            console.log("isTrackingStarted: $it")
                        }
                    }
                    onStop = {
                        accelerometer.stop {
                            console.log("isTrackingStopped: $it")
                        }
                    }
                }
            }
        }
    }
}
