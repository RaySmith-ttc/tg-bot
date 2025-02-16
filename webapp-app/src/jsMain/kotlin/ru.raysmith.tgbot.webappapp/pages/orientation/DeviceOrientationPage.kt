package ru.raysmith.tgbot.webappapp.pages.orientation

import js.objects.jso
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.ReactNode
import react.create
import react.useState
import ru.raysmith.tgbot.DeviceOrientationFailedType
import ru.raysmith.tgbot.hooks.useDeviceOrientation
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.pct

// TODO lockOrientation, unlockOrientation
val DeviceOrientationPage = FC {
    val deviceOrientation = useDeviceOrientation()
    var needAbsolute by useState(false)

    BaseSubPageLayout {
        title = "Device orientation"

        Stack {
            spacing = responsive(1)

            if (deviceOrientation.failed != null) {
                Alert {
                    +when(deviceOrientation.failed!!.error) {
                        DeviceOrientationFailedType.UNSUPPORTED -> "Device orientation tracking is not supported on this device or platform"
                        else -> "Unknown error"
                    }
                    severity = AlertColor.error.unsafeCast<String>()
                }
            } else {
                Table {
                    TableBody {
                        DataDisplayTableRow {
                            title = "isStarted"
                            value = DataDisplayCheckbox.create { checked = deviceOrientation.isStarted }
                        }
                        DataDisplayTableRow {
                            title = "alpha"
                            value = Typography.create { +deviceOrientation.alpha.toString().take(16) }
                        }
                        DataDisplayTableRow {
                            title = "beta"
                            value = Typography.create { +deviceOrientation.beta.toString().take(16) }
                        }
                        DataDisplayTableRow {
                            title = "gamma"
                            value = Typography.create { +deviceOrientation.gamma.toString().take(16) }
                        }
                    }
                }
            }

            FormControlLabel {
                label = ReactNode("Absolute")
                control = Checkbox.create {
                    disabled = deviceOrientation.isStarted
                    checked = needAbsolute
                    onChange = { _, checked ->
                        needAbsolute = checked
                    }
                }
            }

            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(1)
                sx { width = 100.pct }

                Button {
                    +"Start"
                    sx { width = 100.pct }
                    size = Size.large
                    disabled = deviceOrientation.isStarted
                    onClick = {
                        deviceOrientation.start(jso { refreshRate = 100; this.needAbsolute = needAbsolute }) {
                            console.log("isTrackingStarted: $it")
                        }
                    }
                }

                Button {
                    +"Stop"
                    sx { width = 100.pct }
                    size = Size.large
                    disabled = !deviceOrientation.isStarted
                    onClick = {
                        deviceOrientation.stop {
                            console.log("isTrackingStopped: $it")
                        }
                    }
                }
            }
        }
    }
}