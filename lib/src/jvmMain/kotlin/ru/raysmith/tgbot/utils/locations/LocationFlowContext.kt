package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.model.network.updates.Update

interface LocationFlowContext {
    val update: Update
}

context(_: L)
val <L : LocationFlowContext> loc: L get() = contextOf()

/** Empty implementation of LocationFlowContext */
class LocationFlowContextImpl(override val update: Update) : LocationFlowContext

