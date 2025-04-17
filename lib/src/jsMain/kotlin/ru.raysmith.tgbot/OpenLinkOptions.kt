package ru.raysmith.tgbot

import seskar.js.JsValue

/**
 * @property tryInstantView If `true` the link will be opened in [Instant View](https://instantview.telegram.org/)
 *  mode if possible
 * @property tryBrowser The browser to use. If not specified, the default browser will be used.
 *  (use [browser] for custom string value)
 * @see
 * */
external interface OpenLinkOptions {

    @JsName("try_instant_view")
    var tryInstantView: Boolean

    @NotOfficialDocumentedOptIn
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

/**
 * @see OpenLinkOptions.tryBrowser
 * */
fun browser(string: String) = string.unsafeCast<Browser>()