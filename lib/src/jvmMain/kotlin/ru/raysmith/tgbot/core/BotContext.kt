@file:Suppress("unused")

package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.model.api.chat.ChatAdministratorRights
import ru.raysmith.tgbot.model.api.chat.ChatFullInfo
import ru.raysmith.tgbot.model.api.chat.ChatInviteLink
import ru.raysmith.tgbot.model.api.chat.ChatPermissions
import ru.raysmith.tgbot.model.api.chat.forum.ForumTopic
import ru.raysmith.tgbot.model.api.chat.forum.IconColor
import ru.raysmith.tgbot.model.api.chat.member.ChatMember
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.InputMedia
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.api.menubutton.MenuButton
import ru.raysmith.tgbot.model.api.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.api.message.*
import ru.raysmith.tgbot.model.api.message.reaction.ReactionType
import ru.raysmith.tgbot.model.api.message.rich.InputRichMessage
import ru.raysmith.tgbot.model.api.poll.Poll
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.Until
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.keyboard.buildInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.keyboard.buildMarkupOrNull
import ru.raysmith.tgbot.network.API
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

// TODO improve interface documentation

/** Allows to change a bot for the [handler][T] */
interface BotContext<T : EventHandler> : ISender, IEditor, BotHolder, API {

    /** Uses the [bot] token to make requests to telegram from [block]. */
    suspend fun <R> withBot(bot: Bot, block: suspend BotContext<T>.() -> R): R

    /**
     * Use this method to send text messages. On success, the sent [Message] is returned.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required
     *  if the message is sent to a direct messages chat
     * @param text [MessageText] builder of the message text to be sent, 1-4096 characters after entities parsing
     * @param linkPreviewOptions Link preview generation options for the message
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send; for direct
     *  messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is
     *  automatically declined.
     * @param replyParameters Description of the message to reply to
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * @param replyMarkup [KeyboardCreator] builder of keyboard markup
     * */
    suspend fun sendMessage(
        text: MessageText.() -> Unit,
        directMessagesTopicId: Int? = null,
        businessConnectionId: String? = null,
        linkPreviewOptions: LinkPreviewOptions? = botConfig.linkPreviewOptions,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        messageThreadId: Int? = null,
        chatId: ChatId = getChatIdOrThrow(),
        replyMarkup: (KeyboardCreator.() -> Unit)? = null,
    ): Message {
        val text = MessageText(MessageTextType.TEXT, botConfig).apply(text)

        return sendMessage(
            businessConnectionId,
            chatId,
            messageThreadId,
            directMessagesTopicId,
            text.getTextString(),
            null,
            text.getEntities(),
            linkPreviewOptions,
            disableNotification,
            protectContent,
            allowPaidBroadcast,
            messageEffectId,
            suggestedPostParameters,
            replyParameters,
            buildMarkupOrNull(replyMarkup),
        )
    }

    /**
     * Use this method to forward messages of any kind. Service messages and messages with protected content can't be
     * forwarded. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel.
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be forwarded;
     *  required if the message is forwarded to a direct messages chat
     * @param videoStartTimestamp New start timestamp for the forwarded video in the message
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message; only available when
     *  forwarding to private chats
     * @param fromChatId Unique identifier for the chat where the original message was sent (or username of the target
     *  bot, supergroup or channel). Current chat from context by default.
     * @param suggestedPostParameters
     * */
    suspend fun forwardMessage(
        chatId: ChatId,
        messageId: Int,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        videoStartTimestamp: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        fromChatId: ChatId = getChatIdOrThrow(),
    ): Message {
        return forwardMessage(
            chatId,
            messageThreadId,
            directMessagesTopicId,
            fromChatId,
            videoStartTimestamp,
            disableNotification,
            protectContent,
            messageEffectId,
            suggestedPostParameters,
            messageId,
        )
    }

    /**
     * Use this method to forward multiple messages of any kind. If some of the specified messages can't be found or
     * forwarded, they are skipped. Service messages and messages with protected content can't be forwarded. Album
     * grouping is kept for forwarded messages. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be forwarded;
     *  required if the messages are forwarded to a direct messages chat
     * @param messageIds A list of 1-100 identifiers of messages in the chat [fromChatId] to forward. The identifiers
     *  must be specified in a strictly increasing order.
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param fromChatId Unique identifier for the chat where the original messages were sent (or username of the target
     *  bot, supergroup or channel in the format). Current chat from context by default.
     * */
    suspend fun forwardMessages(
        chatId: ChatId,
        messageIds: List<Int>,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        fromChatId: ChatId = getChatIdOrThrow(),
    ): List<MessageId> {
        return forwardMessages(
            chatId,
            messageThreadId,
            directMessagesTopicId,
            fromChatId,
            messageIds,
            disableNotification,
            protectContent,
        )
    }

