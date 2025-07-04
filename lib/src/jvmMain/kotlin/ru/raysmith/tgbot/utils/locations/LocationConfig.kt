package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.model.network.updates.Update



//@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
//@DslMarker
//annotation class LocationsDSLConfig

interface LocationConfig {
    val update: Update
}

/** Empty implementation of LocationConfig */
class LocationConfigImpl(override val update: Update) : LocationConfig

