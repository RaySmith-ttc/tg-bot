package ru.raysmith.tgbot.webappapp

import mui.material.CssBaseline
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import ru.raysmith.tgbot.webappapp.router.Router
import web.dom.document

fun main() {
    val root = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(root).render(App.create())
}

val App = FC<Props> {
    CssBaseline()
    WebAppGuard {
        Router {

        }
    }
}