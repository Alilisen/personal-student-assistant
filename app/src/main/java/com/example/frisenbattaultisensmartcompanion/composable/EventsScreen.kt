package com.example.frisenbattaultisensmartcompanion.composable

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.frisenbattaultisensmartcompanion.Event
import com.example.frisenbattaultisensmartcompanion.EventDetailActivity
import com.example.frisenbattaultisensmartcompanion.EventItem
import com.example.frisenbattaultisensmartcompanion.api.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventView() {
    val events = remember { mutableStateOf<List<Event>>(emptyList()) } // Liste dynamique des événements
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Effectuer la requête pour récupérer les événements depuis l'API
    LaunchedEffect(true) {
        Retrofit.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    events.value = response.body() ?: emptyList()
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                isLoading.value = false
                // Gérer l'erreur si nécessaire
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ISEN Events",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Red)
            )
        }
    ) { innerPadding ->
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize()) // Afficher un indicateur de chargement
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(events.value) { event ->
                    EventItem(event = event, onClick = {
                        // Ouvrir EventDetailActivity avec les détails de l'événement
                        val intent = Intent(context, EventDetailActivity::class.java).apply {
                            putExtra("EVENT", event) // Passe l'objet Event directement
                        }
                        context.startActivity(intent)
                    })
                }
            }
        }
    }
}