    /**
     * Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway
     * winners messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz
     * [poll](https://core.telegram.org/bots/api#poll) can be copied only if the value of the field
     * [correctOptionId][Poll.correctOptionIds] is known to the bot. The method is analogous to the method
     * [forwardMessage], but the copied message doesn't have a link to the original message. Returns the [MessageId] of
     * the sent message on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum;
     *  for forum supergroups only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent;
     *  required if the messages are sent to a direct messages chat
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param caption [MessageText] builder of a new caption for media, 0-1024 characters after entities parsing. If not
     *  specified, the original caption is kept
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media. Ignored if a new
     *  caption isn't specified.
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param replyParameters Description of the message to reply to
     * @param fromChatId Unique identifier for the chat where the original message was sent. Current chat from context
     *  by default.
     * @param replyMarkup [KeyboardCreator] builder of keyboard markup
     * */
    suspend fun copyMessage(
        chatId: ChatId,
        messageId: Int,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        caption: (MessageText.() -> Unit)? = null,
        showCaptionAboveMedia: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        replyParameters: ReplyParameters? = null,
        fromChatId: ChatId = getChatIdOrThrow(),
        replyMarkup: (KeyboardCreator.() -> Unit)? = null,
    ): MessageId {
        val caption = caption?.let { MessageText(MessageTextType.TEXT, botConfig).apply(it) }

        return copyMessage(
            chatId,
            messageThreadId,
            directMessagesTopicId,
            fromChatId,
            messageId,
            caption?.getTextString(),
            null,
            caption?.getEntities(),
            showCaptionAboveMedia,
            disableNotification,
            protectContent,
            allowPaidBroadcast,
            replyParameters,
            buildMarkupOrNull(replyMarkup),
        )
    }

    /**
     * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they
     * are skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice
     * messages can't be copied. A quiz poll can be copied only if the value of the field
     * [correctOptionId][Poll.correctOptionIds] is known to the bot. The method is analogous to the method
     * [forwardMessages], but the copied messages don't have a link to the original message. Album grouping is kept for
     * copied messages. On success, an array of [MessageId] of the sent messages is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent; required
     *  if the messages are sent to a direct messages chat
     * @param messageIds A list of 1-100 identifiers of messages in the chat [fromChatId] to copy. The identifiers must
     *  be specified in a strictly increasing order.
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param removeCaption Pass *True* to copy the messages without their captions
     * @param fromChatId Unique identifier for the chat where the original messages were sent (or username of the target
     *  bot, supergroup or channel). Current chat from context by default.
     * */
    suspend fun copyMessages(
        chatId: ChatId,
        messageIds: List<Int>,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        removeCaption: Boolean? = null,
        fromChatId: ChatId = getChatIdOrThrow(),
    ): List<MessageId> {
        return copyMessages(
            chatId,
            messageThreadId,
            directMessagesTopicId,
            fromChatId,
            messageIds,
            disableNotification,
            protectContent,
            removeCaption,
        )
    }

    // TODO sendPhoto
    // TODO sendLivePhoto
    // TODO sendAudio
    // TODO sendDocument
    // TODO sendVideo
    // TODO sendAnimation
    // TODO sendVoice
    // TODO sendVideoNote
    // TODO sendPaidMedia
    // TODO sendMediaGroup
    // TODO sendLocation
    // TODO sendVenue
    // TODO sendContact
    // TODO sendPoll
    // TODO sendChecklist
    // TODO sendDice
    // TODO sendMessageDraft
    // TODO sendChatAction

