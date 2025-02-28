package com.example.frisenbattaultisensmartcompanion

import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.os.Handler
import android.os.Looper

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Retrieve the event object from the intent
            val event = intent.getSerializableExtra("EVENT") as? Event
            if (event != null) {
                EventDetailScreen(event = event, onBackClick = { finish() })
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.Red) // Fond rouge
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, fontSize = 16.sp, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(event: Event, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val preferences: SharedPreferences = context.getSharedPreferences("EventPreferences", Context.MODE_PRIVATE)

    // Load the notification status from SharedPreferences
    val notificationStatus = remember { mutableStateOf(preferences.getBoolean(event.id, false)) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    // Titre centré dans le SmallTopAppBar
                    Text(
                        text = "Event Details",
                        style = typography.titleLarge.copy(color = Color.White), // Couleur blanche pour le texte
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Centrer le texte
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // Toggle notification status and save to SharedPreferences
                        val newStatus = !notificationStatus.value
                        notificationStatus.value = newStatus
                        preferences.edit().putBoolean(event.id, newStatus).apply()
                        // Trigger notification 10 seconds after subscription
                        if (newStatus) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                sendNotification(context, event)
                            }, 10000) // 10 seconds delay
                        }
                    }) {
                        val icon = if (notificationStatus.value) {
                            Icons.Filled.Notifications
                        } else {
                            Icons.Filled.Clear
                        }
                        Icon(icon, contentDescription = "Notification Icon", tint = Color.White)
                    }
                    Text(
                        text = if (notificationStatus.value) "Unmute" else "Mute",
                        color = Color.White,
                        modifier = Modifier.padding(end = 16.dp) // Add some padding to align the text
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Red) // Fond rouge
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Titre avec Card et couleur rouge
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Title: ${event.title}",
                    style = typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Description dans une Card avec fond léger
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Description: ${event.description}",
                    style = typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.padding(16.dp)

                )
            }

            // Date dans une Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Date: ${event.date}",
                    style = typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.padding(16.dp),
                )
            }

            // Location dans une Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Location: ${event.location}",
                    style = typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.padding(16.dp),
                )
            }

            // Category dans une Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Category: ${event.category}",
                    style = typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

fun sendNotification(context: Context, event: Event) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Create notification channel for devices with API level 26 and higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "event_notifications",
            "Event Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    // Create notification
    val notification: Notification = NotificationCompat.Builder(context, "event_notifications")
        .setContentTitle("Event Reminder")
        .setContentText("Don't forget about the event: ${event.title}")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    // Send notification
    notificationManager.notify(event.id.hashCode(), notification) // Unique ID for each event
}