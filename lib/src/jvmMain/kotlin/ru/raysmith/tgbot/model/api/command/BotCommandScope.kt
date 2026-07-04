package ru.raysmith.tgbot.model.api.command

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.BotCommandScopeSerializer

/**
 * This object represents the scope to which bot commands are applied. Currently, the following 7 scopes are supported:
 * - [BotCommandScopeDefault]
 * - [BotCommandScopeAllPrivateChats]
 * - [BotCommandScopeAllGroupChats]
 * - [BotCommandScopeAllChatAdministrators]
 * - [BotCommandScopeChat]
 * - [BotCommandScopeChatAdministrators]
 * - [BotCommandScopeChatMember]
 *
 * ## Determining list of commands
 * The following algorithm is used to determine the list of commands for a particular user viewing the bot menu.
 * The first list of commands which is set is returned:
 *
 * ### Commands in the chat with the bot
 * - botCommandScopeChat + language_code
 * - botCommandScopeChat
 * - botCommandScopeAllPrivateChats + language_code
 * - botCommandScopeAllPrivateChats
 * - botCommandScopeDefault + language_code
 * - botCommandScopeDefault
 *
 * ### Commands in group and supergroup chats
 * - botCommandScopeChatMember + language_code
 * - botCommandScopeChatMember
 * - botCommandScopeChatAdministrators + language_code (administrators only)
 * - botCommandScopeChatAdministrators (administrators only)
 * - botCommandScopeChat + language_code
 * - botCommandScopeChat
 * - botCommandScopeAllChatAdministrators + language_code (administrators only)
 * - botCommandScopeAllChatAdministrators (administrators only)
 * - botCommandScopeAllGroupChats + language_code
 * - botCommandScopeAllGroupChats
 * - botCommandScopeDefault + language_code
 * - botCommandScopeDefault
 * */
@Serializable(BotCommandScopeSerializer::class)
sealed interface BotCommandScope {

    /** Scope type */
    val type: String
}
