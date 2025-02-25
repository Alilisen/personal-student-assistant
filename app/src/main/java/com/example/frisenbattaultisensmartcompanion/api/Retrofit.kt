package com.example.frisenbattaultisensmartcompanion.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private const val BASE_URL =
        "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    val api: API by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}


