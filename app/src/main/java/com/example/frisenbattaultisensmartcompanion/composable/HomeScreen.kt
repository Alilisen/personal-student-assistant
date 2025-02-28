package com.example.frisenbattaultisensmartcompanion.composable


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frisenbattaultisensmartcompanion.R
import com.example.frisenbattaultisensmartcompanion.gemini.Gemini
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frisenbattaultisensmartcompanion.database.QuestionResponse


@Composable
fun HomeView(viewModel: ChatViewModel = viewModel()) {

    Column(modifier = Modifier.fillMaxSize()) {
        Logotop()
        Chatbot(viewModel = viewModel)
    }
}

@Composable
fun Logotop() {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Prend toute la largeur
            .padding(top = 32.dp), // Ajoute un espace en haut
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de l'application",
            modifier = Modifier
                .size(150.dp) // Définit la taille de l’image
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chatbot(viewModel: ChatViewModel = viewModel(), modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val temporaryChatHistory = remember { mutableStateOf(mutableListOf<QuestionResponse>()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Affichage des messages depuis la DB
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            temporaryChatHistory.value.forEach { message ->
                Text(
                    text = "You <3: ${message.question}",
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Red)
                        .fillMaxWidth(),
                    color = Color.White
                )
                Text(
                    text = "Bot: ${message.response}",
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray)
                        .fillMaxWidth(),
                    color = Color.Black
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { newText -> text = newText },
                label = { Text("Ask a question...") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Red.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Red,
                    cursorColor = Color.Red
                )
            )
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    coroutineScope.launch {
                        val analyzedResponse = Gemini.generateResponse(text).trim()
                        viewModel.addMessage(text, analyzedResponse)
                        temporaryChatHistory.value.add(QuestionResponse(question = text, response = analyzedResponse))
                        text = ""
                    }
                } else {
                    Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show()
                    text = ""
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.Red
                )
            }
        }
    }
}