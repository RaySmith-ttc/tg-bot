package ru.raysmith.tgbot.webappapp.pages

import js.date.Date
import kotlinx.browser.window
import mui.material.*
import react.FC
import react.Props
import react.useState
import ru.raysmith.tgbot.EventType
import ru.raysmith.tgbot.Telegram
import ru.raysmith.tgbot.hooks.useViewportChanged
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.Header

val BaseInfoPage = FC<Props> {
    var state by useState(0)
    Header {

    }

//    webApp.onEvent(EventType.viewportChanged) {
//        println(JSON.stringify(it))
//    }

//    webApp.onEvent("viewportChanged".asDynamic()) {
//        println(JSON.stringify(it))
//        println(webApp.viewportHeight.toString())
//        println(window.Telegram.WebApp.viewportHeight.toString())
////        state += 1
//    }

    val vc = useViewportChanged()

    Table {
        TableBody {
            BaseInfoPageRow {
                title = "Bot API version"
                value = webApp.version
            }
            BaseInfoPageRow {
                title = "Platform"
                value = webApp.platform
            }
            BaseInfoPageRow {
                title = "Color scheme"
                value = webApp.colorScheme.toString()
            }
            BaseInfoPageRow {
                title = "isExpanded"
                value = vc.isExpanded.toString()
            }
            BaseInfoPageRow {
                title = "viewportHeight"
                value = vc.viewportHeight.toString()
            }
            BaseInfoPageRow {
                title = "viewportStableHeight"
                value = vc.viewportStableHeight.toString()
            }
            BaseInfoPageRow {
                title = "inc"
                value = state.toString()
            }
        }
    }
}

private external interface BaseInfoPageProps : Props {
    var title: String
    var value: String
}

private val BaseInfoPageRow = FC<BaseInfoPageProps> { props ->
    TableRow {
        TableCell {
           Typography {
               +props.title
           }
        }
        TableCell {
            Typography {
                +props.value
            }
        }
    }
}