    /**
     * Use this method to change the chosen reactions on a message. Service messages of some types can't be reacted to.
     * Automatically forwarded messages from a channel to its discussion group have the same available reactions as
     * messages in the channel. Bots can't use paid reactions. Returns *True* on success.
     *
     * @param messageId Identifier of the target message. If the message belongs to a media group, the reaction is set
     *  to the first non-deleted message in the group instead.
     * @param reaction A list of reaction types to set on the message. Currently, as non-premium users, bots can set up
     *  to one reaction per message. A custom emoji reaction can be used if it is either already present on the message
     *  or explicitly allowed by chat administrators. Paid reactions can't be used by bots.
     * @param isBig Pass *True* to set the reaction with a big animation
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * */
    suspend fun setMessageReaction(
        messageId: Int,
        reaction: List<ReactionType>,
        isBig: Boolean,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return setMessageReaction(
            chatId,
            messageId,
            reaction,
            isBig,
        )
    }

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels,the
     * user will not be able to return to the chat on their own using invite links, etc., unless
     * [unbanned][unbanChatMember] first. The bot must be an administrator in the chat for this to work and must have
     * the appropriate admin rights. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned. If user is banned for more than 366 days or less than 30
     *  seconds from the current time they are considered to be banned forever. Applied for supergroups and channels
     *  only.
     * @param revokeMessages Pass *True* to delete all messages from the chat for the user that is being removed. If
     *  *False*, the user will be able to see messages in the group that were sent before the user was removed. Always
     *  *True* for supergroups and channels.
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel. Current
     *  chat from context by default.
     * */
    suspend fun banChatMember(
        userId: ChatId.ID,
        untilDate: Until? = null,
        revokeMessages: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return banChatMember(
            chatId,
            userId,
            untilDate,
            revokeMessages,
        )
    }

    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will **not** return to the
     * group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this
     * to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be
     * able to join it. So if the user is a member of the chat they will also be **removed** from the chat. If you don't
     * want this, use the parameter [onlyIfBanned]. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel. Current
     *  chat from context by default.
     * */
    suspend fun unbanChatMember(
        userId: ChatId.ID,
        onlyIfBanned: Boolean,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return unbanChatMember(
            chatId,
            userId,
            onlyIfBanned,
        )
    }

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this
     * to work and must have the appropriate administrator rights. Pass *True* for all permissions to lift restrictions
     * from a user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param permissions New user permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently. Otherwise, the
     *  [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews] permissions will imply the
     *  [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios], [ChatPermissions.canSendDocuments],
     *  [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos], [ChatPermissions.canSendVideoNotes], and
     *  [ChatPermissions.canSendVoiceNotes] permissions; the [ChatPermissions.canSendPolls] permission will imply the
     *  [ChatPermissions.canSendMessages] permission.
     * @param untilDate Date when restrictions will be lifted for the user. If user is restricted for more than 366 days
     *  or less than 30 seconds from the current time, they are considered to be restricted forever.
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun restrictChatMember(
        userId: ChatId.ID,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
        untilDate: Until? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return restrictChatMember(
            chatId,
            userId,
            permissions,
            useIndependentChatPermissions,
            untilDate,
        )
    }

    /**
     * Use this method to promote or demote a user in a supergroup or a channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Pass _False_ for all boolean parameters to demote a user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param administratorRights New administrator rights
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * */
    suspend fun promoteChatMember(
        userId: ChatId.ID,
        administratorRights: ChatAdministratorRights,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return promoteChatMember(
            chatId,
            userId,
            administratorRights.isAnonymous,
            administratorRights.canManageChat,
            administratorRights.canPostMessages,
            administratorRights.canEditMessages,
            administratorRights.canDeleteMessages,
            administratorRights.canPostStories,
            administratorRights.canEditStories,
            administratorRights.canDeleteStories,
            administratorRights.canManageVideoChats,
            administratorRights.canRestrictMembers,
            administratorRights.canPromoteMembers,
            administratorRights.canChangeInfo,
            administratorRights.canInviteUsers,
            administratorRights.canPinMessages,
            administratorRights.canManageTopics,
            administratorRights.canManageDirectMessages,
            administratorRights.canManageTags,
        )
    }

    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns *True* on
     *  success.
     *
     * @param userId Unique identifier of the target user
     * @param customTitle Unique identifier of the target user; 0-16 characters, emoji are not allowed
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun setChatAdministratorCustomTitle(
        userId: ChatId.ID,
        customTitle: String,
        chatId: ChatId = getChatIdOrThrow()
    ): Boolean {
        return setChatAdministratorCustomTitle(chatId, userId, customTitle)
    }

    // TODO setChatMemberTag

