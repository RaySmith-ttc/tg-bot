package ru.raysmith.tgbot.core

import io.ktor.client.*
import io.ktor.client.request.*
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.keyboard.buildInlineKeyboard
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.model.network.chat.*
import ru.raysmith.tgbot.model.network.chat.forum.ForumTopic
import ru.raysmith.tgbot.model.network.chat.forum.IconColor
import ru.raysmith.tgbot.model.network.chat.member.ChatMember
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.network.menubutton.MenuButton
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageId
import ru.raysmith.tgbot.model.network.message.ParseMode
import java.time.ZonedDateTime

@DslMarker
annotation class BotContextDsl

// TODO оставить только альтернативные варианты функций с билдерами или с заполненными chatId
/** Allows to change a bot for the [handler][T] */
interface BotContext<T : EventHandler> : ISender, IEditor {

    /** Uses the [bot] token to make requests to telegram from [block]. */
    @BotContextDsl
    suspend fun <R> withBot(bot: Bot, block: suspend BotContext<T>.() -> R): R

    /**
     * Use this method to send text messages. On success, the sent [Message] is returned.
     *
     * @param text Text of the message to be sent, 1-4096 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text.
     * @param entities List of special entities that appear in message text, which can be specified instead of *[parseMode]*
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param keyboardMarkup Additional interface options. Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun sendMessage(
        text: String,
        parseMode: ParseMode? = null,
        entities: String? = null,
        disableWebPagePreview: Boolean? = bot.botConfig.disableWebPagePreviews,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
        messageThreadId: Int? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return sendMessage(
            chatId, messageThreadId, text, parseMode, entities, disableWebPagePreview, disableNotification, protectContent,
            replyToMessageId, allowSendingWithoutReply, keyboardMarkup
        )
    }

    /**
     * Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent [Message] is returned.
     * @param fromChatId Unique identifier for the chat where the original message was sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param chatId Unique identifier for the target chat or username of the target channel
     *
     *  */
    suspend fun forwardMessage(
        fromChatId: ChatId,
        messageThreadId: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageId: Int,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return forwardMessage(chatId, messageThreadId, fromChatId, disableNotification, protectContent, messageId)

    }

    /**
     * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
     * The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the
     * original message. Returns the [MessageId] of the sent message on success.
     *
     * @param fromChatId Unique identifier for the chat where the original message was sent
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
     * @param parseMode [ParseMode] for parsing entities in the message caption.
     * @param captionEntities List of special entities that appear in message text, which can be specified instead of *[parseMode]*
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param keyboardMarkup Additional interface options. Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */

    suspend fun copyMessage(
        fromChatId: ChatId,
        messageId: Int,
        messageThreadId: Int? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ) = copyMessage(
        chatId, messageThreadId, fromChatId, messageId, caption, parseMode, captionEntities, disableNotification, protectContent,
        replyToMessageId, allowSendingWithoutReply, keyboardMarkup
    )

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels,
     * the user will not be able to return to the chat on their own using invite links, etc., unless [unbanned][unbanChatMember] first.
     * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
     * Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are
     * considered to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * */
    suspend fun banChatMember(
        userId: ChatId.ID,
        untilDate: Until? = null,
        revokeMessages: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return banChatMember(chatId, userId, untilDate, revokeMessages)
    }


    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will **not** return to the
     * group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for
     * this to work. By default, this method guarantees that after the call the user is not a member of the chat,
     * but will be able to join it. So if the user is a member of the chat they will also be **removed** from the chat.
     * If you don't want this, use the parameter _only_if_banned_. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     * */
    suspend fun unbanChatMember(userId: ChatId.ID, onlyIfBanned: Boolean, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return unbanChatMember(chatId, userId, onlyIfBanned)
    }

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this
     * to work and must have the appropriate administrator rights.
     * Pass *True* for all permissions to lift restrictions from a user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param permissions New user permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently.
     * Otherwise, the [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews]
     * permissions will imply the [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios],
     * [ChatPermissions.canSendDocuments], [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos],
     * [ChatPermissions.canSendVideoNotes], and [ChatPermissions.canSendVoiceNotes] permissions;
     * the [ChatPermissions.canSendPolls] permission will imply the [ChatPermissions.canSendMessages] permission.
     * @param untilDate Date when restrictions will be lifted for the user. If user is restricted for more than
     * 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun restrictChatMember(
        userId: ChatId.ID,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
        untilDate: Until? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return restrictChatMember(chatId, userId, permissions, useIndependentChatPermissions, untilDate)
    }

