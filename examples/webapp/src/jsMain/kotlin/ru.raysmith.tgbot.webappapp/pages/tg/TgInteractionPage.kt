package ru.raysmith.tgbot.webappapp.pages.tg

import kotlinx.coroutines.launch
import mui.material.Alert
import mui.material.Button
import mui.material.StackDirection
import mui.material.TextField
import mui.material.Typography
import mui.system.responsive
import mui.system.sx
import react.FC
import react.ReactNode
import react.dom.onChange
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.DownloadFileParams
import ru.raysmith.tgbot.EmojiStatusParams
import ru.raysmith.tgbot.PopupParams
import ru.raysmith.tgbot.PopupButton
import ru.raysmith.tgbot.PopupButtonType
import ru.raysmith.tgbot.StoryShareParams
import ru.raysmith.tgbot.StoryWidgetLink
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.API
import ru.raysmith.tgbot.webappapp.ApiException
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.DefaultValuesHintAlert
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.mainScope
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.pct
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
                        webApp.showPopup(PopupParams(
                                title = "Contact shared",
                                message = "You shared your contact with the bot. Check your bot's chat.",
                                buttons = arrayOf(PopupButton(
                                    type = PopupButtonType.ok,
                                    text = "Ok",
                                )),
                            )
                        )
                    }
                }
            }
        }

        ControlsPaperStack {
            direction = responsive(StackDirection.column)

            Alert {
                +"This feature works only if the mini app was opened from reply keyboard button"
                severity = mui.material.AlertColor.info.unsafeCast<String>()
                sx {
                    width = 100.pct
                }
            }

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
                    try {
                        val link = API.createInvoiceLink()
                        webApp.openInvoice(link) {
                            println("Invoice was opened. Status: $it")
                        }
                    } catch (e: ApiException) {
                        webApp.showPopup(PopupParams(
                            title = "Error",
                            message = e.message,
                            buttons = arrayOf(PopupButton(
                                type = PopupButtonType.ok,
                                text = "Ok",
                            )),
                        ))
                    } catch (e: Exception) {
                        println("Unknown error: ${e.message}")
                    } finally {
                        isCreateInvoiceLinkLoading = false
                    }
                }
            }
        }

        Button {
            +"Share to story"
            applyControlButtonStyle()
            onClick = {
                webApp.shareToStory(window.location.origin + "/image.jpg", StoryShareParams(
                    text = "Some text",
                    widgetLink = StoryWidgetLink(
                        url = "https://google.com",
                        name = "Google link",
                    ),
                ))
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
                        // TODO is emoji exist at prod server?
                        webApp.setEmojiStatus("5062070836140179457", EmojiStatusParams(duration = 20)) { isStatusSet ->
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
                webApp.downloadFile(DownloadFileParams(
                    url = if (window.asDynamic().isSecureContext != true) {
                        "https://i.ytimg.com/vi/aKnX5wci404/maxresdefault.jpg"
                    } else {
                        window.location.origin + "/image.jpg"
                    },
                    fileName = "image.jpg",
                )) { granted: Boolean ->
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
