package ru.raysmith.tgbot.webappapp.pages.location

import mui.icons.material.Settings
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Button
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.Stack
import mui.system.StackDirection
import mui.system.responsive
import mui.system.sx
import react.FC
import react.create
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useLocationManager
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.tgbot.webappapp.wrappers.pt
import web.cssom.AlignItems

val LocationManagerPage = FC {
    val locationManager = useLocationManager()

    useEffectOnce {
        locationManager.init(null)
    }

    BaseSubPageLayout {
        title = "Location manager"


        Stack {
            spacing = responsive(1)

            Typography {
                +"Manager state"
                variant = TypographyVariant.h6
                sx { pt = 2 }
            }

            DataDisplayTable {
                DataDisplayTableRow {
                    title = "isInited"
                    value = DataDisplayCheckbox.create { checked = locationManager.isInited }
                }
                DataDisplayTableRow {
                    title = "isLocationAvailable"
                    value = DataDisplayCheckbox.create { checked = locationManager.isLocationAvailable }
                }
                DataDisplayTableRow {
                    title = "isAccessRequested"
                    value = DataDisplayCheckbox.create { checked = locationManager.isAccessRequested }
                }
                DataDisplayTableRow {
                    title = "isAccessGranted"
                    value = DataDisplayCheckbox.create { checked = locationManager.isAccessGranted }
                }
            }

            if (!locationManager.isLocationAvailable) {
                Alert {
                    +"LocationManager not available on this device. To access the additional features on this page, use a device with a GPS module."
                    severity = AlertColor.error.unsafeCast<String>()
                }
                return@Stack
            }

            if (!locationManager.isAccessGranted) {
                Stack {
                    spacing = responsive(1)

                    Alert {
                        +"The bot does not have permission for location. Go to the settings and grant permission to use it."
                        severity = AlertColor.warning.unsafeCast<String>()
                    }

                    Button {
                        +"Open settings"
                        applyControlButtonStyle()
                        startIcon = Settings.create()
                        onClick = {
                            locationManager.openSettings()
                        }
                    }
                }
                return@Stack
            }

            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(1)
                sx {
                    alignItems = AlignItems.baseline
                }

                Typography {
                    +"Location data"
                    variant = TypographyVariant.h6
                    sx { pt = 2 }
                }

                Button {
                    +"Update"
                    disabled = !locationManager.isInited
                    onClick = {
                        locationManager.getLocation(null)
                    }
                }
            }

            DataDisplayTable {
                DataDisplayTableRow {
                    title = "latitude"
                    value = Typography.create { +locationManager.locationData?.latitude.toString() }
                }
                DataDisplayTableRow {
                    title = "longitude"
                    value = Typography.create { +locationManager.locationData?.longitude.toString() }
                }
                DataDisplayTableRow {
                    title = "altitude"
                    value = Typography.create { +locationManager.locationData?.altitude.toString() }
                }
                DataDisplayTableRow {
                    title = "course"
                    value = Typography.create { +locationManager.locationData?.course.toString() }
                }
                DataDisplayTableRow {
                    title = "speed"
                    value = Typography.create { +locationManager.locationData?.speed.toString() }
                }
                DataDisplayTableRow {
                    title = "horizontalAccuracy"
                    value = Typography.create { +locationManager.locationData?.horizontalAccuracy.toString() }
                }
                DataDisplayTableRow {
                    title = "verticalAccuracy"
                    value = Typography.create { +locationManager.locationData?.verticalAccuracy.toString() }
                }
                DataDisplayTableRow {
                    title = "courseAccuracy"
                    value = Typography.create { +locationManager.locationData?.courseAccuracy.toString() }
                }
                DataDisplayTableRow {
                    title = "speedAccuracy"
                    value = Typography.create { +locationManager.locationData?.speedAccuracy.toString() }
                }
            }

        }
    }
}