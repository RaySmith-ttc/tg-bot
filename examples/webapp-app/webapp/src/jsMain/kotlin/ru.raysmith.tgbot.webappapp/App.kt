package ru.raysmith.tgbot.webappapp

import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.provider.SettingsProvider
import ru.raysmith.tgbot.webappapp.provider.ThemeProvider
import ru.raysmith.tgbot.webappapp.router.Router
import web.dom.document

fun main() {
    val root = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(root).render(App.create())
    webApp.ready()
}

val App = FC<Props> {
    SettingsProvider {
        ThemeProvider {
            WebAppGuard {
                Router {

                }
            }
        }
    }
}