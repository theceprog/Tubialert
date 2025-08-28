package com.proj.tubialert

// AccuWeatherModels.kt (create a new file for these)
// Note: This is a simplified example. Refer to the actual AccuWeather API documentation
// for the precise structure and field names. Use a tool like "JSON to Kotlin Class"
// plugin in Android Studio or online converters to help generate these from sample JSON.

import com.google.android.gms.common.api.Response
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class CurrentConditionResponse(
    @SerializedName("LocalObservationDateTime")
    val localObservationDateTime: String? = null,

    @SerializedName("EpochTime")
    val epochTime: Long? = null,

    @SerializedName("WeatherText")
    val weatherText: String? = null,

    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean? = null,

    @SerializedName("PrecipitationType")
    val precipitationType: String? = null,

    @SerializedName("PrecipitationProbability") // This might be from a different endpoint
    val precipitationProbability: Int? = null,

    @SerializedName("Link")
    val link: String? = null
)

// AccuWeatherModels.kt (add this)
data class HourlyForecastResponse(
    @SerializedName("DateTime")
    val dateTime: String? = null,
    @SerializedName("EpochDateTime")
    val epochDateTime: Long? = null,
    @SerializedName("WeatherIcon")
    val weatherIcon: Int? = null,
    @SerializedName("IconPhrase")
    val iconPhrase: String? = null,
    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean? = null,
    @SerializedName("PrecipitationType")
    val precipitationType: String? = null,
    @SerializedName("PrecipitationIntensity")
    val precipitationIntensity: String? = null,
    @SerializedName("IsDaylight")
    val isDaylight: Boolean? = null,
    @SerializedName("Temperature")
    val temperature: TemperatureValue? = null,
    @SerializedName("PrecipitationProbability") // Key field!
    val precipitationProbability: Int? = null,
    @SerializedName("Link")
    val link: String? = null
)

data class TemperatureValue(
    @SerializedName("Value")
    val value: Double? = null,
    @SerializedName("Unit")
    val unit: String? = null,
    @SerializedName("UnitType")
    val unitType: Int? = null
)