    /**
     * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is
     * [unbanned][unbanChatSenderChat], the owner of the banned chat won't be able to send messages on behalf of **any
     * of their channels**. The bot must be an administrator in the supergroup or channel for this to work and must have
     * the appropriate administrator rights. Returns *True* on success.
     *
     * @param senderChatId Unique identifier of the target sender chat
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun banChatSenderChat(
        senderChatId: ChatId.ID,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return banChatSenderChat(
            chatId,
            senderChatId,
        )
    }

    /**
     * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an
     * administrator for this to work and must have the appropriate administrator rights. Returns *True* on success.
     *
     * @param senderChatId Unique identifier of the target sender chat
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun unbanChatSenderChat(
        senderChatId: ChatId.ID,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return unbanChatSenderChat(
            chatId,
            senderChatId,
        )
    }

    /**
     * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or
     * a supergroup for this to work and must have the [canRestrictMembers][ChatAdministratorRights.canRestrictMembers]
     * administrator rights. Returns *True* on success.
     *
     * @param permissions New default chat permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently. Otherwise, the
     *  [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews] permissions will imply the
     *  [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios], [ChatPermissions.canSendDocuments],
     *  [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos], [ChatPermissions.canSendVideoNotes], and
     *  [ChatPermissions.canSendVoiceNotes] permissions; the [ChatPermissions.canSendPolls] permission will imply the
     *  [ChatPermissions.canSendMessages] permission.
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun setChatPermissions(
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return setChatPermissions(
            chatId,
            permissions,
            useIndependentChatPermissions,
        )
    }

    /**
     * Use this method to generate a new primary invite link for a chat (in context); any previously generated primary
     * link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate
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
     * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for
     * this to work and must have the appropriate administrator rights. The link can be revoked using the method
     * [revokeChatInviteLink]. Returns the new invite link as [ChatInviteLink] object.
     *
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the
     *  chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     *  administrators. If *True*, [memberLimit] can't be specified.
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun createChatInviteLink(
        name: String? = null,
        expireDate: ZonedDateTime? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): ChatInviteLink {
        return createChatInviteLink(
            chatId,
            name,
            expireDate,
            memberLimit,
            createsJoinRequest,
        )
    }

    /**
     * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the
     * chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a
     * [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the
     *  chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     *  administrators. If *True*, [memberLimit] can't be specified
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun editChatInviteLink(
        inviteLink: String,
        name: String? = null,
        expireDate: ZonedDateTime? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): ChatInviteLink {
        return editChatInviteLink(
            chatId,
            inviteLink,
            name,
            expireDate,
            memberLimit,
            createsJoinRequest,
        )
    }

    /**
     * Use this method to create a
     * [subscription invite link](https://telegram.org/blog/superchannels-star-reactions-subscriptions#star-subscriptions)
     * for a channel chat. The bot must have the [canInviteUsers][ChatAdministratorRights.canInviteUsers] administrator
     * rights. The link can be edited using the method [editChatSubscriptionInviteLink] or revoked using the method
     * [revokeChatInviteLink]. Returns the new invite link as a [ChatInviteLink] object.
     *
     * @param subscriptionPrice The amount of Telegram Stars a user must pay initially and after each subsequent
     *  subscription period to be a member of the chat; 1-10000
     * @param name Invite link name; 0-32 characters
     * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment.
     *  Currently, it must always be 2592000 (30 days).
     * @param chatId Unique identifier for the target channel chat or username of the target channel. Current chat from
     *  context by default.
     * */
    suspend fun createChatSubscriptionInviteLink(
        subscriptionPrice: Int,
        name: String? = null,
        subscriptionPeriod: Duration = 30.days,
        chatId: ChatId = getChatIdOrThrow(),
    ): ChatInviteLink {
        return createChatSubscriptionInviteLink(
            chatId,
            name,
            subscriptionPeriod.inWholeSeconds.toInt(),
            subscriptionPrice,
        )
    }

    /**
     * Use this method to edit a subscription invite link created by the bot. The bot must have the
     * [ChatAdministratorRights.canInviteUsers] administrator rights. Returns the edited invite link as a
     * [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun editChatSubscriptionInviteLink(
        inviteLink: String,
        name: String? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): ChatInviteLink {
        return editChatSubscriptionInviteLink(
            chatId,
            inviteLink,
            name,
        )
    }


    /**
     * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is
     * automatically generated. The bot must be an administrator in the chat for this to work and must have the
     * appropriate administrator rights. Returns the revoked invite link as [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to revoke
     * @param chatId Unique identifier of the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun revokeChatInviteLink(
        inviteLink: String,
        chatId: ChatId = getChatIdOrThrow(),
    ): ChatInviteLink {
        return revokeChatInviteLink(
            chatId,
            inviteLink,
        )
    }

    /**
     * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work
     * and must have the _can_invite_users_ administrator right. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * */
    suspend fun approveChatJoinRequest(userId: ChatId.ID, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return approveChatJoinRequest(chatId, userId)
    }

    /**
     * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and
     * must have the [canInviteUsers][ChatAdministratorRights.canInviteUsers] administrator right. Returns *True* on
     * success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun declineChatJoinRequest(
        userId: ChatId.ID,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return declineChatJoinRequest(
            chatId,
            userId,
        )
    }

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must
     * be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns
     * *True* on success.
     *
     * @param photo New chat photo
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun setChatPhoto(
        photo: NotReusableInputFile,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return setChatPhoto(
            chatId,
            photo,
        )
    }

    /**
     * Use this method to delete a chat (current from context) photo. Photos can't be changed for private chats. The bot
     * must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns
     * *True* on success.
     * */
    suspend fun deleteChatPhoto(): Boolean {
        return deleteChatPhoto(getChatIdOrThrow())
    }

