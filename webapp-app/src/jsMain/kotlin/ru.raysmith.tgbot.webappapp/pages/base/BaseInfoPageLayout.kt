package ru.raysmith.tgbot.webappapp.pages.base

import mui.material.*
import react.*
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.hooks.useViewport
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.components.BottomAppBar
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val BaseInfoPage = FC<Props> {
    BaseSubPageLayout {
        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "Bot API version"
                    value = ReactNode(webApp.version)
                }
                DataDisplayTableRow {
                    title = "Platform"
                    value = ReactNode(webApp.platform)
                }
            }
        }
    }
}