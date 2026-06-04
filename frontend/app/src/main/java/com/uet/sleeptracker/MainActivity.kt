package com.uet.sleeptracker

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.navigation.onboardingNavGraph
import com.uet.sleeptracker.ui.screens.home.CaffeineCutoffScreen
import com.uet.sleeptracker.ui.screens.home.CaffeineLearnMoreScreen
import com.uet.sleeptracker.ui.screens.home.buildReminderChoiceItems
import com.uet.sleeptracker.ui.screens.home.buildDefaultReminderChoiceItems
import com.uet.sleeptracker.ui.screens.home.EveningRoutineScreen
import com.uet.sleeptracker.ui.screens.home.EveningRoutineSettingsScreen
import com.uet.sleeptracker.ui.screens.home.findInitialReminderChoiceIndex
import com.uet.sleeptracker.ui.screens.home.HomeScreen
import com.uet.sleeptracker.ui.screens.home.PeaksAndDipsScreen
import com.uet.sleeptracker.ui.screens.home.ReminderTimeScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationMenuScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationsScreen
import com.uet.sleeptracker.ui.screens.settings.*
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.screens.tools.ToolsScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleeptrackerTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SleepBackground)
                ) {
                    onboardingNavGraph(navController)

                    // Main Screens
                    composable("home") {
                        HomeScreen(
                            onProfileClick = { navController.navigate("profile") },
                            onPeaksAndDipsClick = { navController.navigate("peaks_and_dips") },
                            onCaffeineCutoffClick = { navController.navigate("caffeine_cutoff") },
                            onEveningRoutineClick = { navController.navigate("evening_routine") },
                            onBottomNavSelected = { tab ->
                                when (tab) {
                                    SleepBottomNavTab.Tools -> navController.navigate("tools")
                                    else -> {}
                                }
                            }
                        )
                    }
                    composable("tools") {
                        ToolsScreen(
                            onNotificationClick = { navController.navigate("notification_menu") },
                            onProfileClick = { navController.navigate("profile") },
                            onNavigateToHome = { navController.navigate("home") }
                        )
                    }
                    composable(
                        route = "evening_routine",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) {
                        EveningRoutineScreen(
                            onCloseClick = { navController.popBackStack() },
                            onSettingsClick = { navController.navigate("evening_routine_settings") }
                        )
                    }
                    composable(
                        route = "evening_routine_settings",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) {
                        EveningRoutineSettingsScreen(
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "caffeine_cutoff",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) {
                        CaffeineCutoffScreen(
                            onCloseClick = { navController.popBackStack() },
                            onReminderTimeClick = { navController.navigate("caffeine_cutoff_reminder_time") },
                            onLearnMoreClick = { navController.navigate("caffeine_cutoff_learn_more") }
                        )
                    }
                    composable(
                        route = "peaks_and_dips",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) {
                        PeaksAndDipsScreen(
                            onCloseClick = { navController.popBackStack() },
                            onReminderTimeClick = { sectionLabel, selectedTime, recommendedTime ->
                                navController.navigate(
                                    "reminder_time/${Uri.encode(sectionLabel)}/${Uri.encode(selectedTime)}/${Uri.encode(recommendedTime)}"
                                )
                            }
                        )
                    }
                    composable(
                        route = "reminder_time/{sectionLabel}/{selectedTime}/{recommendedTime}",
                        arguments = listOf(
                            navArgument("sectionLabel") { type = NavType.StringType },
                            navArgument("selectedTime") { type = NavType.StringType },
                            navArgument("recommendedTime") { type = NavType.StringType }
                        ),
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) { backStackEntry ->
                        val sectionLabel = backStackEntry.arguments?.getString("sectionLabel").orEmpty()
                        val selectedTime = backStackEntry.arguments?.getString("selectedTime").orEmpty()
                        val recommendedTime = backStackEntry.arguments?.getString("recommendedTime").orEmpty()
                        val reminderChoices = buildDefaultReminderChoiceItems(highlightedLabel = recommendedTime)

                        ReminderTimeScreen(
                            headerTitle = "Your Peaks And Dips",
                            screenTitle = "Reminder time",
                            sectionLabel = sectionLabel,
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = selectedTime
                            ),
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "caffeine_cutoff_learn_more",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) {
                        CaffeineLearnMoreScreen(
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "caffeine_cutoff_reminder_time",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(320)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(320)
                            )
                        }
                    ) {
                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("8h", "9h", "10h", "11h", "12h"),
                            highlightedLabel = "10h"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Caffeine Cutoff",
                            screenTitle = "Reminder Time",
                            sectionLabel = "BEFORE MELATONIN WINDOW",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = "10h"
                            ),
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            onBackClick = { navController.popBackStack() },
                            onEditProfileClick = { /* Handle edit profile */ },
                            onLogoutClick = { navController.navigate("step1") },
                            onMySleepNeedClick = { navController.navigate("my_sleep_need") },
                            onDataSourcesClick = { navController.navigate("data_sources") },
                            onNotificationsClick = { navController.navigate("notification_menu") },
                            onCalendarsClick = { navController.navigate("calendars") },
                            onTimeFormatClick = { navController.navigate("time_format") },
                            onLanguageClick = { navController.navigate("language") }
                        )
                    }

                    // Settings/Profile Sub-screens
                    composable("my_sleep_need") {
                        MySleepNeedScreen(onBackClick = { navController.popBackStack() })
                    }
                    composable("data_sources") {
                        DataSourcesScreen(onBackClick = { navController.popBackStack() })
                    }
                    composable("calendars") {
                        CalendarsScreen(onBackClick = { navController.popBackStack() })
                    }
                    composable("time_format") {
                        TimeFormatScreen(onBackClick = { navController.popBackStack() })
                    }
                    composable("language") {
                        LanguageScreen(onBackClick = { navController.popBackStack() })
                    }

                    // Notifications
                    composable("notification_menu") {
                        NotificationMenuScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable("notifications") {
                        NotificationsScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
