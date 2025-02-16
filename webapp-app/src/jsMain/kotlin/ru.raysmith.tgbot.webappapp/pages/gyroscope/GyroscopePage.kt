package ru.raysmith.tgbot.webappapp.pages.gyroscope

import js.objects.jso
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.create
import ru.raysmith.tgbot.GyroscopeFailedType
import ru.raysmith.tgbot.hooks.useGyroscope
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.AlignItems
import web.cssom.pct

val GyroscopePage = FC {
    val gyroscope = useGyroscope()

    BaseSubPageLayout {
        title = "Gyroscope"

        Stack {
            spacing = responsive(1)
            sx { alignItems = AlignItems.center }

            if (gyroscope.failed != null) {
                Alert {
                    +when (gyroscope.failed!!.error) {
                        GyroscopeFailedType.UNSUPPORTED -> "Gyroscope tracking is not supported on this device or platform"
                        else -> "Unknown error"
                    }
                    severity = AlertColor.error.unsafeCast<String>()
                }
            } else {
                DataDisplayTable {
                    DataDisplayTableRow {
                        title = "isStarted"
                        value = DataDisplayCheckbox.create { checked = gyroscope.isStarted }
                    }
                    DataDisplayTableRow {
                        title = "x"
                        value = Typography.create { +gyroscope.x.toString().take(16) }
                    }
                    DataDisplayTableRow {
                        title = "y"
                        value = Typography.create { +gyroscope.y.toString().take(16) }
                    }
                    DataDisplayTableRow {
                        title = "z"
                        value = Typography.create { +gyroscope.z.toString().take(16) }
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
                    disabled = gyroscope.isStarted
                    onClick = {
                        gyroscope.start(jso { refreshRate = 100 }) {
                            console.log("isTrackingStarted: $it")
                        }
                    }
                }

                Button {
                    +"Stop"
                    sx { width = 100.pct }
                    size = Size.large
                    disabled = !gyroscope.isStarted
                    onClick = {
                        gyroscope.stop {
                            console.log("isTrackingStopped: $it")
                        }
                    }
                }
            }
        }
    }
}