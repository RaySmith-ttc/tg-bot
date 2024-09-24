package ru.raysmith.tgbot

import seskar.js.JsValue

external interface OpenLinkOptions {

    /** If `true` the link will be opened in [Instant View](https://instantview.telegram.org/) mode if possible */
    @JsName("try_instant_view")
    var tryInstantView: Boolean

    /**
     * *String | Browser*
     *
     * @see browser
     * */
    @Deprecated("Not persist in documentation")
    @JsName("try_browser")
    var tryBrowser: Browser
}

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface Browser {
    companion object {

        @JsValue("chrome")
        val chrome: Browser

        @JsValue("firefox")
        val firefox: Browser

        @JsValue("safari")
        val safari: Browser

        @JsValue("opera")
        val opera: Browser
    }
}

fun browser(string: String) = string.unsafeCast<Browser>()