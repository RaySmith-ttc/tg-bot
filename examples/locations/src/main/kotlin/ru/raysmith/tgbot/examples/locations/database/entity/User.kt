package ru.raysmith.tgbot.examples.locations.database.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScopeChat
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.examples.locations.FORM
import ru.raysmith.tgbot.examples.locations.Location
import ru.raysmith.tgbot.utils.toChatId

object Users : LongIdTable("users") {
    val location = enumerationByName("location", 32, Location::class)
    val currentDraftId = optReference("current_draft_id", Drafts)
}

class User(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<User>(Users) {
        fun findByIdOrAdd(id: Long) = findById(id) ?: new(id) {
            location = Location.MENU
        }
    }

    var location by Users.location
    var currentDraft by Draft optionalReferencedOn Users.currentDraftId

    context(api: API)
    suspend fun setupCommandsList() {
        api.setMyCommands(listOf(
            ru.raysmith.tgbot.model.network.command.BotCommand(BotCommand.START, "Info message"),
            ru.raysmith.tgbot.model.network.command.BotCommand(BotCommand.FORM, "Start filling out a form")
        ), BotCommandScopeChat(chatId = id.value.toChatId()))
    }
}