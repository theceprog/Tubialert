package com.proj.tubialert.ui.home

import android.util.Log
import androidx.activity.result.launch
import androidx.annotation.DrawableRes
import java.util.Locale
import androidx.core.util.remove
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.proj.tubialert.FirestoreService
import com.proj.tubialert.R
import com.proj.tubialert.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.format
import kotlin.text.isNullOrEmpty


class HomeViewModel : ViewModel() {
    private val firestoreService = FirestoreService()
    private var sensorListenerRegistration: ListenerRegistration? = null

    // LiveData for rain
    private val _rainText = MutableLiveData<String>()
    val rainText: LiveData<String> = _rainText

    // LiveData for temperature
    private val _tempText = MutableLiveData<String>()
    val tempText: LiveData<String> = _tempText

    // LiveData for water level
    private val _waterText = MutableLiveData<String>()
    val waterText: LiveData<String> = _waterText

    private val _statusText = MutableLiveData<String>()
    val statusText: LiveData<String> = _statusText

    // LiveData for a generic status or error message
    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage


    private val _weatherImageResId = MutableLiveData<Int>()

    @get:DrawableRes
    val weatherImageResId: LiveData<Int> = _weatherImageResId

    private val _lastUpdateText = MutableLiveData<String>()
    val lastUpdateText: LiveData<String> = _lastUpdateText

    private val _weatherDescriptionText = MutableLiveData<String>()
    val weatherDescriptionText: LiveData<String> = _weatherDescriptionText

    private val _rainChanceText = MutableLiveData<String>()
    val rainChanceText: LiveData<String> = _rainChanceText

    private val _accuWeatherStatus = MutableLiveData<String>()
    val accuWeatherStatus: LiveData<String> = _accuWeatherStatus

    private val accuWeatherService = RetrofitClient.instance
    private val apiKey = "BphYMJ4IIbBH9XXfPnBUGTcn0EZIfoOb"
    private val locationKey = "3406785"


    fun fetchAccuWeatherData() {
        _accuWeatherStatus.value = "Loading weather..."
        viewModelScope.launch {
            try {
                val currentConditionsResponse =
                    accuWeatherService.getCurrentConditions(locationKey, apiKey)

                if (currentConditionsResponse.isSuccessful) {
                    val conditionsList = currentConditionsResponse.body()
                    if (!conditionsList.isNullOrEmpty()) {
                        val currentCondition = conditionsList[0]

                        // Update Last Update Time
                        currentCondition.localObservationDateTime?.let { dateTimeString ->
                            _lastUpdateText.value = formatAccuWeatherDateTime(dateTimeString)
                        } ?: run { _lastUpdateText.value = "N/A" }

                        // Update Weather Description Text
                        val weatherText = currentCondition.weatherText ?: "N/A"
                        _weatherDescriptionText.value = weatherText

                        // Update Weather Image based on weatherText
                        _weatherImageResId.value = getWeatherImageResource(weatherText)

                        // Update Chance of Rain
                        currentCondition.precipitationProbability?.let { probability ->
                            _rainChanceText.value = "$probability% chance of rain"
                        }
                        _accuWeatherStatus.value = "Weather updated"

                    } else {
                        _accuWeatherStatus.value = "No weather data received."
                        setDefaultWeatherTextsAndImage()
                    }
                } else {
                    _accuWeatherStatus.value = "Error: ${currentConditionsResponse.message()}"
                    setDefaultWeatherTextsAndImage()
                }
            } catch (e: Exception) {
                _accuWeatherStatus.value = "Failed to load weather: ${e.message}"
                Log.e("HomeViewModel", "AccuWeather API Error", e)
                setDefaultWeatherTextsAndImage()
            }
        }
    }

    private fun setDefaultWeatherTextsAndImage() {
        _lastUpdateText.value = "N/A"
        _weatherDescriptionText.value = "N/A"
        _rainChanceText.value = "N/A"
        _weatherImageResId.value = R.drawable.sanm // Your default image
    }

    private fun formatAccuWeatherDateTime(dateTimeString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val date: Date? = inputFormat.parse(dateTimeString)
            date?.let {
                val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                "As of " + outputFormat.format(it)
            } ?: "Invalid date"
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Date parsing error", e)
            "N/A"
        }
    }

    private fun getWeatherImageResource(weatherDescription: String?): Int {
        Log.i("HomeViewModel", "Weather Description: $weatherDescription")
        return when (weatherDescription?.lowercase()) {

            "sunny", "clear" -> R.drawable.ic_weather_sunny
            "mostly sunny", "partly sunny" -> R.drawable.ic_weather_partly_cloudy
            "cloudy", "overcast" -> R.drawable.ic_weather_cloudy
            "partly cloudy", "mostly cloudy" -> R.drawable.ic_weather_partly_cloudy
            "rain", "showers", "light rain", "heavy rain", "drizzle" -> R.drawable.ic_weather_rain
            "thunderstorms", "t-storms" -> R.drawable.ic_weather_thunderstorm
            else -> R.drawable.sanm
        }
    }


    fun startListeningForSensorUpdates() {
        if (sensorListenerRegistration != null) return
        sensorListenerRegistration = firestoreService.listenForSensorUpdates(
            onUpdate = { rain, temp, water, status ->
                _rainText.value = rain?.let { "%.1f mm/hr".format(it) } ?: "N/A"
                _tempText.value = temp?.let { "%.1f °C".format(it) } ?: "N/A"
                _waterText.value = water?.let { "%.0f %%".format(it) } ?: "N/A"
                _statusText.value = status ?: "N/A"
            },
            onError = { exception ->
                _statusMessage.value = "Error: ${exception.message}"
                _rainText.value = "Error"; _tempText.value = "Error"; _waterText.value =
                "Error"; _statusText.value = "Error"
            }
        )
    }

    fun fetchSensorDataOnce() {
        firestoreService.getSensorData(
            onSuccess = { rain, temp, water, status ->
                _rainText.value = rain?.let { "%.1f mm/hr".format(it) } ?: "N/A"
                _tempText.value = temp?.let { "%.1f °C".format(it) } ?: "N/A"
                _waterText.value = water?.let { "%.0f %%".format(it) } ?: "N/A"
                _statusText.value = status ?: "N/A"
            },
            onFailure = { exception ->
                _statusMessage.value = "Error: ${exception.message}"
                _rainText.value = "Error"; _tempText.value = "Error"; _waterText.value =
                "Error"; _statusText.value = "Error"
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        sensorListenerRegistration?.remove()
    }
}