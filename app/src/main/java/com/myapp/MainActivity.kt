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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.myapp.Route.*
import com.myapp.model.room.entities.Challenges
import com.myapp.model.room.entities.Participant
import com.myapp.ui.composables.OurNavigationBar
import com.myapp.ui.screens.*
import com.myapp.ui.theme.PersistenceTheme
import com.myapp.util.NotificationScheduler
import com.myapp.viewmodel.ChallengesViewModel
import com.myapp.viewmodel.DataViewModel
import com.myapp.viewmodel.SettingsViewModel
import com.myapp.viewmodel.ParticipantViewModel

class MainActivity : ComponentActivity() {

    private val dataViewModel: DataViewModel by viewModels()
    private val challengesViewModel: ChallengesViewModel by viewModels()
    private val participantViewModel: ParticipantViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                NotificationScheduler.scheduleDailyNotification(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔔 Benachrichtigungsberechtigung (Android 13+)
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

                val allParticipants by participantViewModel.allParticipants.collectAsStateWithLifecycle(emptyList())
                val allChallenges by challengesViewModel.allChallenges.collectAsStateWithLifecycle(emptyList())

                // 🧠 Teilnehmer-Paare bilden (immer zwei zusammen)
                val participantPairs = remember(allParticipants) {
                    allParticipants.chunked(2)
                        .filter { it.size == 2 }
                        .map { Pair(it[0], it[1]) }
                }

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
                        // 🏠 Home Screen
                        composable(HomeRoute::class.simpleName!!) {
                            val allParticipants by participantViewModel.allParticipants.collectAsStateWithLifecycle(emptyList())

                            val participantPairs = allParticipants
                                .chunked(2)
                                .filter { it.size == 2 }
                                .map { Pair(it[0], it[1]) }

                            HomeScreen(
                                participantPairs = participantPairs,
                                onChallengeClick = {
                                    navController.navigate(ChallengesRoute::class.simpleName!!)
                                },
                                onNavigate = { route ->
                                    navController.navigate(route::class.simpleName!!) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }


                        // 👤 Teilnehmer
                        composable(ParticipantRoute::class.simpleName!!) {
                            ParticipantScreen(
                                onNavigate = { route ->
                                    navController.navigate(route::class.simpleName!!) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // ⚙️ Einstellungen
                        composable(SettingsRoute::class.simpleName!!) {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                onManualSync = {
                                    //liest die aktuellen Werte aus dem DataStore und lädt in Cloud hoch
                                    settingsViewModel.uploadSettingsToCloud()
                                },
                                isPeriodicSyncEnabled = isPeriodicSyncEnabled,
                                onPeriodicSyncToggled = { isPeriodicSyncEnabled = it },
                                onNavigate = { route ->
                                    navController.navigate(route::class.simpleName!!) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // 📋 Challenge-Liste
                        composable(ChallengesRoute::class.simpleName!!) {
                            ChallengeScreen(
                                onChallengeClick = { challenge: Challenges ->
                                    navController.navigate(
                                        ChallengeDetailRoute(
                                            challenge.id ?: -1,
                                            challenge.title,
                                            challenge.description
                                        )
                                    )
                                }
                            )
                        }

                        // 🎯 Challenge-Detail
                        composable<ChallengeDetailRoute> {
                            val data = it.toRoute<ChallengeDetailRoute>()
                            ChallengeDetailScreen(
                                challenge = data.toChallenge()
                            )
                        }
                    }
                }
            }
        }
    }
}
