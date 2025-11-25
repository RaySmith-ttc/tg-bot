package ru.raysmith.tgbot.examples.locations

import org.jetbrains.exposed.sql.transactions.transaction
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.examples.locations.database.entity.User
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.message.MessageAction

class LocationFlowContextImpl(override val update: Update) : LocationFlowContext {
    val user by lazy {
        transaction {
            // The from field may be null only in POLL and MESSAGE_REACTION_COUNT update types
            User.findByIdOrAdd(update.findFrom()!!.id.value)
        }
    }

    var nextLocationMessageAction: MessageAction? = null
}