    /**
     * Use this method to promote or demote a user in a supergroup or a channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Pass _False_ for all boolean parameters to demote a user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param administratorRights New administrator rights
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun promoteChatMember(
        userId: ChatId.ID,
        administratorRights: ChatAdministratorRights,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return promoteChatMember(
            chatId, userId,
            isAnonymous = administratorRights.isAnonymous,
            canManageChat = administratorRights.canManageChat,
            canPostMessages = administratorRights.canPostMessages,
            canEditMessages = administratorRights.canEditMessages,
            canDeleteMessages = administratorRights.canDeleteMessages,
            canPostStories = administratorRights.canPostStories,
            canEditStories = administratorRights.canEditStories,
            canDeleteStories = administratorRights.canDeleteStories,
            canManageVideoChats = administratorRights.canManageVideoChats,
            canRestrictMembers = administratorRights.canRestrictMembers,
            canPromoteMembers = administratorRights.canPromoteMembers,
            canChangeInfo = administratorRights.canChangeInfo,
            canInviteUsers = administratorRights.canInviteUsers,
            canPinMessages = administratorRights.canPinMessages,
            canManageTopics = administratorRights.canManageTopics
        )
    }

    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the bot.
     * Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param customTitle Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun setChatAdministratorCustomTitle(
        userId: ChatId.ID,
        customTitle: String,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return setChatAdministratorCustomTitle(chatId, userId, customTitle)
    }

    /**
     * Use this method to ban a channel chat in a supergroup or a channel.
     * Until the chat is unbanned[unbanChatSenderChat], the owner of the banned chat won't be able to send messages on
     * behalf of **any of their channels**. The bot must be an administrator in the supergroup or channel for this to
     * work and must have the appropriate administrator rights. Returns *True* on success.
     *
     * @param senderChatId    Unique identifier of the target sender chat
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun banChatSenderChat(
        senderChatId: ChatId.ID,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return banChatSenderChat(chatId, senderChatId)
    }

    /**
     * Use this method to unban a previously banned channel chat in a supergroup or channel.
     * The bot must be an administrator for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param senderChatId Unique identifier of the target sender chat
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun unbanChatSenderChat(
        senderChatId: ChatId.ID,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return unbanChatSenderChat(chatId, senderChatId)
    }

    /**
     * Use this method to set default chat permissions for all members.
     * The bot must be an administrator in the group or a supergroup for this to work and must have the
     * _can_restrict_members_ administrator rights. Returns *True* on success.
     *
     * @param permissions New default chat permissions
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently.
     * Otherwise, the [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews]
     * permissions will imply the [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios],
     * [ChatPermissions.canSendDocuments], [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos],
     * [ChatPermissions.canSendVideoNotes], and [ChatPermissions.canSendVoiceNotes] permissions;
     * the [ChatPermissions.canSendPolls] permission will imply the [ChatPermissions.canSendMessages] permission.
     * */
    suspend fun setChatPermissions(
        permissions: ChatPermissions,
        chatId: ChatId = getChatIdOrThrow(),
        useIndependentChatPermissions: Boolean? = null
    ): Boolean {
        return setChatPermissions(chatId, permissions, useIndependentChatPermissions)
    }

