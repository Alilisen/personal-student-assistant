package com.example.frisenbattaultisensmartcompanion.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import kotlin.math.log

@Composable
fun HomeView() {
    Column(modifier = Modifier.fillMaxSize()) {
        Logotop()
        Chatbot()
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
fun Chatbot(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var savedchat by remember { mutableStateOf("") }
    var ListChat by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var analyzedResponse by remember { mutableStateOf("") }

    val fakeResponses = listOf(
        "I'm just a bot, but I'm listening!",
        "Interesting! Tell me more.",
        "I'm not sure I understand 🤔",
        "Good question!",
        "Try asking me another question."
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Affichage des messages
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 16.dp).verticalScroll(rememberScrollState())
        ) {
            ListChat.forEach { (userMsg, analyzedResponse) ->
                // Message de l'utilisateur avec fond rouge
                Text(
                    text = "You <3: $userMsg",
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Red)
                        .fillMaxWidth(),
                    color = Color.White // Texte blanc pour l'utilisateur
                )
                // Message du bot avec fond rouge clair
                Text(
                    text = "Bot: $analyzedResponse",
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray)
                        .fillMaxWidth(),
                    color = Color.Black // Texte noir pour le bot
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
                    containerColor = Color.Red.copy(alpha = 0.1f), // Fond rouge clair
                    focusedIndicatorColor = Color.Red, // Indicateur de focus rouge
                    unfocusedIndicatorColor = Color.Red, // Indicateur de non-focus rouge
                    cursorColor = Color.Red // Curseur rouge
                )
            )
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    coroutineScope.launch {
                        savedchat = text
                        text = ""
                        analyzedResponse = Gemini.generateResponse(savedchat).trim()
                        Log.d("TAG", "$analyzedResponse")
                        ListChat = ListChat+listOf(Pair(savedchat, analyzedResponse))
                    }
                } else {
                    Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show()
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