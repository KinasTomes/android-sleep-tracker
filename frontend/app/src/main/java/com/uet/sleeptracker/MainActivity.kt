package com.uet.sleeptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uet.sleeptracker.ui.screens.HomeScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationMenuScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationsScreen
import com.uet.sleeptracker.ui.screens.onboarding.*
import com.uet.sleeptracker.ui.screens.onboarding.info.*
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
                
                NavHost(navController = navController, startDestination = "step1") {
                    // Onboarding Steps
                    composable("step1") {
                        Step1GenderScreen(
                            onNavigateNext = { _ -> navController.navigate("step2") },
                            onSkip = { navController.navigate("info_discovery") }
                        )
                    }
                    composable("step2") {
                        Step2AgeScreen(
                            onNavigateNext = { _ -> navController.navigate("step3") },
                            onNavigateBack = { navController.popBackStack() },
                            onSkip = { navController.navigate("info_discovery") }
                        )
                    }
                    composable("step3") {
                        Step3GoalScreen(
                            onNavigateNext = { _ -> navController.navigate("step4") },
                            onSkip = { navController.navigate("info_discovery") }
                        )
                    }
                    composable("step4") {
                        Step4LCScreen(
                            onNavigateNext = { _ -> navController.navigate("step5") },
                            onSkip = { navController.navigate("info_discovery") }
                        )
                    }
                    composable("step5") {
                        Step5SleepPracticeScreen(
                            onNavigateNext = { _ -> navController.navigate("step6") },
                            onSkip = { navController.navigate("info_discovery") }
                        )
                    }
                    composable("step6") {
                        Step6EnergyBoostScreen(
                            onNavigateNext = { _ -> navController.navigate("info_discovery") },
                            onSkip = { navController.navigate("info_discovery") }
                        )
                    }

                    // Info Screens
                    composable("info_discovery") {
                        OnboardingDiscoveryIntroScreen(
                            onNavigateNext = { navController.navigate("info_sleep_need") },
                            onLearnMore = { /* Handle learn more */ }
                        )
                    }
                    composable("info_sleep_need") {
                        OnboardingSleepNeedIntroScreen(
                            onNavigateNext = { navController.navigate("info_sleep_schedule") }
                        )
                    }
                    composable("info_sleep_schedule") {
                        OnboardingSleepScheduleScreen(
                            onNavigateNext = { _, _, _, _ -> navController.navigate("info_data_permission") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("info_data_permission") {
                        DataPermissionInfoScreen(
                            onConfirm = { navController.navigate("info_health_connect") }
                        )
                    }
                    composable("info_health_connect") {
                        HealthConnectPermissionScreen(
                            onTurnOnClick = { navController.navigate("info_notification") },
                            onWhyClick = { /* Handle why click */ }
                        )
                    }
                    composable("info_notification") {
                        NotificationPermissionScreen(
                            onNext = { navController.navigate("info_physical_activity") }
                        )
                    }
                    composable("info_physical_activity") {
                        PhysicalActivityPermissionScreen(
                            onNext = { navController.navigate("home") },
                            onWhyClick = { /* Handle why click */ }
                        )
                    }

                    // Main Screens
                    composable("home") {
                        HomeScreen(
                            onNavigateToNotifications = { navController.navigate("notification_menu") },
                            onNavigateToProfile = { navController.navigate("profile") },
                            onNavigateToTools = { navController.navigate("tools") }
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