    /**
     * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights. Returns *True* on
     * success.
     *
     * @param title New chat title, 1-255 characters
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun setChatTitle(
        title: String,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return setChatTitle(
            chatId,
            title,
        )
    }

    /**
     * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator
     * in the chat for this to work and must have the appropriate administrator rights. Returns *True* on success.
     *
     * @param description New chat description, 0-255 characters
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun setChatDescription(
        description: String?,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return setChatDescription(
            chatId,
            description,
        )
    }

    /**
     * Use this method to add a message to the list of pinned messages in a chat. In private chats and channel direct
     * messages chats, all non-service messages can be pinned. Conversely, the bot must be an administrator with the
     * [canPinMessages][ChatAdministratorRights.canPinMessages] right or the
     * [canEditMessages][ChatAdministratorRights.canEditMessages] right to pin messages in groups and channels
     * respectively. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  pinned
     * @param messageId Identifier of the message to pin
     * @param disableNotification Pass *True*, if it is not necessary to send a notification to all chat members about
     *  the new pinned message. Notifications are always disabled in channels and private chats.
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun pinChatMessage(
        messageId: Int,
        businessConnectionId: String? = null,
        disableNotification: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return pinChatMessage(
            businessConnectionId,
            chatId,
            messageId,
            disableNotification,
        )
    }

    /**
     * Use this method to remove a message from the list of pinned messages in a chat. In private chats and channel
     * direct messages chats, all messages can be unpinned. Conversely, the bot must be an administrator with the
     * [canPinMessages][ChatAdministratorRights.canPinMessages] right or the
     * [canEditMessages][ChatAdministratorRights.canEditMessages] right in groups and channels respectively. Returns
     * *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  unpinned
     * @param messageId Identifier of the message to unpin. Required if [businessConnectionId] is specified. If not
     *  specified, the most recent pinned message (by sending date) will be unpinned.
     * @param chatId Unique identifier for the target chat or username of the target channel. Current chat from context
     *  by default.
     * */
    suspend fun unpinChatMessage(
        businessConnectionId: String? = null,
        messageId: Int? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return unpinChatMessage(
            businessConnectionId,
            chatId,
            messageId,
        )
    }

    /**
     * Use this method to clear the list of pinned messages in a chat (current from context). In private chats and
     * channel direct messages chats, no additional rights are required to unpin all pinned messages. Conversely, the
     * bot must be an administrator with the [canPinMessages][ChatAdministratorRights.canPinMessages] right or the
     * [canEditMessages][ChatAdministratorRights.canEditMessages] right to unpin all pinned messages in groups and
     * channels respectively. Returns *True* on success.
     * */
    suspend fun unpinAllChatMessages(): Boolean {
        return unpinAllChatMessages(getChatIdOrThrow())
    }

    /**
     * Use this method for your bot to leave a group, supergroup or channel (current from context). Returns *True* on
     *  success.
     * */
    suspend fun leaveChat(): Boolean {
        return leaveChat(getChatIdOrThrow())
    }

    /**
     * Use this method to get up to date information about the chat (current from context). Returns a [ChatFullInfo]
     * object on success.
     * */
    suspend fun getChat() = getChat(getChatIdOrThrow())

    /**
     * Use this method to get a list of administrators in a chat.
     *
     * @param returnBots Pass *True* to additionally receive all bots that are administrators of the chat. By default,
     *  bots other than the current bot are omitted.
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel. Current chat
     *  from context by default.
     * */
    suspend fun getChatAdministrators(
        returnBots: Boolean? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): List<ChatMember> {
        return getChatAdministrators(
            chatId,
            returnBots,
        )
    }

    /** Use this method to get the number of members in a chat (current from context). */
    suspend fun getChatMemberCount(): Int {
        return getChatMemberCount(getChatIdOrThrow())
    }

    /**
     * Use this method to get information about a member of a chat.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel. Current chat
     *  from context by default.
     * */
    suspend fun getChatMember(
        userId: ChatId.ID,
        chatId: ChatId = getChatIdOrThrow(),
    ): ChatMember {
        return getChatMember(
            chatId,
            userId,
        )
    }

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for
     * this to work and must have the appropriate administrator rights. Use the field
     * [canSetStickerSet][ChatFullInfo.canSetStickerSet] optionally returned in [getChat] requests to check if the bot
     * can use this method. Returns *True* on success.
     *
     * @param stickerSetName Name of the sticker set to be set as the group sticker set
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun setChatStickerSet(
        stickerSetName: String,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return setChatStickerSet(
            chatId,
            stickerSetName,
        )
    }

    /**
     * Use this method to delete a group sticker set from a supergroup (current from context). The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights. Use the field
     * [canSetStickerSet][ChatFullInfo.canSetStickerSet] optionally returned in [getChat] requests to check if the bot
     * can use this method. Returns *True* on success.
     * */
    suspend fun deleteChatStickerSet(): Boolean {
        return deleteChatStickerSet(getChatIdOrThrow())
    }

