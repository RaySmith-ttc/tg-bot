package ru.raysmith.tgbot.webappapp

import js.process
import kotlinx.browser.window
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.useEffectOnce
import ru.raysmith.tgbot.Telegram
import ru.raysmith.tgbot.verify
import web.cssom.NamedColor
import web.dom.document

fun main() {
    val root = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(root).render(App.create())
}

val App = FC<Props> {

    useEffectOnce {
//        window.Telegram.WebApp.enableClosingConfirmation()
//        println("pr")
//        println(process.env.TG_BOT_TOKEN)
//        println(js("process"))
//        println(js("process.env"))
//        println(js("process.env.TG_BOT_TOKEN"))
//        println(process)
    }

    Typography {
        variant = TypographyVariant.body1
        sx { color = NamedColor.white }
//        +"Hello"
//        +"verified: ${window.Telegram.WebApp.verify("1729711415:AAGI8G_ob4RS7SsOr11UCc9vT_Ciw32Zczg")}"

        +"verified: ${window.Telegram.WebApp.verify(process.env.TG_BOT_TOKEN)}"
    }
}

