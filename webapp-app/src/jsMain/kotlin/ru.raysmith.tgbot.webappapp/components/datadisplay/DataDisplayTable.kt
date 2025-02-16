package ru.raysmith.tgbot.webappapp.components.datadisplay

import mui.material.Table
import mui.material.TableBody
import mui.material.TableProps
import mui.system.sx
import react.FC
import ru.raysmith.tgbot.webappapp.other
import web.cssom.TableLayout
import web.cssom.pct

// TODO replace Table>TableBody to DataDisplayTable
val DataDisplayTable = FC<TableProps> { props ->
    Table {
        sx {
            width = 100.pct
            tableLayout = TableLayout.fixed
        }

        +props.other("children")

        TableBody {
            +props.children
        }
    }
}