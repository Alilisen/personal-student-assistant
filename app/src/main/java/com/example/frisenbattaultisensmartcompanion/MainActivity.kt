package com.example.frisenbattaultisensmartcompanion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*
import com.example.frisenbattaultisensmartcompanion.ui.theme.FrisenbattaultisensmartcompanionTheme
import java.io.Serializable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.frisenbattaultisensmartcompanion.api.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable


fun Events(): List<Event> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val events = mutableListOf<Event>()

    events.add(
        Event(
            id = "1",
            title = "BDE Evening",
            description = "A fun afterwork organized by BDE.",
            date = "2025-03-01",
            location = "ISEN Toulon",
            category = "Social"
        )
    )

    events.add(
        Event(
            id = "2",
            title = "Gala",
            description = "Annual Gala event.",
            date = "2025-05-15",
            location = "Le Palais des Congrès",
            category = "Formal"
        )
    )

    events.add(
        Event(
            id = "3",
            title = "Cohesion Day",
            description = "Team building day for all students.",
            date = "2025-06-10",
            location = "ISEN Toulon",
            category = "Team Building"
        )
    )
    events.add(
        Event(
            id = "4",
            title = "Caddie Day",
            description = "Follow the caddie and find the path of happiness.",
            date = "2025-06-10",
            location = "ISEN to Barathym",
            category = "Drinking party"
        )
    )
    return events
}


data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrisenbattaultisensmartcompanionTheme {
                val tabBarItems = listOf(
                    TabBarItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Filled.Home
                    ),
                    TabBarItem(
                        title = "Events",
                        selectedIcon = Icons.AutoMirrored.Filled.List,
                        unselectedIcon = Icons.AutoMirrored.Filled.List
                    ),
                    TabBarItem(
                        title = "History",
                        selectedIcon = Icons.Filled.Notifications,
                        unselectedIcon = Icons.Filled.Notifications
                    )
                )

                // NavController pour gérer la navigation
                val navController = rememberNavController()

                Scaffold(

                    bottomBar = {
                        // Ajoute la barre de navigation ici
                        TabView(tabBarItems, navController)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home", // Définir la première destination
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeView()
                        }
                        composable("events") {
                            EventView()
                        }
                        composable("history") {
                            HistoryView()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun HomeView() {
        Column(modifier = Modifier.fillMaxSize()) {
            Logotop()
            Chatbot()
        }
    }

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

    @Composable
    fun HistoryView() {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Here is the History!")
        }
    }

    @Composable
    fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

        NavigationBar (
            containerColor = Color.Red // Ajouter la couleur rouge ici
        ) {
            tabBarItems.forEachIndexed { index, tabBarItem ->
                NavigationBarItem(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        val route = when (tabBarItem.title) {
                            "Home" -> "home"
                            "Events" -> "events"
                            "History" -> "history"
                            else -> "home"
                        }
                        navController.navigate(route)
                    },
                    icon = {
                        TabBarIconView(
                            isSelected = selectedTabIndex == index,
                            selectedIcon = tabBarItem.selectedIcon,
                            unselectedIcon = tabBarItem.unselectedIcon,
                            title = tabBarItem.title
                        )
                    },
                    label = { Text(tabBarItem.title) }
                )
            }
        }
    }

    @Composable
    fun TabBarIconView(
        isSelected: Boolean,
        selectedIcon: ImageVector,
        unselectedIcon: ImageVector,
        title: String
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = title
        )
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
        var messages by remember { mutableStateOf(listOf<Pair<String, String>>()) } // Liste des messages (utilisateur -> IA)
        val context = LocalContext.current

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
                    .padding(bottom = 16.dp)
            ) {
                messages.forEach { (userMsg, botResponse) ->
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
                        text = "Bot: $botResponse",
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
                    if (text.isNotBlank()) {
                        val botResponse =
                            fakeResponses.random() // Sélectionne une réponse au hasard
                        messages =
                            messages + (text to botResponse) // Ajoute le message et la réponse
                        Log.d("Chatbot", "Message envoyé: $text | Réponse IA: $botResponse")
                        Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                        text = "" // Effacer le champ après l'envoi
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

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FrisenbattaultisensmartcompanionTheme {
            Column {
                Logotop()
                Chatbot()
            }
        }
    }
}
