package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapp.model.Challenge
import com.example.myapp.ui.composables.*
import com.example.myapp.ui.screens.*
import com.example.myapp.ui.theme.HandsOn1stTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HandsOn1stTheme {
                val navController = rememberNavController()

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
                        modifier = androidx.compose.ui.Modifier.padding(paddingValues)
                    ) {
                        composable(HomeRoute::class.simpleName!!) {
                            HomeScreen(
                                challenges = listOf(
                                    Challenge("Tim", "Jonas", false),
                                    Challenge("Paul", "Lena", true)
                                ),
                                onChallengeClick = { challenge ->
                                    navController.navigate(
                                        `ChallengeRoute.kt`(
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
                            SettingsScreen()
                        }
                        composable<`ChallengeRoute.kt`> {
                            val data = it.toRoute<`ChallengeRoute.kt`>()
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
