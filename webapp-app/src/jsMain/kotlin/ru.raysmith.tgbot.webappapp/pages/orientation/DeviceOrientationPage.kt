package ru.raysmith.tgbot.webappapp.pages.orientation

import js.objects.jso
import mui.material.*
import mui.system.responsive
import react.FC
import react.ReactNode
import react.create
import react.useState
import ru.raysmith.tgbot.DeviceOrientationFailedType
import ru.raysmith.tgbot.hooks.useDeviceOrientation
import ru.raysmith.tgbot.webappapp.components.ButtonsControl
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.ToggleButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val DeviceOrientationPage = FC {
    val deviceOrientation = useDeviceOrientation()
    var needAbsolute by useState(false)

    BaseSubPageLayout {
        title = "Device orientation"

        Stack {
            spacing = responsive(1)

            if (deviceOrientation.failed != null) {
                Alert {
                    +when (deviceOrientation.failed!!.error) {
                        DeviceOrientationFailedType.UNSUPPORTED -> "Device orientation tracking is not supported on this device or platform"
                        else -> "Unknown error"
                    }
                    severity = AlertColor.error.unsafeCast<String>()
                }
            } else {
                DataDisplayTable {
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
                    DataDisplayTableRow {
                        title = "isOrientationLocked"
                        value = DataDisplayCheckbox.create { checked = deviceOrientation.isOrientationLocked }

                        ToggleButtonsGroupControl {
                            value = deviceOrientation.isOrientationLocked
                            items = mapOf(
                                ("Lock" to true) to { deviceOrientation.lockOrientation() },
                                ("Unlock" to false) to { deviceOrientation.unlockOrientation() }
                            )
                        }
                    }
                }
            }

            ControlsPaperStack {
                title = "Tracking controls"
                header = FormControlLabel.create {
                    label = ReactNode("Absolute")
                    control = Checkbox.create {
                        disabled = deviceOrientation.isStarted
                        checked = needAbsolute
                        onChange = { _, checked ->
                            needAbsolute = checked
                        }
                    }
                }

                ButtonsControl {
                    value = deviceOrientation.isStarted
                    onStart = {
                        deviceOrientation.start(jso { refreshRate = 100; this.needAbsolute = needAbsolute }) {
                            console.log("isTrackingStarted: $it")
                        }
                    }
                    onStop = {
                        deviceOrientation.stop {
                            console.log("isTrackingStopped: $it")
                        }
                    }
                }
            }
        }
    }
}

