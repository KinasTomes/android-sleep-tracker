package com.uet.sleeptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.navigation.onboardingNavGraph
import com.uet.sleeptracker.ui.screens.home.HomeScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationMenuScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationsScreen
import com.uet.sleeptracker.ui.screens.settings.*
import com.uet.sleeptracker.ui.screens.tools.ToolsScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleeptrackerTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "home") {
                    onboardingNavGraph(navController)

                    // Main Screens
                    composable("home") {
                        HomeScreen(
                            onProfileClick = { navController.navigate("profile") },
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
