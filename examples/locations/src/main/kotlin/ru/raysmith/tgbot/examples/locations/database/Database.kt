package ru.raysmith.tgbot.examples.locations.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.raysmith.tgbot.examples.locations.database.entity.Drafts
import ru.raysmith.tgbot.examples.locations.database.entity.Users

object Database {
    fun init() {
        Database.connect(
            url = "jdbc:h2:file:./h2/database;MODE=MySQL",
            driver = "org.h2.Driver",
            user = "root",
            password = "",
            databaseConfig = DatabaseConfig {
                keepLoadedReferencesOutOfTransaction = true
            }
        )

        transaction {
            SchemaUtils.create(Users, Drafts)
        }
    }
}