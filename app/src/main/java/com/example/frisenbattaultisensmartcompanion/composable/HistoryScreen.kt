package com.example.frisenbattaultisensmartcompanion.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HistoryScreen(viewModel: ChatViewModel = viewModel()) {
    val chatHistory by viewModel.chatMessages.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        chatHistory.forEach { chat ->
            val date = Date(chat.timestamp)
            val formattedDate = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(date)

            // Regroupement de chaque question/réponse dans une Card pour un affichage propre
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(Color.Red), // Fond rouge de la Card
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp), // Définir l'élévation de la Card
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    // Question
                    Text(
                        text = "You: ${chat.question}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )

                    // Réponse
                    Text(
                        text = "Bot: ${chat.response}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )

                    // Date
                    Text(
                        text = "Date: $formattedDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Bouton de suppression
                    Button(
                        onClick = {
                            viewModel.deleteMessage(chat.id) // Appeler la fonction de suppression dans le ViewModel
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Supprimer")
                    }
                }
            }
        }
    }
}