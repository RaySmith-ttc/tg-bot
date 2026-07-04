package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.chat.member.*

internal object ChatMemberSerializer : BaseJsonContentPolymorphicSerializer<ChatMember>(ChatMember::class, "status") {
    override fun selectDeserializer(type: String) = when (type) {
        "creator" -> ChatMemberOwner.serializer()
        "administrator" -> ChatMemberAdministrator.serializer()
        "member" -> ChatMemberMember.serializer()
        "restricted" -> ChatMemberRestricted.serializer()
        "left" -> ChatMemberLeft.serializer()
        "kicked" -> ChatMemberBanned.serializer()
        else -> null
    }
}
