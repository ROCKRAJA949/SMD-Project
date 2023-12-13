package com.example.tic_tac_toechallenge.presentation.sign_in

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.example.tic_tac_toechallenge.R
import com.example.tic_tac_toechallenge.presentation.authentication.GameResponseModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.lang.StringBuilder
import java.util.concurrent.CancellationException

class GoogleAuthUIClient (
    private val context: Context,
    private val oneTapClient: SignInClient,
    ){
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val usersDb = FirebaseFirestore.getInstance().collection("users")
    private val gamesDb = FirebaseFirestore.getInstance().collection("games")
    suspend fun signIn():IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender

    }





    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            var authResult = auth.signInWithCredential(googleCredentials).await()
            val user = authResult.user
            if(authResult.additionalUserInfo?.isNewUser == true) {

                val newUser =
                    user?.run {
                        UserData(
                            userId = uid,
                            username = displayName,
                            email = email,
                            profilePictureUrl = photoUrl?.toString(),
                            winCount = 0,
                            lossCount = 0,
                            friends = null
                        )
                    }

                if (newUser != null) {
                    db.collection("users").document(user.uid).set(newUser)
                        .addOnSuccessListener{
                            Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { e ->
                            Toast.makeText(context, "Exception: $e", Toast.LENGTH_SHORT).show()
                        }
//                    usersDb.add(newUser).addOnSuccessListener{
//                        Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener { e ->
//                        Toast.makeText(context, "Exception: $e", Toast.LENGTH_SHORT).show()
//                    }
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                        winCount = null,
                        lossCount = null,
                        friends = null
                    )
                },
                errorMessage = null
            )
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }

    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e

        }
    }


    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString(),
            winCount = null,
            lossCount = null,
            friends = null,
        )
    }

    private fun buildSignInRequest():BeginSignInRequest {
        return BeginSignInRequest.Builder().setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.DEFAULT_WEB_CLIENT_ID))
                .build()
            )
            .setAutoSelectEnabled(false)
            .build()
    }
}