package com.example.frisenbattaultisensmartcompanion.api

import com.example.frisenbattaultisensmartcompanion.Event
import retrofit2.Call
import retrofit2.http.GET

interface API  {
    @GET("events.json")
    fun getEvents(): Call<List<Event>> // Map<String, Event> car Firebase renvoie une map d'objets avec un ID unique.
}
