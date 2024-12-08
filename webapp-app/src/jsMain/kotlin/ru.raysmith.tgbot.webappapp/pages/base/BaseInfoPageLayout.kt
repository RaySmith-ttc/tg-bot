package ru.raysmith.tgbot.webappapp.pages.base

import mui.material.*
import react.*
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val BaseInfoPage = FC<Props> {
    BaseSubPageLayout {
        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "Bot API version"
                    value = Typography.create { +webApp.version }
                }
                DataDisplayTableRow {
                    title = "Platform"
                    value = Typography.create { +webApp.platform }
                }
            }
        }
    }
}