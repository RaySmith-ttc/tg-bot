package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.User
import kotlin.reflect.KProperty

internal class MeDelegate(var needRefreshMe: Boolean) {
    var meCache: User? = null
    operator fun getValue(bot: Bot, property: KProperty<*>): User {
        if (needRefreshMe || meCache == null) {
            meCache = bot.getMe()
            needRefreshMe = false
        }
        
        return meCache!!
    }
    
    operator fun setValue(bot: Bot, property: KProperty<*>, me: User) {
        meCache = me
    }
}