package ru.raysmith.tgbot.events

/**
 * @property data Text from the clipboard. If the clipboard contains non-text data, the field will be an empty string.
 * */
external interface ClipboardTextReceived {

    /**
     * Text from the clipboard. If the clipboard contains non-text data, the field will be an empty string.
     * If the Mini App has no access to the clipboard, the field `data` will be `null`.
     * */
    val data: String?
}