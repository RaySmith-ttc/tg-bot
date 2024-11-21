package ru.raysmith.tgbot.webappapp.components.datadisplay

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.PropsWithChildren
import react.ReactNode
import web.cssom.WhiteSpace
import web.cssom.WordWrap
import web.cssom.pct

external interface BaseInfoPageProps : PropsWithChildren {
    var title: String
    var value: ReactNode
}

val DataDisplayTableRow = FC<BaseInfoPageProps> { props ->
    TableRow {
        sx {
            width = 50.pct
            wordWrap = WordWrap.breakWord
            whiteSpace = WhiteSpace.normal
        }

        TableCell {
            sx {
                width = 50.pct
                wordWrap = WordWrap.breakWord
                whiteSpace = WhiteSpace.normal
            }

            Typography {
                +props.title
            }
        }
        TableCell {
            sx {
                width = 50.pct
                wordWrap = WordWrap.breakWord
                whiteSpace = WhiteSpace.normal
            }
            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(1)
                
                Typography {
                    +props.value
                }

                +props.children
            }
        }
    }
}