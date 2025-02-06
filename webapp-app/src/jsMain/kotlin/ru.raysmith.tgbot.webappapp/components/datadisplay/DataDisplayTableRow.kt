package ru.raysmith.tgbot.webappapp.components.datadisplay

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.PropsWithChildren
import react.ReactNode
import ru.raysmith.tgbot.webappapp.wrappers.pb
import ru.raysmith.tgbot.webappapp.wrappers.pt
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
                if (props.children != null) {
                    borderBottom = None.none
                    pb = 0.5
                }
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
                if (props.children != null) {
                    borderBottom = None.none
                    pb = 1
                }
            }
            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(1)
                sx {
                    justifyItems = JustifyItems.center
                    alignItems = AlignItems.center
                }

                +props.value
            }
        }
    }

    if (props.children != null) {
        TableRow {
            TableCell {
                colSpan = 2
                sx {
                    pt = 0.5
                }

                +props.children
            }
        }
    }
}