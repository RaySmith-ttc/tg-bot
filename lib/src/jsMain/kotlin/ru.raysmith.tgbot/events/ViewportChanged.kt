package ru.raysmith.tgbot.events

/**
 * @property isStateStable If true, the resizing of the Mini App is finished. If it is false, the resizing is ongoing
 *                         (the user is expanding
 * */
external interface ViewportChanged {

    /**
     * If true, the resizing of the Mini App is finished. If it is false, the resizing is ongoing (the user is expanding
     * or collapsing the Mini App or an animated object is playing).
     * */
    val isStateStable: Boolean
}