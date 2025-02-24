package ru.raysmith.tgbot.webappapp

import react.dom.events.MouseEvent
import web.html.HTMLButtonElement

typealias OnClick = (OnClickEvent?) -> Unit
typealias OnClickEvent = MouseEvent<HTMLButtonElement, *>

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
fun String.decapitalize() = replaceFirstChar { it.lowercase() }

fun String.camelCaseToSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase()
}

fun <T> T?.isNullOrUndefined() = this == null || this == undefined
fun <T> T?.isNotNullOrUndefined() = !isNullOrUndefined()