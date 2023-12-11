package com.example.tic_tac_toechallenge.presentation.network

import com.example.tic_tac_toechallenge.presentation.authentication.UserResponseModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("user/{id}")
    suspend fun fetchData(@Path("id") id: String?): UserResponseModel
}
