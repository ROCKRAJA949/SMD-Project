package com.example.tic_tac_toechallenge.presentation.authentication

import com.example.tic_tac_toechallenge.presentation.sign_in.UserData

data class UserResponseModel (
    val userId: String,
    val username: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val winCount: Number?,
    val lossCount: Number?,
    val friends: List<UserResponseModel>?,
)

