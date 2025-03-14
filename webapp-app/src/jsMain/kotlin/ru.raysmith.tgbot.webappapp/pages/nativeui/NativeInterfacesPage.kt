package ru.raysmith.tgbot.webappapp.pages.nativeui

import js.objects.jso
import kotlinx.coroutines.launch
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Button
import mui.material.StackDirection
import mui.system.responsive
import react.FC
import react.ReactNode
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.PopupButtonType
import ru.raysmith.tgbot.events.EventType
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTextField
import ru.raysmith.tgbot.webappapp.mainScope
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.utils.generateRandomString
import web.navigator.navigator
import web.window.window

val NativeInterfacesPage = FC {

    var qrCodeData by useState<String?>(null)
    var textFromClipboard by useState<String?>(null)

    val isHttps = window.asDynamic().isSecureContext == true

    useEffectOnce {
        webApp.onEvent(EventType.popupClosed) {
            println("EventType.popupClosed: ${JSON.stringify(it)}")
        }

        webApp.onEvent(EventType.qrTextReceived) { payload ->
            println("EventType.qrTextReceived: ${JSON.stringify(payload)}")
        }

        webApp.onEvent(EventType.clipboardTextReceived) { payload ->
            println("EventType.clipboardTextReceived: ${JSON.stringify(payload)}")
        }
    }

    BaseSubPageLayout {
        title = "Native interfaces"

        ControlsPaperStack {
            direction = responsive(StackDirection.column)

            Button {
                +"Show popup #1"
                applyControlButtonStyle()
                onClick = {
                    webApp.showPopup(jso {
                        title = "Tilte"
                        message = "Mesage"
                        buttons = arrayOf(jso {
                            id = "b1"
                            type = PopupButtonType.ok
                            text = "ok"
                        },jso {
                            id = "b2"
                            type = PopupButtonType.cancel
                            text = "cancel"
                        },jso {
                            id = "b3"
                            type = PopupButtonType.default
                            text = "default"
                        },)
                    }) {
                        println("Popup was closed by $it")
                    }
                }
            }

            Button {
                +"Show popup #2"
                applyControlButtonStyle()
                onClick = {
                    webApp.showPopup(jso {
                        title = "Title"
                        message = "Message"
                        buttons = arrayOf(jso {
                            id = "b1"
                            type = PopupButtonType.destructive
                            text = "destructive"
                        },jso {
                            id = "b2"
                            type = PopupButtonType.close
                            text = "close"
                        })
                    }) {
                        println("Popup was closed by $it")
                    }
                }
            }

            Button {
                +"Show alert"
                applyControlButtonStyle()
                onClick = {
                    webApp.showAlert("Message") {
                        println("Alert was closed")
                    }
                }
            }
        }

        ControlsPaperStack {
            direction = responsive(StackDirection.column)

            DataDisplayTextField {
                label = ReactNode("QR code data")
                value = qrCodeData
                fullWidth = true
                copyToClipboard = true
            }

            Button {
                +"Show Scan QR popup"
                applyControlButtonStyle()
                onClick = {
                    webApp.showScanQrPopup(jso {
                        text = "Text"
                    }) {
                        println("QR data: $it")
                        qrCodeData = it
                        true
                    }
                }
            }
        }

        ControlsPaperStack {
            direction = responsive(StackDirection.column)

            DataDisplayTextField {
                disabled = !isHttps
                label = ReactNode("Text from clipboard")
                value = textFromClipboard
                fullWidth = true
            }

            Button {
                +"Copy random string to clipboard"
                applyControlButtonStyle()
                disabled = !isHttps
                onClick = {
                    mainScope.launch {
                        navigator.clipboard.writeText(generateRandomString(8))
                    }
                }
            }

            Button {
                +"Read text from clipboard"
                applyControlButtonStyle()
                disabled = !isHttps
                onClick = {
                    webApp.readTextFromClipboard {
                        textFromClipboard = it
                    }
                }
            }

            if (!isHttps) {
                Alert {
                    +"This features are available only in secure context (HTTPS)"
                    severity = AlertColor.info.unsafeCast<String>()
                }
            }
        }

        Button {
            +"test"
            applyControlButtonStyle()
            onClick = {
                webApp.requestContact {
                    println("isUserSharedItsPhoneNumber: $it")
                }
            }
        }
    }
}