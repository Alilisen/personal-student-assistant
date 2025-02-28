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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Date


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
            Text(
                text = "You: ${chat.question}",
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Red)
                    .fillMaxWidth(),
                color = Color.White
            )
            Text(
                text = "Bot: ${chat.response}",
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray)
                    .fillMaxWidth(),
                color = Color.Black
            )
            val date = Date(chat.timestamp)
            Text(
                text = "Date: ${date.toString()}",
                modifier = Modifier.padding(8.dp),
                color = Color.Gray
            )
        }
    }
}
