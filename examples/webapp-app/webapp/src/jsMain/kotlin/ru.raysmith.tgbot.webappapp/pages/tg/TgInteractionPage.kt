package ru.raysmith.tgbot.webappapp.pages.tg

import js.objects.jso
import kotlinx.coroutines.launch
import mui.material.Button
import mui.material.StackDirection
import mui.material.TextField
import mui.system.responsive
import react.FC
import react.ReactNode
import react.dom.onChange
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.PopupButtonType
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.API
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.mainScope
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.window.window

val TgInteractionPage = FC {
    var textToSendData by useState<String?>(null)
    var isCreateInvoiceLinkLoading by useState(false)
    var isShareMessageLoading by useState(false)

    useEffectOnce {  }

    BaseSubPageLayout {
        title = "Interaction with Telegram"

        Button {
            +"Share contact with bot"
            applyControlButtonStyle()
            onClick = {
                webApp.requestContact {
                    println("isUserSharedItsPhoneNumber: $it")
                    if (it) {
                        webApp.showPopup(jso {
                            title = "Contact shared"
                            message = "You shared your contact with the bot. Check your bot's chat."
                            buttons = arrayOf(jso {
                                type = PopupButtonType.ok
                                text = "Ok"
                            })
                        })
                    }
                }
            }
        }

        ControlsPaperStack {
            direction = responsive(StackDirection.column)

            TextField {
                label = ReactNode("Text to send")
                value = textToSendData
                onChange = { event ->
                    textToSendData = event.target.asDynamic().value.unsafeCast<String>().take(4096) // TODO create object with limits
                }
            }

            Button {
                +"Send data to bot"
                applyControlButtonStyle()
                disabled = textToSendData.isNullOrEmpty()
                onClick = {
                    webApp.sendData(textToSendData!!)
                }
            }
        }

        Button {
            +"Switch inline query"
            applyControlButtonStyle()
            onClick = {
                webApp.switchInlineQuery("Some query")
            }
        }

        Button {
            +"Create invoice"
            applyControlButtonStyle()
            asDynamic().loading = isCreateInvoiceLinkLoading
            asDynamic().loadingPosition = "end"
            onClick = {
                isCreateInvoiceLinkLoading = true
                mainScope.launch {
                    val link = API.createInvoiceLink()
                    webApp.openInvoice(link) {
                        println("Invoice was opened. Status: $it")
                    }
                    isCreateInvoiceLinkLoading = false
                }
            }
        }

        Button {
            +"Share to story"
            applyControlButtonStyle()
            onClick = {
                webApp.shareToStory(window.location.origin + "/image.jpg", jso {
                    text = "Some text"
                    widgetLink = jso {
                        url = "https://google.com"
                        name = "Google link"
                    }
                })
            }
        }

        Button {
            +"Share message"
            applyControlButtonStyle()
            onClick = {
                isShareMessageLoading = true
                mainScope.launch {
                    val id = API.preparedMessageId(webApp.initDataUnsafe.user!!.id)
                    webApp.shareMessage(id) { sent: Boolean ->
                        println("shareMessage sent: $sent")
                    }
                }
            }
        }

        Button {
            +"Set emoji status"
            applyControlButtonStyle()
            onClick = {
                webApp.requestEmojiStatusAccess { granted ->
                    if (granted) {
                        webApp.setEmojiStatus("5062070836140179457", jso { // TODO is emoji exist at prod server?
                            duration = 20
                        }) { isStatusSet ->
                            println("isStatusSet: $isStatusSet")
                        }
                    }
                }
            }
        }

        Button {
            +"Download file"
            applyControlButtonStyle()
            onClick = {
                webApp.downloadFile(jso {
                    url = if (window.asDynamic().isSecureContext != true) {
                        "https://i.ytimg.com/vi/aKnX5wci404/maxresdefault.jpg"
                    } else {
                        window.location.origin + "/image.jpg"
                    }
                    fileName = "image.jpg"
                }) { granted: Boolean ->
                    println("Download file granted: $granted")
                }
            }
        }

        Button {
            +"Request write access"
            applyControlButtonStyle()
            onClick = {
                webApp.requestWriteAccess {
                    println("Request write access granted: $it")
                }
            }
        }

        Button {
            +"Close Mini App"
            applyControlButtonStyle()
            onClick = {
                webApp.close()
            }
        }
    }
}