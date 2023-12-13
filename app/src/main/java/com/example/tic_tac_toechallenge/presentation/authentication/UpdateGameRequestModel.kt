package com.example.tic_tac_toechallenge.presentation.authentication

data class UpdateGameRequestModel (
    val id:String,
    val boardState:List<String>,
    val playerId: String
)
