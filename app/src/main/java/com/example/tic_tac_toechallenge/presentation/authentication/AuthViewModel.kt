package com.example.tic_tac_toechallenge.presentation.authentication

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    // Check if it's the user's first sign-in
                    if (authResult.additionalUserInfo?.isNewUser == true) {
                        // Store additional data in Firestore
                        val userData = hashMapOf(
                            "displayName" to user.displayName,
                            "email" to user.email,
                            // Add other data fields as needed
                        )

                        firestore.collection("users")
                            .document(user.uid)
                            .set(userData)
                    }
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}