    /**
     * Use this method to generate a new primary invite link for a chat; any previously generated primary link
     * is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate
     * administrator rights. Returns the new invite link as String on success.
     *
     * > Note: Each administrator in a chat generates their own invite links. Bots can't use invite links generated
     * by other administrators. If you want your bot to work with invite links, it will need to generate its own link
     * using [exportChatInviteLink] or by calling the [getChat] method. If your bot needs to generate a new primary
     * invite link replacing its previous one, use [exportChatInviteLink] again.
     * */
    suspend fun exportChatInviteLink(): String {
        return exportChatInviteLink(getChatIdOrThrow())
    }

    /**
     * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat
     * for this to work and must have the appropriate administrator rights. The link can be revoked using the method
     * [revokeChatInviteLink]. Returns the new invite link as [ChatInviteLink] object.
     *
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining
     * the chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     * administrators. If *True*, _member_limit_ can't be specified
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun createChatInviteLink(
        name: String? = null,
        expireDate: ZonedDateTime? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): ChatInviteLink {
        return createChatInviteLink(chatId, name, expireDate, memberLimit, createsJoinRequest)
    }

    /**
     * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the
     * chat for this to work and must have the appropriate administrator rights.
     * Returns the edited invite link as a [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining
     * the chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     * administrators. If *True*, _member_limit_ can't be specified
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun editChatInviteLink(
        inviteLink: String,
        name: String? = null,
        expireDate: ZonedDateTime? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): ChatInviteLink {
        return editChatInviteLink(chatId, inviteLink, name, expireDate, memberLimit, createsJoinRequest)
    }

    /**
     * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is
     * automatically generated. The bot must be an administrator in the chat for this to work and must have the
     * appropriate administrator rights. Returns the revoked invite link as [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to revoke
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun revokeChatInviteLink(inviteLink: String, chatId: ChatId = getChatIdOrThrow()): ChatInviteLink {
        return revokeChatInviteLink(chatId, inviteLink)
    }

    /**
     * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work
     * and must have the _can_invite_users_ administrator right. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun approveChatJoinRequest(userId: ChatId.ID, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return approveChatJoinRequest(chatId, userId)
    }

    /**
     * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work
     * and must have the _can_invite_users_ administrator right. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun declineChatJoinRequest(userId: ChatId.ID, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return declineChatJoinRequest(chatId, userId)
    }

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param photo New chat photo
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatPhoto(photo: NotReusableInputFile, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return setChatPhoto(chatId, photo)
    }

    /**
     * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     * */
    suspend fun deleteChatPhoto(): Boolean {
        return deleteChatPhoto(getChatIdOrThrow())
    }

    /**
     * Use this method to change the title of a chat. Titles can't be changed for private chats.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param title New chat title, 1-255 characters
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatTitle(title: String, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return setChatTitle(chatId, title)
    }

    /**
     * Use this method to change the description of a group, a supergroup or a channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param description New chat description, 0-255 characters
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatDescription(description: String?, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return setChatDescription(chatId, description)
    }

    /**
     * Use this method to add a message to the list of pinned messages in a chat. If the chat is not a private chat,
     * the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator
     * right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns *True* on success.
     *
     * @param messageId Identifier of a message to pin
     * @param disableNotification Pass True, if it is not necessary to send a notification to all chat members
     * about the new pinned message. Notifications are always disabled in channels and private chats.
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun pinChatMessage(
        messageId: Int,
        disableNotification: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return pinChatMessage(chatId, messageId, disableNotification)
    }

    /**
     * Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a
     * private chat, the bot must be an administrator in the chat for this to work and must have the
     * 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel.
     * Returns *True* on success.
     *
     * @param messageId Identifier of a message to unpin.
     * If not specified, the most recent pinned message (by sending date) will be unpinned.
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun unpinChatMessage(messageId: Int? = null, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return unpinChatMessage(chatId, messageId)
    }

    /**
     * Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat,
     * the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages'
     * administrator right in a supergroup or 'can_edit_messages' administrator right in a channel.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun unpinAllChatMessages(): Boolean {
        return unpinAllChatMessages(getChatIdOrThrow())
    }

    /**
     * Use this method for your bot to leave a group, supergroup or channel. Returns *True* on success.
     * */
    suspend fun leaveChat(): Boolean {
        return leaveChat(getChatIdOrThrow())
    }

