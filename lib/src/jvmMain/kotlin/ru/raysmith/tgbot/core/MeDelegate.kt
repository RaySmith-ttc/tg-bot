package ru.raysmith.tgbot.core

import kotlinx.coroutines.runBlocking
import ru.raysmith.tgbot.model.network.User
import kotlin.reflect.KProperty

internal class MeDelegate(val needRefreshMe: Boolean, val updateNeedRefreshMe: (Boolean) -> Unit) {
    var meCache: User? = null

    operator fun getValue(bot: Bot, property: KProperty<*>): User {
        if (needRefreshMe || meCache == null) {
            meCache = runBlocking { bot.getMe() }
            updateNeedRefreshMe(false)
        }
        
        return meCache!!
    }
    
    operator fun setValue(bot: Bot, property: KProperty<*>, me: User) {
        meCache = me
    }
}