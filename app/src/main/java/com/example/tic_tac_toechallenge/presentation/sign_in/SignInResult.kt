package com.example.tic_tac_toechallenge.presentation.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,

    )

data class UserData(
    val userId: String,
    val username: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val winCount: Number?,
    val friends: List<UserData>?,
)