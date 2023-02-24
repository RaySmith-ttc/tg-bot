package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.handler.MessageHandler
import ru.raysmith.tgbot.model.network.media.Media
import ru.raysmith.tgbot.model.network.media.PhotoSize

// TODO MessageObject
class MessageMediaWrapper<T : Media, R>(val obj: T?) {
    private var state: R? = null
    private var isConverted = false
    private var badObj = false
    
    fun verify(block: (T) -> Boolean): MessageMediaWrapper<T, R> {
        if (obj == null || !block(obj)) {
            badObj = true
        }
        return this
    }
    fun convert(block: (T) -> R?): MessageMediaWrapper<T, R> {
        if (!badObj && obj != null) {
            state = block(obj)
            if (state == null)  {
                badObj = true
            }
        }
        isConverted = true
        return this
    }
    fun use(block: (R) -> Unit): R? {
        return when {
            badObj -> return null
            isConverted && state != null -> {
                block(state!!)
                state
            }
    
            !isConverted && obj != null -> {
                block(obj as R)
                obj
            }
    
            else -> {
                null
            }
        }
    }
}

fun <R> MessageHandler.messagePhoto(): MessageMediaWrapper<PhotoSize, R> {
    return MessageMediaWrapper(message.photo?.lastOrNull())
}