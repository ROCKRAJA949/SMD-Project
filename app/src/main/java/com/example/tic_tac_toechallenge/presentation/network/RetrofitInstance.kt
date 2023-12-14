package com.example.tic_tac_toechallenge.presentation.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://app-ipsygc6sjq-uc.a.run.app//"

//    Gson gson = new GsonBuilder()
//    .setLenient()
//    .create()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
