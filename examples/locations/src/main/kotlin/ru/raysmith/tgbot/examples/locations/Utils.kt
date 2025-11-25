package ru.raysmith.tgbot.examples.locations

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.examples.locations.database.entity.Draft
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper
import ru.raysmith.tgbot.utils.message.MessageAction

/**
 * The wrapper that register a new [location] as enum with the given [newLocation] setup.
 * */
suspend fun <LFC : LocationFlowContext> LocationsWrapper<LFC>.location(
    location: Location,
    newLocation: suspend ru.raysmith.tgbot.utils.locations.Location<LFC>.() -> Unit
) {
    location(location.name, newLocation)
}

/**
 * The wrapper that move the user to the [location] and sets [nextLocationMessageAction] to the location flow context
 * if its provided.
 * */
suspend fun <T : EventHandler> LocationHandler<LocationFlowContextImpl, T>.toLocation(location: Location, nextLocationMessageAction: MessageAction? = null) {
    if (locationFlowContext.nextLocationMessageAction == null) {
        locationFlowContext.nextLocationMessageAction = nextLocationMessageAction
    }
    toLocation(location.name)
}

/**
 * Executes the provided block of code within the context of the current user's draft.
 *
 * @param block Lambda function that receives the current draft (`Draft`) and executes within a transaction context.
 *              If no current draft exists, an error handler is invoked.
 *
 * Context:
 * - `loc` - `LocationFlowContextImpl` object containing information about the current user state.
 *
 * ###### Usage example
 * ```kotlin
 * withCurrentDraft { draft ->
 *     // Work with the draft
 * }
 * ```
 *
 * ###### Error handling
 *
 * If no current draft is available, sends a message to the user asking them to create a new draft.
 * */
context(loc: LocationFlowContextImpl)
suspend fun EventHandler.withCurrentDraft(block: suspend context(Transaction) (draft: Draft) -> Unit) {
    newSuspendedTransaction {
        if (loc.user.currentDraft != null) {
            block(loc.user.currentDraft!!)
        } else {
            handleEntityNotAvailable("The current draft is not available, please, create a new one with /form.")
        }
    }
}

/**
 * Handles the case when an entity (like a draft) is not available.
 * Sends an alert or a message to the user depending on the type of event handler.
 *
 * @param message The message to be sent to the user.
 * @param reply If true, the message will be sent as a reply to the current message.
 */
private suspend fun EventHandler.handleEntityNotAvailable(message: String, reply: Boolean = false) {
    if (this is CallbackQueryHandler) {
        alert(message)
    } else {
        send {
            if (reply && messageId != null) {
                replyParameters = ReplyParameters(messageId!!)
            }
            text = message
        }
    }
}

/**
 * Checks if the callback query data matches the "back" action.
 *
 * This function verifies if the callback query data is equal to [CallbackQuery.BACK][CallbackQuery.Companion.BACK].
 * If the condition is met, it invokes the provided [handler].
 *
 * @param handler A suspendable function that processes the callback query data.
 *  It receives `null` if the data matches [CallbackQuery.BACK][CallbackQuery.Companion.BACK].
 */
suspend inline fun CallbackQueryHandler.isBack(handler: () -> Unit) {
    isDataEqual(CallbackQuery.BACK) {
        handler()
    }
}
