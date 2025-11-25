package ru.raysmith.tgbot.examples.locations.database.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object Drafts : LongIdTable("drafts") {
    val name = varchar("name", 4096, "utf8mb4_unicode_ci").nullable()
    val birthDate = date("birth_date").nullable()
    val address = varchar("address", 4096, "utf8mb4_unicode_ci").nullable()
}

class Draft(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Draft>(Drafts)

    var name by Drafts.name
    var birthDate by Drafts.birthDate
    var address by Drafts.address

    fun isCompleted() = name != null && birthDate != null && address != null
}