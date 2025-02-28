package com.example.frisenbattaultisensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frisenbattaultisensmartcompanion.composable.EventView
import com.example.frisenbattaultisensmartcompanion.composable.HistoryScreen
import com.example.frisenbattaultisensmartcompanion.composable.HomeView
import com.example.frisenbattaultisensmartcompanion.ui.theme.FrisenbattaultisensmartcompanionTheme
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale


data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable


//fun Events(): List<Event> {
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    val events = mutableListOf<Event>()
//
//    events.add(
//        Event(
//            id = "1",
//            title = "BDE Evening",
//            description = "A fun afterwork organized by BDE.",
//            date = "2025-03-01",
//            location = "ISEN Toulon",
//            category = "Social"
//        )
//    )
//
//    events.add(
//        Event(
//            id = "2",
//            title = "Gala",
//            description = "Annual Gala event.",
//            date = "2025-05-15",
//            location = "Le Palais des Congrès",
//            category = "Formal"
//        )
//    )
//
//    events.add(
//        Event(
//            id = "3",
//            title = "Cohesion Day",
//            description = "Team building day for all students.",
//            date = "2025-06-10",
//            location = "ISEN Toulon",
//            category = "Team Building"
//        )
//    )
//    events.add(
//        Event(
//            id = "4",
//            title = "Caddie Day",
//            description = "Follow the caddie and find the path of happiness.",
//            date = "2025-06-10",
//            location = "ISEN to Barathym",
//            category = "Drinking party"
//        )
//    )
//    return events
//}


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
                            HistoryScreen()
                        }
                    }
                }
            }
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



    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FrisenbattaultisensmartcompanionTheme {
            Column {
            }
        }
    }
}
