package ru.raysmith.tgbot.webappapp.components.datadisplay

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.PropsWithChildren
import react.ReactNode
import web.cssom.*

external interface BaseInfoPageProps : PropsWithChildren {
    var title: String
    var value: ReactNode
    var bigTitleWidth: Boolean?
}

val DataDisplayTableRow = FC<BaseInfoPageProps> { props ->
    TableRow {
        TableCell {
            sx {
                width = if (props.bigTitleWidth == true) 75.pct else 50.pct
                wordWrap = WordWrap.breakWord
                whiteSpace = WhiteSpace.normal
                overflowWrap = OverflowWrap.anywhere
            }

            Typography {
                +props.title
            }
        }
        TableCell {
            sx {
                wordWrap = WordWrap.breakWord
                whiteSpace = WhiteSpace.normal
                overflowWrap = OverflowWrap.anywhere
            }
            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(1)
                sx {
                    justifyItems = JustifyItems.center
                }

                +props.value

                +props.children
            }
        }
    }
}