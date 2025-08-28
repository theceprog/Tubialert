package com.proj.tubialert

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class FirestoreService {

    private val db = Firebase.firestore

    // Your existing getSensorData method (fetches once)
    fun getSensorData(
        onSuccess: (rain: Double?, temp: Double?, water: Double?, status: String?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = db.collection("sensor").document("1")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("FirestoreService", "DocumentSnapshot data (get): ${document.data}")
                    val rain = document.getDouble("rain")
                    val temp = document.getDouble("temp")
                    val water = document.getDouble("water")
                    val status = document.getString("status")
                    onSuccess(rain, temp, water, status)
                } else {
                    Log.d("FirestoreService", "No such document (get)")
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreService", "Error getting document (get): ", exception)
                onFailure(exception)
            }
    }

    // New method to listen for realtime updates
    fun listenForSensorUpdates(
        onUpdate: (rain: Double?, temp: Double?, water: Double?, status: String?) -> Unit,
        onError: (FirebaseFirestoreException) -> Unit
    ): ListenerRegistration { // Return ListenerRegistration to allow detaching
        val docRef = db.collection("sensor").document("1")

        // The addSnapshotListener method is key here
        return docRef.addSnapshotListener { snapshot: DocumentSnapshot?, error: FirebaseFirestoreException? ->
            if (error != null) {
                Log.w("FirestoreService", "Listen failed.", error)
                onError(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("FirestoreService", "Current data (listen): ${snapshot.data}")
                val rain = snapshot.getDouble("rain")
                val temp = snapshot.getDouble("temp")
                val water = snapshot.getDouble("water")
                val status = snapshot.getString("status")

                onUpdate(rain, temp, water, status)
            } else {
                Log.d("FirestoreService", "Current data: null (listen or document deleted)")
                // You might want to signal that the document was deleted or doesn't exist
                onUpdate(null, null, null, null) // Or handle this case specifically
            }
        }
    }

    // Example with custom object (similar to your existing one but for listeners)
    data class SensorReadings( // Make sure this matches your Firestore document structure
        val rain: Double? = null,
        val temp: Double? = null,
        val water: Double? = null
    )

    fun listenForSensorUpdatesAsCustomObject(
        onUpdate: (readings: SensorReadings?) -> Unit,
        onError: (FirebaseFirestoreException) -> Unit
    ): ListenerRegistration {
        val docRef = db.collection("sensor").document("1")
        return docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val readings = snapshot.toObject(SensorReadings::class.java)
                onUpdate(readings)
            } else {
                onUpdate(null) // Document deleted or does not exist
            }
        }
    }
}
