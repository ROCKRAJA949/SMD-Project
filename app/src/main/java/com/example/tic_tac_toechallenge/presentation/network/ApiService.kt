package com.example.tic_tac_toechallenge.presentation.network

import com.example.tic_tac_toechallenge.presentation.authentication.GameRequestModel
import com.example.tic_tac_toechallenge.presentation.authentication.GameResponseModel
import com.example.tic_tac_toechallenge.presentation.authentication.JoinGameRequestModel
import com.example.tic_tac_toechallenge.presentation.authentication.MessageResponseModel
import com.example.tic_tac_toechallenge.presentation.authentication.UserResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    //get user
    @GET("user/{id}")
    suspend fun fetchData(@Path("id") id: String?): UserResponseModel

    //create game
    @POST("game/create")
    fun createGame(@Body gameRequestModel: GameRequestModel): Call<GameResponseModel>

    //get game
    @GET("game/{id}")
    suspend fun fetchGameData(@Path("id") id: String?): Response<GameResponseModel>

    //join game
    @PUT("game/join")
    fun joinGame(@Body joinGameRequestModel: JoinGameRequestModel): Call<MessageResponseModel>
}