    /**
     * Use this method to create a topic in a forum supergroup chat or a private chat with a user. In the case of a
     * supergroup chat the bot must be an administrator in the chat for this to work and must have
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator right. Returns information about the
     * created topic as a [ForumTopic] object.
     *
     * @param name Topic name, 1-128 characters
     * @param iconColor Color of the topic icon.
     * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon. Use
     *  [getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun createForumTopic(
        name: String,
        iconColor: IconColor? = null,
        iconCustomEmojiId: String? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): ForumTopic {
        return createForumTopic(
            chatId,
            name,
            iconColor,
            iconCustomEmojiId,
        )
    }

    /**
     * Use this method to edit name and icon of a topic in a forum supergroup chat or a private chat with a user. In the
     * case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights, unless it is the creator of the
     * topic. Returns *True* on success.
     *
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param name New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be
     *  kept.
     * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon. Use
     *  [getForumTopicIconStickers] to get all allowed custom emoji identifiers. Pass an empty string to remove the icon
     *  . If not specified, the current icon will be kept.
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun editForumTopic(
        messageThreadId: Int,
        name: String? = null,
        iconCustomEmojiId: String? = null,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return editForumTopic(
            chatId,
            messageThreadId,
            name,
            iconCustomEmojiId,
        )
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat
     * for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics] administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun closeForumTopic(
        messageThreadId: Int,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return closeForumTopic(
            chatId,
            messageThreadId,
        )
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat
     * for this to work and must have the [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun reopenForumTopic(
        messageThreadId: Int,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return reopenForumTopic(
            chatId,
            messageThreadId,
        )
    }

    /**
     * Use this method to delete a forum topic along with all its messages in a forum supergroup chat or a private chat
     * with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and
     * must have the [canDeleteMessages][ChatAdministratorRights.canDeleteMessages] administrator rights. Returns *True*
     * on success.
     *
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun deleteForumTopic(
        messageThreadId: Int,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return deleteForumTopic(
            chatId,
            messageThreadId,
        )
    }

    /**
     * Use this method to clear the list of pinned messages in a forum topic in a forum supergroup chat or a private
     * chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work
     * and must have the [canPinMessages][ChatAdministratorRights.canPinMessages] administrator right in the supergroup.
     * Returns *True* on success.
     *
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param chatId Unique identifier for the target chat or username of the target supergroup. Current chat from
     *  context by default.
     * */
    suspend fun unpinAllForumTopicMessages(
        messageThreadId: Int,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return unpinAllForumTopicMessages(
            chatId,
            messageThreadId,
        )
    }

    /**
     * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param name New topic name, 1-128 characters
     * */
    suspend fun editGeneralForumTopic(
        name: String,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return editGeneralForumTopic(
            chatId,
            name,
        )
    }

