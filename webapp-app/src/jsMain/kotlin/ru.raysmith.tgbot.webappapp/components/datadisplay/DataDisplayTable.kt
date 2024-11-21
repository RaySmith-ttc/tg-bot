package ru.raysmith.tgbot.webappapp.components.datadisplay

import mui.material.Table
import mui.material.TableProps
import mui.system.sx
import react.FC
import web.cssom.TableLayout
import web.cssom.pct

val DataDisplayTable = FC<TableProps> { props ->
    Table {
        sx {
            width = 100.pct
            tableLayout = TableLayout.fixed
        }

        +props
    }
}