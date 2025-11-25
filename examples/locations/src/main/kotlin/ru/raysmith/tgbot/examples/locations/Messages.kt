package ru.raysmith.tgbot.examples.locations

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButton
import ru.raysmith.tgbot.examples.locations.database.entity.Draft
import ru.raysmith.tgbot.utils.message.MessageAction
import ru.raysmith.tgbot.utils.message.message
import ru.raysmith.tgbot.utils.n

suspend fun EventHandler.messageStart(action: MessageAction = MessageAction.SEND) = message(action) {
    textWithEntities {
        text("This is a bot that works with locations.").n()
        n()
        text("Use /${BotCommand.FORM} to start filling out an abstract form. ")
            .text("You will go through 3 locations (")
            .code(Location.MENU).text(" → ").code(Location.BIRTH_DATE).text(" → ").code(Location.ADDRESS)
            .text("), receive the result, and be returned to the ").code(Location.MENU)
            .text(" location you are currently in.")
    }
}

suspend fun EventHandler.messageFormName(action: MessageAction, prefix: String? = null) = message(action) {
    textWithEntities {
        italic(prefix).n()
        n()
        text("Send your full name")
    }
    inlineKeyboard {
        back(text = KeyboardButton.CANCEL)
    }
}

context(_: BotContext<*>) // BotContext is required because of the date pickers global config // TODO [2.2] should be BotHolder
suspend fun EventHandler.messageFormBirthDate(action: MessageAction) = message(action) {
    datePicker(birthDateDatePicker)
}

suspend fun EventHandler.messageFormBirthAddress(action: MessageAction) = message(action) {
    text = "Send your address or location"
}

suspend fun EventHandler.messageDraft(draft: Draft, action: MessageAction) = message(action) {
    textWithEntities {
        text("Your draft:").n()
        n()
        text("Name: ").bold(draft.name).n()
        text("Birth date: ").bold(draft.birthDate).n()
        text("Address: ").bold(draft.address).n()
        n()
        text("You've been returned to MENU location. Use /${BotCommand.FORM} to start filling out a new form.")
    }
}