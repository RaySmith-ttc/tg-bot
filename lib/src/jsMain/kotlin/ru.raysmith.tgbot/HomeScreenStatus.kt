package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface HomeScreenStatus {
    companion object {

        /** The feature is not supported, and it is not possible to add the icon to the home screen */
        @JsValue("unsupported")
        val unsupported: HomeScreenStatus

        /**
         * The feature is supported, and the icon can be added, but it is not possible to determine if the icon has
         * already been added
         * */
        @JsValue("unknown")
        val unknown: HomeScreenStatus

        /** The icon has already been added to the home screen */
        @JsValue("added")
        val added: HomeScreenStatus

        /** The icon has not been added to the home screen */
        @JsValue("missed")
        val missed: HomeScreenStatus
    }
}