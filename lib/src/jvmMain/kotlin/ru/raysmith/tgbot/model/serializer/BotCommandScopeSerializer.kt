package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.command.*

internal object BotCommandScopeSerializer :
    BaseJsonContentPolymorphicSerializer<BotCommandScope>(BotCommandScope::class) {

    override fun selectDeserializer(type: String) = when (type) {
        "default" -> BotCommandScopeDefault.serializer()
        "all_private_chats" -> BotCommandScopeAllPrivateChats.serializer()
        "all_group_chats" -> BotCommandScopeAllGroupChats.serializer()
        "all_chat_administrators" -> BotCommandScopeAllChatAdministrators.serializer()
        "chat" -> BotCommandScopeChat.serializer()
        "chat_administrators" -> BotCommandScopeChatAdministrators.serializer()
        "chat_member" -> BotCommandScopeChatMember.serializer()
        else -> null
    }
}
