package com.proj.tubialert

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
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
    }  fun loginUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Input validation
        if (email.isBlank() || password.isBlank()) {
            onFailure(Exception("Email and password cannot be empty"))
            return
        }

        val docRef = db.collection("users").document(email)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)

                    if (user != null) {
                        // Verify password (you should use proper password hashing)
                        if (user.password == password) { // In production, use proper password verification
                            onSuccess(user)
                        } else {
                            onFailure(Exception("Invalid password"))
                        }
                    } else {
                        onFailure(Exception("User data corrupted"))
                    }
                } else {
                    onFailure(Exception("User not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun signUpUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Input validation
        if (user.email.isBlank() || user.password.isBlank() || user.name.isBlank()) {
            onFailure(Exception("All fields are required"))
            return
        }

        // Check if user already exists
        db.collection("users")
            .document(user.email)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onFailure(Exception("User with this email already exists"))
                } else {
                    // Create new user
                    val userData = hashMapOf(
                        "email" to user.email,
                        "password" to user.password, // Hash this in production
                        "name" to user.name,
                        "phone" to user.phone,
                        "gender" to user.gender,
                        "createdAt" to Timestamp.now()
                    )

                    db.collection("users")
                        .document(user.email)
                        .set(userData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Password validation helper
    fun isValidPassword(password: String): Boolean {
        val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$".toRegex()
        return pattern.matches(password)
    }

    // Email validation helper
    fun isValidEmail(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return pattern.matches(email)
    }
}
