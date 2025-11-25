package ru.raysmith.tgbot.examples.locations

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.edit
import ru.raysmith.tgbot.core.send
import ru.raysmith.tgbot.examples.locations.database.Database
import ru.raysmith.tgbot.examples.locations.database.entity.Draft
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.utils.locations.loc
import ru.raysmith.tgbot.utils.message.MessageAction
import ru.raysmith.tgbot.utils.messageLocation
import ru.raysmith.tgbot.utils.messageText
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val logger: Logger = LoggerFactory.getLogger("sample-locations")

// ---------------------------------------------------- Bot setup ------------------------------------------------------

val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
val bot = Bot(scope = scope)
    .onError { e -> logger.error(e.message, e) }
    .registerDatePicker(birthDateDatePicker)

suspend fun startBot() = bot.locations {
    config { update -> LocationFlowContextImpl(update) }
    getLocation {
        user.location.name // current user's location
    }
    updateLocation { newLocation ->
        transaction {
            user.location = Location.valueOf(newLocation.name) // update user's location
        }
    }

    /* Global handlers setup */

    global {
        handleCommand {
            isCommand(BotCommand.START) {
                loc.user.setupCommandsList() // rewrite commands of other bot (or sample)
                messageStart()
            }

            isCommand(BotCommand.FORM) {
                transaction {
                    loc.user.currentDraft = Draft.new {  }
                }
                toLocation(Location.NAME)
            }
        }
    }

    /* Locations setup */

    location(Location.MENU) {
        // same as global
    }

    location(Location.NAME) {
        onEnter {
            messageFormName(loc.nextLocationMessageAction ?: MessageAction.SEND)
        }

        handleMessage {
            withCurrentDraft { draft ->
                messageText()
                    .verify("Please, provide your full name, e.g. John Doe") { text ->
                        text.isNotEmpty() && text.contains(" ") // have at least 2 words
                    }
                    .onNull { messageFormName(MessageAction.SEND, prefix = it) }
                    .onResult {
                        draft.name = it
                        toLocation(Location.BIRTH_DATE, MessageAction.SEND)
                    }
            }
        }

        handleCallbackQuery {
            isBack {
                transaction {
                    loc.user.currentDraft?.delete()
                }

                edit("You returned to the main menu. Use /${BotCommand.FORM} to start filling out an abstract form.")
                toLocation(Location.MENU)
            }
        }
    }

    location(Location.BIRTH_DATE) {
        onEnter {
            messageFormBirthDate(loc.nextLocationMessageAction ?: MessageAction.SEND)
        }

        handleCallbackQuery {
            isBack {
                toLocation(Location.NAME, MessageAction.EDIT)
            }

            datePickerResult(birthDateDatePicker) { date ->
                withCurrentDraft { draft ->
                    if (ChronoUnit.YEARS.between(date, LocalDate.now()) < 18) {
                        alert("You must be at least 18 years old to continue.")
                    } else {
                        draft.birthDate = date
                        toLocation(Location.ADDRESS, MessageAction.EDIT)
                    }
                }
            }
        }
    }

    location(Location.ADDRESS) {
        onEnter {
            messageFormBirthAddress(loc.nextLocationMessageAction ?: MessageAction.SEND)
        }

        handleMessage {
            withCurrentDraft { draft ->
                messageLocation {
                    draft.address = "${it.latitude} ${it.longitude}"
                } ?: messageText {
                    draft.address = it
                } ?: run {
                    messageFormBirthAddress(MessageAction.SEND)
                    return@withCurrentDraft
                }

                if (draft.isCompleted()) {
                    messageDraft(draft, MessageAction.SEND)
                } else {
                    send("Form was changed in process and your draft can't be completed. Please, start again with /${BotCommand.FORM}")
                }
                toLocation(Location.MENU, MessageAction.EDIT)
            }
        }
    }
}

// ------------------------------------------------------ main ---------------------------------------------------------

suspend fun main() {
    Database.init()
    startBot()
}