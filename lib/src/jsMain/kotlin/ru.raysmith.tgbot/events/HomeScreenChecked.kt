package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.HomeScreenCheckedStatus.Companion.added
import ru.raysmith.tgbot.events.HomeScreenCheckedStatus.Companion.missed
import ru.raysmith.tgbot.events.HomeScreenCheckedStatus.Companion.unknown
import ru.raysmith.tgbot.events.HomeScreenCheckedStatus.Companion.unsupported
import seskar.js.JsValue

/**
 * @property status Current home screen status
 * */
external interface HomeScreenChecked {

    /** Current home screen status */
    val status: HomeScreenCheckedStatus
}

/**
 * The current home screen status.
 *
 * @property unsupported The feature is not supported, and it is not possible to add the icon to the home screen
 * @property unknown The feature is supported, and the icon can be added, but it is not possible to determine if the
 *                   icon has already been added
 * @property added The icon has already been added to the home screen
 * @property missed The icon has not been added to the home screen
 * */
external interface HomeScreenCheckedStatus {
    companion object {

        /** The feature is not supported, and it is not possible to add the icon to the home screen */
        @JsValue("unsupported")
        val unsupported: HomeScreenCheckedStatus

        /**
         * The feature is supported, and the icon can be added, but it is not possible to determine if the icon has
         * already been added
         * */
        @JsValue("unknown")
        val unknown: HomeScreenCheckedStatus

        /** The icon has already been added to the home screen */
        @JsValue("added")
        val added: HomeScreenCheckedStatus

        /** The icon has not been added to the home screen */
        @JsValue("missed")
        val missed: HomeScreenCheckedStatus
    }
}