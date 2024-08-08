package ru.raysmith.tgbot

external interface OpenLinkOptions {

    @JsName("try_instant_view")
    var tryInstantView: Boolean

    @JsName("try_browser")
    var tryBrowser: Boolean
}