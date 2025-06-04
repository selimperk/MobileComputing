package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapp.model.Challenge
import com.example.myapp.ui.composables.*
import com.example.myapp.ui.screens.*
import com.example.myapp.ui.theme.HandsOn1stTheme
import com.example.myapp.viewmodel.DataViewModel

class MainActivity : ComponentActivity() {

    // ⬅️ ViewModel erstellen (wie beim Professor)
    private val dataViewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HandsOn1stTheme {
                val navController = rememberNavController()

                // ⬅️ Toggle-Zustand für automatische Syncs speichern
                var isPeriodicSyncEnabled by rememberSaveable { mutableStateOf(false) }

                Scaffold(
                    bottomBar = {
                        OurNavigationBar { route ->
                            navController.navigate(route::class.simpleName!!) {
                                launchSingleTop = true
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeRoute::class.simpleName!!,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(HomeRoute::class.simpleName!!) {
                            HomeScreen(
                                challenges = listOf(
                                    Challenge("Tim", "Jonas", false),
                                    Challenge("Paul", "Lena", true)
                                ),
                                onChallengeClick = { challenge ->
                                    navController.navigate(
                                        ChallengeRoute(
                                            player1 = challenge.player1,
                                            player2 = challenge.player2,
                                            won = challenge.won
                                        )
                                    )
                                }
                            )
                        }

                        composable(AddRoute::class.simpleName!!) {
                            AddScreen()
                        }

                        composable(SettingsRoute::class.simpleName!!) {
                            // ⬅️ Hier Übergibst du dem Screen echte Logik:
                            SettingsScreen(
                                onManualSync = { dataViewModel.initDataSync() },
                                isPeriodicSyncEnabled = isPeriodicSyncEnabled,
                                onPeriodicSyncToggled = { enabled ->
                                    isPeriodicSyncEnabled = enabled
                                    if (enabled) {
                                        dataViewModel.initPeriodicDataSync()
                                    } else {
                                        dataViewModel.cancelPeriodicDataSync()
                                    }
                                }
                            )
                        }

                        composable<ChallengeRoute> {
                            val data = it.toRoute<ChallengeRoute>()
                            ChallengeScreen(
                                player1 = data.player1,
                                player2 = data.player2,
                                won = data.won
                            )
                        }
                    }
                }
            }
        }
    }
}
