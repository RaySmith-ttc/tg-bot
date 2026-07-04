package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.MessageEntityType
import ru.raysmith.utils.notNull

class MessageEntityBuilder(val type: MessageEntityType) {
    var offset: Int? = null
    var length: Int? = null
    var url: String? = null
    var user: User? = null
    var language: String? = null

    fun toEntity(): MessageEntity {
        require(offset notNull length) { "The offset and length are required to create the message entity" }
        return MessageEntity(
            type, offset!!, length!!, url, user, language
        )
    }
}
