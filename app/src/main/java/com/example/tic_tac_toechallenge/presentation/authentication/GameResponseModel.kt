package com.example.tic_tac_toechallenge.presentation.authentication

data class GameResponseModel (
    val player1Id: String,
    val player2Id: String?,
    val status: String,
    val turn: String,
    val winnerId: String,
    val boardState: List<String>
)

