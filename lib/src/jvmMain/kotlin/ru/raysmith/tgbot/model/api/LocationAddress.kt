package ru.raysmith.tgbot.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes the physical address of a location. */
@Serializable
data class LocationAddress(

    /** The two-letter ISO 3166-1 alpha-2 country code of the country where the location is located */
    @SerialName("country_code") val countryCode: String,

    /** State of the location */
    @SerialName("state") val state: String? = null,

    /** City of the location */
    @SerialName("city") val city: String? = null,

    /** Street address of the location */
    @SerialName("street") val street: String? = null,
)