    // TODO docs
    /**
     * Use this method to get up to date information about the chat (current name of the user for one-on-one
     * conversations, current username of a user, group or channel, etc.).
     * Returns a [Chat] object on success.
     * */
    suspend fun getChat() = getChat(getChatIdOrThrow())

    /**
     * Use this method to get a list of administrators in a chat, which aren't bots.
     * Returns an Array of [ChatMember] objects.
     * */
    suspend fun getChatAdministrators(): List<ChatMember> {
        return getChatAdministrators(getChatIdOrThrow())
    }

    /**
     * Use this method to get the number of members in a chat. Returns _Int_ on success.
     * */
    suspend fun getChatMemberCount(): Int {
        return getChatMemberCount(getChatIdOrThrow())
    }

    /**
     * Use this method to get information about a member of a chat. Returns a [ChatMember] object on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun getChatMember(userId: ChatId.ID, chatId: ChatId = getChatIdOrThrow()): ChatMember {
        return getChatMember(chatId, userId)
    }

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator
     * in the chat for this to work and must have the appropriate administrator rights.
     * Use the field [canSetStickerSet][Chat.canSetStickerSet] optionally returned in [getChat] requests to check
     * if the bot can use this method. Returns *True* on success.
     *
     * @param stickerSetName Name of the sticker set to be set as the group sticker set
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatStickerSet(stickerSetName: String, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return setChatStickerSet(chatId, stickerSetName)
    }

    /**
     * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator
     * in the chat for this to work and must have the appropriate administrator rights.
     * Use the field [*canSetStickerSet*][Chat.canSetStickerSet] optionally returned in [getChat] requests to check
     * if the bot can use this method. Returns *True* on success.
     * */
    suspend fun deleteChatStickerSet(): Boolean {
        return deleteChatStickerSet(getChatIdOrThrow())
    }

    /**
     * Use this method to create a topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * Returns information about the created topic as a [ForumTopic] object.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param name Topic name, 1-128 characters
     * @param iconColor Color of the topic icon
     * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon.
     * Use [getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     * */
    suspend fun createForumTopic(
        name: String, iconColor: IconColor? = null, iconCustomEmojiId: String? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): ForumTopic {
        return createForumTopic(chatId, name, iconColor, iconCustomEmojiId)
    }

