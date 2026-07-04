package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.model.api.updates.Update

interface LocationFlowContext {
    val update: Update
}

context(_: LFC)
val <LFC : LocationFlowContext> loc: LFC get() = contextOf<LFC>()

/** Empty implementation of LocationFlowContext */
class LocationFlowContextImpl(override val update: Update) : LocationFlowContext

