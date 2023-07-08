package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.model.network.updates.Update

@DslMarker
annotation class LocationsDSL

@DslMarker
annotation class LocationsDSLConfig

interface LocationConfig {
    val update: Update
}

/** Empty implementation of LocationConfig */
class LocationConfigImpl(override val update: Update) : LocationConfig