    /**
     * Use this method to edit name and icon of a topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param name New topic name, 0-128 characters. If not specified or empty,
     * the current name of the topic will be kept
     * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon.
     * Use [getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     * ass an empty string to remove the icon. If not specified, the current icon will be kept
     * */
    suspend fun editForumTopic(
        messageThreadId: Int, name: String? = null, iconCustomEmojiId: String? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return editForumTopic(chatId, messageThreadId, name, iconCustomEmojiId)
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights,
     * unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun closeForumTopic(messageThreadId: Int, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return closeForumTopic(chatId, messageThreadId)
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights,
     * unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun reopenForumTopic(messageThreadId: Int, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return reopenForumTopic(chatId, messageThreadId)
    }

    /**
     * Use this method to delete a forum topic along with all its messages in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canDeleteMessages*][ChatAdministratorRights.canDeleteMessages] administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun deleteForumTopic(messageThreadId: Int, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return deleteForumTopic(chatId, messageThreadId)
    }

    /**
     * Use this method to clear the list of pinned messages in a forum topic.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canPinMessages*][ChatAdministratorRights.canPinMessages] administrator right in the supergroup.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun unpinAllForumTopicMessages(messageThreadId: Int, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return unpinAllForumTopicMessages(chatId, messageThreadId)
    }

    /**
     * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param name New topic name, 1-128 characters
     * */
    suspend fun editGeneralForumTopic(name: String, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return editGeneralForumTopic(chatId, name)
    }

    /**
     * Use this method to close an open 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.

     * */
    suspend fun closeGeneralForumTopic(): Boolean {
        return closeGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to reopen a closed 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically unhidden if it was hidden. Returns *True* on success.

     * */
    suspend fun reopenGeneralForumTopic(): Boolean {
        return reopenGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to hide the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically closed if it was open. Returns *True* on success.
     * */
    suspend fun hideGeneralForumTopic(): Boolean {
        return hideGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to unhide the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically closed if it was open. Returns *True* on success.
     * */
    suspend fun unhideGeneralForumTopic(): Boolean {
        return unhideGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to clear the list of pinned messages in a General forum topic.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically closed if it was open. Returns *True* on success.
     * */
    suspend fun unpinAllGeneralForumTopicMessages(): Boolean {
        return unpinAllGeneralForumTopicMessages(getChatIdOrThrow())
    }

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     * Returns *True* on Success.
     *
     * @param chatId Unique identifier for the target private chat.
     * If not specified, default bot's menu button will be changed
     * @param menuButton [MenuButton] object for the bot's new menu button. Defaults to [MenuButtonDefault]
     * */
    suspend fun setChatMenuButton(menuButton: MenuButton? = null, chatId: ChatId? = null): Boolean {
        return setChatMenuButton(chatId, menuButton)
    }

    /**
     * Use this method to edit text and game messages. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise True is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param text New text of the message, 1-4096 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text
     * @param entities A JSON-serialized list of special entities that appear in message text,
     * which can be specified instead of parseMode
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageText(
        messageId: Int? = null,
        inlineMessageId: String? = null,
        text: String,
        parseMode: ParseMode? = null,
        entities: String? = null,
        disableWebPagePreview: Boolean? = bot.botConfig.disableWebPagePreviews,
        replyMarkup: KeyboardMarkup? = null,
        chatId: ChatId? = getChatIdOrThrow()
    ): Message {
        return editMessageText(
            chatId, messageId, inlineMessageId, text, parseMode, entities, disableWebPagePreview, replyMarkup
        )
    }

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise True is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param caption New caption of the message, 0-1024 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of parseMode
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageCaption(
        messageId: Int? = null,
        inlineMessageId: String? = null,
        caption: String,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        disableWebPagePreview: Boolean? = bot.botConfig.disableWebPagePreviews,
        replyMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return editMessageCaption(
            chatId, messageId, inlineMessageId, caption, parseMode, captionEntities, replyMarkup
        )
    }

    /**
     * Use this method to edit animation, audio, document, photo, or video messages.
     * If a message is part of a message album, then it can be edited only to an audio for audio albums,
     * only to a document for document albums and to a photo or a video otherwise.
     * When an inline message is edited, a new file can't be uploaded; use a previously uploaded file
     * via its file_id or specify a URL. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise *True* is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param media A JSON-serialized object for a new media content of the message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageMedia(
        messageId: Int? = null,
        inlineMessageId: String? = null,
        media: InputMedia,
        replyMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return editMessageMedia(
            chatId, messageId, inlineMessageId, media, replyMarkup
        )
    }

    /**
     * Use this method to edit only the reply markup of messages. On success,
     * if the edited message is not an inline message, the edited [Message] is returned, otherwise *True* is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageReplyMarkup(
        messageId: Int? = null,
        inlineMessageId: String? = null,
        replyMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return editMessageReplyMarkup(
            chatId, messageId, inlineMessageId, replyMarkup
        )
    }

    /**
     * Use this method to stop a poll which was sent by the bot. On success, the stopped [Poll] is returned
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageId Identifier of the original message with the poll
     * @param keyboard [MessageInlineKeyboard] builder for a new message inline keyboard.
     * */
    suspend fun stopPoll(
        messageId: Int,
        chatId: ChatId = getChatIdOrThrow(),
        keyboard: suspend MessageInlineKeyboard.() -> Unit
    ): Poll {
        val replyMarkup = buildInlineKeyboard(keyboard).toMarkup()
        return stopPoll(chatId, messageId, replyMarkup)
    }

    /**
     * Use this method to delete a message, including service messages, with the following limitations:
     * - A message can only be deleted if it was sent less than 48 hours ago.
     * - Service messages about a supergroup, channel, or forum topic creation can't be deleted.
     * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
     * - Bots can delete outgoing messages in private chats, groups, and supergroups.
     * - Bots can delete incoming messages in private chats.
     * - Bots granted can_post_messages permissions can delete outgoing messages in channels.
     * - If the bot is an administrator of a group, it can delete any message there.
     * - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
     *
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageId Identifier of the message to delete
     * */
    suspend fun deleteMessage(messageId: Int, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return deleteMessage(chatId, messageId)
    }

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes.
     * This will not be displayed to the user, use for your internal processes.
     * @param providerToken Payments provider token, obtained via [@Botfather](tg://BotFather)
     * @param currency Three-letter ISO 4217 currency code, see
     * [more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount,
     * delivery cost, delivery tax, bonus, etc.)
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency
     * (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`.
     * See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * Defaults to 0
     * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the *smallest units*
     * of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified.
     * The suggested tip amounts must be positive, passed in a strictly increased order and must
     * not exceed *max_tip_amount*.
     * @param startParameter Unique deep-linking parameter. If left empty, *forwarded copies* of the sent message will
     * have a *Pay* button, allowing multiple users to pay directly from the forwarded message, using the same invoice.
     * If non-empty, forwarded copies of the sent message will have a URL button with a deep link to the bot
     * (instead of a *Pay* button), with the value used as the start parameter
     * @param providerData A JSON-serialized data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing
     * image for a service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True*, if you require the user's full name to complete the order
     * @param needPhoneNumber Pass *True*, if you require the user's phone number to complete the order
     * @param needEmail Pass *True*, if you require the user's email address to complete the order
     * @param needShippingAddress Pass *True*, if you require the user's shipping address to complete the order
     * @param sendPhoneNumberToProvider Pass *True*, if user's phone number should be sent to provider
     * @param sendEmailToProvider Pass *True*, if user's email address should be sent to provider
     * @param isFlexible Pass *True*, if the final price depends on the shipping method
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to
     * message is not found
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     * If empty, one 'Pay `total price`' button will be shown. If not empty, the first button must be a Pay button.
     * */
    @Suppress("KDocUnresolvedReference")
    suspend fun sendInvoice(chatId: ChatId = getChatIdOrThrow(), buildAction: InvoiceSender.() -> Unit): Message {
        return InvoiceSender(bot).apply(buildAction).send(chatId)
    }

    /**
     * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
     *
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes.
     * This will not be displayed to the user, use for your internal processes.
     * @param providerToken Payments provider token, obtained via [@Botfather](tg://BotFather)
     * @param currency Three-letter ISO 4217 currency code, see
     * [more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount,
     * delivery cost, delivery tax, bonus, etc.)
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency
     * (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`.
     * See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * Defaults to 0
     * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the *smallest units*
     * of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified.
     * The suggested tip amounts must be positive, passed in a strictly increased order and must
     * not exceed *max_tip_amount*.
     * @param providerData A JSON-serialized data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing
     * image for a service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True*, if you require the user's full name to complete the order
     * @param needPhoneNumber Pass *True*, if you require the user's phone number to complete the order
     * @param needEmail Pass *True*, if you require the user's email address to complete the order
     * @param needShippingAddress Pass *True*, if you require the user's shipping address to complete the order
     * @param sendPhoneNumberToProvider Pass *True*, if user's phone number should be sent to provider
     * @param sendEmailToProvider Pass *True*, if user's email address should be sent to provider
     * @param isFlexible Pass *True*, if the final price depends on the shipping method
     * */
    @Suppress("KDocUnresolvedReference")
    suspend fun createInvoiceLink(buildAction: InvoiceCreateLinkSender.() -> Unit): String {
        return InvoiceCreateLinkSender(bot).apply(buildAction).send()
    }
}