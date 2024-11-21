package ru.raysmith.tgbot.events

external interface ViewportChangedEvent : Event {

    /** If true, the resizing of the Mini App is finished. */
    val isStateStable: Boolean
}