    /**
     * Use this method to close an open 'General' topic in a forum supergroup chat (current from context). The bot must
     * be an administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.
     * */
    suspend fun closeGeneralForumTopic(): Boolean {
        return closeGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to reopen a closed 'General' topic in a forum supergroup chat (current from context). The bot
     * must be an administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. The topic will be automatically
     * unhidden if it was hidden. Returns *True* on success.
     * */
    suspend fun reopenGeneralForumTopic(): Boolean {
        return reopenGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to hide the 'General' topic in a forum supergroup chat (current from context). The bot must be an
     * administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. The topic will be automatically
     * closed if it was open. Returns *True* on success.
     * */
    suspend fun hideGeneralForumTopic(): Boolean {
        return hideGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to unhide the 'General' topic in a forum supergroup chat (current from context). The bot must be
     * an administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. The topic will be automatically
     * closed if it was open. Returns *True* on success.
     * */
    suspend fun unhideGeneralForumTopic(): Boolean {
        return unhideGeneralForumTopic(getChatIdOrThrow())
    }

    /**
     * Use this method to clear the list of pinned messages in a General forum topic (in current chat from context). The
     * bot must be an administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. The topic will be automatically
     * closed if it was open. Returns *True* on success.
     * */
    suspend fun unpinAllGeneralForumTopicMessages(): Boolean {
        return unpinAllGeneralForumTopicMessages(getChatIdOrThrow())
    }

    // TODO getUserChatBoosts

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     * Returns *True* on Success.
     *
     * @param menuButton [MenuButton] object for the bot's new menu button. Defaults to [MenuButtonDefault].
     * @param chatId Unique identifier for the target private chat. If not specified the bot's default menu button will
     *  be changed. Current chat from context by default.
     * */
    suspend fun setChatMenuButton(
        menuButton: MenuButton? = null,
        chatId: ChatId? = getChatIdOrThrow(),
    ): Boolean {
        return setChatMenuButton(
            chatId,
            menuButton,
        )
    }

    // TODO getChatMenuButton

    /**
     * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver.
     * Returns True on success.
     *
     * @param userId Required if [chatId] is not specified. Unique identifier of the target user who will receive the
     *  gift.
     * @param chatId Required if [userId] is not specified. Unique identifier for the chat or username of the channel
     *  that will receive the gift.
     * @param giftId Identifier of the gift; limited gifts can't be sent to channel chats
     * @param payForUpgrade Pass *True* to pay for the gift upgrade from the bot's balance, thereby making the upgrade
     *  free for the receiver
     * @param text [MessageText] builder of text that will be shown along with the gift. Entities other than
     *  [bold][MessageEntityType.BOLD], [italic][MessageEntityType.ITALIC], [underline][MessageEntityType.UNDERLINE],
     *  [strikethrough][MessageEntityType.STRIKETHROUGH], [spoiler][MessageEntityType.SPOILER],
     *  [custom emoji][MessageEntityType.CUSTOM_EMOJI], and [date time][MessageEntityType.DATE_TIME] are ignored.
     *
     * @see [IMessage.MAX_GIFT_TEXT_LENGTH] // TODO link to limits object
     * */
    suspend fun sendGift(
        giftId: String,
        userId: ChatId.ID? = null,
        chatId: ChatId? = null, // not should use getChatIdOrThrow()
        payForUpgrade: Boolean? = null,
        text: MessageText.() -> Unit
    ): Boolean {
        val text = MessageText(MessageTextType.GIFT_TEXT, botConfig).apply(text)

        return sendGift(
            userId,
            chatId,
            giftId,
            payForUpgrade,
            text.getTextString(),
            null,
            text.getEntities(),
        )
    }

    // TODO verifyChat
    // TODO removeChatVerification
    // TODO getChatGifts
    // TODO postStory (builders)
    // TODO repostStory

    /**
     * Use this method to edit text, rich and game messages. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise *True* is returned. Note that business messages that were not sent by
     * the bot and do not contain an inline keyboard can only be edited within **48 hours** from the time they were sent
     * .
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be
     *  edited was sent

     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message.
     * @param text [MessageText] builder of a new text of the message, 1-4096 characters after entity parsing; required
     *  if [richMessage] isn't specified which can be specified instead of parseMode
     * @param linkPreviewOptions Link preview generation options for the message
     * @param richMessage New rich content of the message; required if [text] isn't specified
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel. Current chat from context by default.
     * @param keyboardMarkup [MessageInlineKeyboard] builder for an inline keyboard
     * */
    suspend fun editMessageText(
        text: (MessageText.() -> Unit)? = null,
        businessConnectionId: String? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        linkPreviewOptions: LinkPreviewOptions? = bot.botConfig.linkPreviewOptions,
        richMessage: InputRichMessage? = null,
        chatId: ChatId? = getChatIdOrThrow(),
        keyboardMarkup: (MessageInlineKeyboard.() -> Unit)? = null,
    ): Message {
        val text = text?.let { MessageText(MessageTextType.TEXT, botConfig).apply(text) }

        return editMessageText(
            businessConnectionId,
            chatId,
            messageId,
            inlineMessageId,
            text?.getTextString(),
            null,
            text?.getEntities(),
            linkPreviewOptions,
            richMessage,
            keyboardMarkup?.let { buildInlineKeyboard(it) }?.toMarkup(),
        )
    }

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the
     * edited [Message] is returned, otherwise *True* is returned. Note that business messages that were not sent by the
     * bot and do not contain an inline keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to
     *  be edited was sent
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message.
     * @param caption [MessageText] builder of a new caption of the message, 0-1024 characters after entities parsing
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media.
     *  Supported only for animation, photo and video messages.
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel. Current chat from context by default.
     * @param keyboardMarkup [MessageInlineKeyboard] builder for an inline keyboard
     * */
    suspend fun editMessageCaption(
        caption: (MessageText.() -> Unit)? = null,
        businessConnectionId: String? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        showCaptionAboveMedia: Boolean? = null,
        linkPreviewOptions: LinkPreviewOptions? = bot.botConfig.linkPreviewOptions,
        chatId: ChatId = getChatIdOrThrow(),
        keyboardMarkup: (MessageInlineKeyboard.() -> Unit)? = null,
    ): Message {
        val caption = caption?.let { MessageText(MessageTextType.CAPTION, botConfig).apply(caption) }

        return editMessageCaption(
            businessConnectionId,
            chatId,
            messageId,
            inlineMessageId,
            caption?.getTextString(),
            null,
            caption?.getEntities(),
            showCaptionAboveMedia,
            keyboardMarkup?.let { buildInlineKeyboard(it) }?.toMarkup(),
        )
    }

    /**
     * Use this method to edit animation, audio, document, live photo, photo, or video messages, or to replace to a text
     * or a rich message with a media. If a message is part of a message album, then it can be edited only to an audio
     * for audio albums, only to a document for document albums and to a photo, a live photo or a video otherwise. When
     * an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its
     * [InputFile.FileIdOrUrl]. On success, if the edited message is not an inline message, the edited [Message] is
     * returned, otherwise *True* is returned. Note that business messages that were not sent by the bot and do not
     * contain an inline keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param media New media content of the message
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be
     *  edited was sent
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if [chatId] and [messageId] are not specified. Identifier of the inline message.
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel. Current chat from context by default.
     * @param keyboardMarkup [MessageInlineKeyboard] builder for an inline keyboard
     * */
    suspend fun editMessageMedia(
        media: InputMedia,
        businessConnectionId: String? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        chatId: ChatId = getChatIdOrThrow(),
        keyboardMarkup: (MessageInlineKeyboard.() -> Unit)? = null,
    ): Message {
        return editMessageMedia(
            businessConnectionId,
            chatId,
            messageId,
            inlineMessageId,
            media,
            keyboardMarkup?.let { buildInlineKeyboard(it) }?.toMarkup(),
        )
    }

    // TODO editMessageLiveLocation
    // TODO stopMessageLiveLocation
    // TODO editMessageChecklist

    /**
     * Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline
     * message, the edited [Message] is returned, otherwise *True* is returned. Note that business messages that were
     * not sent by the bot and do not contain an inline keyboard can only be edited within **48 hours** from the time
     * they were sent.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to
     *  be edited was sent
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message.
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel. Current chat from context by default.
     * @param keyboardMarkup [MessageInlineKeyboard] builder for an inline keyboard
     * */
    suspend fun editMessageReplyMarkup(
        messageId: Int? = null,
        inlineMessageId: String? = null,
        businessConnectionId: String? = null,
        chatId: ChatId = getChatIdOrThrow(),
        keyboardMarkup: (MessageInlineKeyboard.() -> Unit)? = null,
    ): Message {
        return editMessageReplyMarkup(
            businessConnectionId,
            chatId,
            messageId,
            inlineMessageId,
            keyboardMarkup?.let { buildInlineKeyboard(it) }?.toMarkup(),
        )
    }

    /**
     * Use this method to stop a poll which was sent by the bot. On success, the stopped [Poll] is returned.
     *
     * @param messageId Identifier of the original message with the poll
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to
     *  be edited was sent
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * @param keyboardMarkup [MessageInlineKeyboard] builder for a new message inline keyboard.
     * */
    suspend fun stopPoll(
        messageId: Int,
        businessConnectionId: String? = null,
        chatId: ChatId = getChatIdOrThrow(),
        keyboardMarkup: (MessageInlineKeyboard.() -> Unit)? = null,
    ): Poll {
        return stopPoll(
            businessConnectionId,
            chatId,
            messageId,
            keyboardMarkup?.let { buildInlineKeyboard(it) }?.toMarkup(),
        )
    }

    // TODO approveSuggestedPost
    // TODO declineSuggestedPost

    /**
     * Use this method to delete a message, including service messages, with the following limitations:
     * - A message can only be deleted if it was sent less than 48 hours ago.
     * - Service messages about a supergroup, channel, or forum topic creation can't be deleted.
     * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
     * - Bots can delete outgoing messages in private chats, groups, and supergroups.
     * - Bots can delete incoming messages in private chats.
     * - Bots granted [ChatAdministratorRights.canPostMessages] permissions can delete outgoing messages in channels.
     * - If the bot is an administrator of a group, it can delete any message there.
     * - If the bot has [ChatAdministratorRights.canDeleteMessages] right in a supergroup or a channel, it can delete
     * any message there.
     * - If the bot has [ChatAdministratorRights.canManageDirectMessages] right in a channel, it can delete any message
     * in the corresponding direct messages chat.
     *
     * Returns *True* on success.
     *
     * @param messageId Identifier of the message to delete
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * */
    suspend fun deleteMessage(
        messageId: Int,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return deleteMessage(
            chatId,
            messageId,
        )
    }

    /**
     * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found,
     * they are skipped. Returns *True* on success.
     *
     * See [deleteMessage] for limitations on which messages can be deleted
     *
     * @param messageIds A list of 1-100 identifiers of messages to delete.
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * */
    suspend fun deleteMessages(
        messageIds: List<Int>,
        chatId: ChatId = getChatIdOrThrow(),
    ): Boolean {
        return deleteMessages(
            chatId,
            messageIds,
        )
    }

    // TODO deleteMessageReaction
    // TODO deleteAllMessageReactions
    // TODO sendSticker
    // TODO sendRichMessage
    // TODO sendRichMessageDraft

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel. Current
     *  chat from context by default.
     * @param invoice [SendInvoiceRequestBuilder] builder of invoice t osend
     * */
    suspend fun sendInvoice(
        chatId: ChatId = getChatIdOrThrow(),
        invoice: SendInvoiceRequestBuilder.() -> Unit,
    ): Message {
        return SendInvoiceRequestBuilder(bot).apply(invoice).send(chatId)
    }


    /** Use this method to create a link for an invoice. Returns the created invoice link as String on success. */
    suspend fun createInvoiceLink(
        buildAction: suspend CreateInvoiceLinkRequestBuilder.() -> Unit
    ): String {
        return CreateInvoiceLinkRequestBuilder(bot).apply { buildAction() }.send()
    }

    // TODO sendGame
}
