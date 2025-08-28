package com.proj.tubialert.ui.home

import android.util.Log
import androidx.core.util.remove
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.proj.tubialert.FirestoreService


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

    // --- Option 1: Using individual value callbacks (Modified for formatting) ---
    fun startListeningForSensorUpdates() {
        if (sensorListenerRegistration != null) {
            Log.d("HomeViewModel", "Listener already active.")
            return
        }
        Log.d("HomeViewModel", "Starting to listen for sensor updates.")
        sensorListenerRegistration = firestoreService.listenForSensorUpdates(
            onUpdate = { rain, temp, water, status ->
                Log.i(
                    "HomeViewModel",
                    "Sensor Update Received: Rain: $rain, Temp: $temp, Water: $water, Status: $status"
                )

                _rainText.value = rain?.let { "%.1f mm/hr".format(it) } ?: "N/A"
                _tempText.value = temp?.let { "%.1f °C".format(it) } ?: "N/A"
                _waterText.value =
                    water?.let { "%.0f %%".format(it) } ?: "N/A" // %% to escape % in format string
                _statusText.value = status ?: "N/A"
                //_statusMessage.value = "Data updated" // Optional
            },
            onError = { exception ->
                Log.e(
                    "HomeViewModel",
                    "Error listening to sensor updates: ${exception.message}",
                    exception
                )
                _statusMessage.value = "Error: ${exception.message}"
                _rainText.value = "Error"
                _tempText.value = "Error"
                _waterText.value = "Error"
                _statusText.value = "Error"
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
        sensorListenerRegistration?.remove()
        Log.d("HomeViewModel", "Sensor listener removed.")
    }

    // Example for the one-time fetch method with formatting
    fun fetchSensorDataOnce() {
        Log.d("HomeViewModel", "Fetching sensor data once.")
        firestoreService.getSensorData( // Ensure this method exists and works in FirestoreService
            onSuccess = { rain, temp, water, status ->
                _rainText.value = rain?.let { "%.1f mm/hr".format(it) } ?: "N/A"
                _tempText.value = temp?.let { "%.1f °C".format(it) } ?: "N/A"
                _waterText.value = water?.let { "%.0f %%".format(it) } ?: "N/A"
                _statusText.value = status ?: "N/A"
            },
            onFailure = { exception ->
                _statusMessage.value = "Error fetching once: ${exception.message}"
                _rainText.value = "Error"
                _tempText.value = "Error"
                _waterText.value = "Error"
                _statusText.value = "Error"
            }
        )
    }
}
