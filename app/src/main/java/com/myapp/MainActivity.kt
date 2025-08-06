package com.myapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.myapp.ui.composables.OurNavigationBar
import com.myapp.ui.screens.ChallengeScreen
import com.myapp.ui.screens.HomeScreen
import com.myapp.Route.ChallengeRoute
import com.myapp.Route.HomeRoute
import com.myapp.Route.ParticipantRoute
import com.myapp.Route.SettingsRoute
import com.myapp.model.Challenge
import com.myapp.ui.screens.ParticipantScreen
import com.myapp.ui.screens.SettingsScreen
import com.myapp.ui.theme.PersistenceTheme
import com.myapp.viewmodel.DataViewModel
import com.myapp.util.NotificationScheduler

class MainActivity : ComponentActivity() {

    private val dataViewModel: DataViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                NotificationScheduler.scheduleDailyNotification(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔔 Berechtigung für Notifications einholen (ab Android 13)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                NotificationScheduler.scheduleDailyNotification(this)
            }
        } else {
            NotificationScheduler.scheduleDailyNotification(this)
        }

        // 🎨 UI & Navigation
        setContent {
            PersistenceTheme {
                val navController = rememberNavController()
                var isPeriodicSyncEnabled by rememberSaveable { mutableStateOf(false) }

                Scaffold(
                    bottomBar = {
                        OurNavigationBar { route ->
                            navController.navigate(route::class.simpleName!!) {
                                launchSingleTop = true
                            }
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeRoute::class.simpleName!!,
                        modifier = Modifier.padding(padding)
                    ) {
                        // 🏠 Home Screen
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
                                },
                                onNavigateToHome = {
                                    navController.navigate(HomeRoute::class.simpleName!!) {
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToParticipants = {
                                    navController.navigate(ParticipantRoute::class.simpleName!!) {
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToSettings = {
                                    navController.navigate(SettingsRoute::class.simpleName!!) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // 👤 Teilnehmer Screen
                        composable(ParticipantRoute::class.simpleName!!) {
                            ParticipantScreen()
                        }

                        // ⚙️ Einstellungen
                        composable(SettingsRoute::class.simpleName!!) {
                            SettingsScreen()
                        }

                        // 🎯 Challenge Detail Screen
                        //composable<ChallengeRoute> {
                           // val data = it.toRoute<ChallengeRoute>()
                            //ChallengeScreen(
                               // player1 = data.player1,
                               // player2 = data.player2,
                               // won = data.won
                            //)
                        }
                    }
                }
            }
        }
    }
//}
