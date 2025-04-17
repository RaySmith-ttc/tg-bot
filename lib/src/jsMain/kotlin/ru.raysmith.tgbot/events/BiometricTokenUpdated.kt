package ru.raysmith.tgbot.events

/**
 * @property isUpdated Whether the token was updated
 * */
external interface BiometricTokenUpdated {

    /** Whether the token was updated */
    val isUpdated: Boolean
}