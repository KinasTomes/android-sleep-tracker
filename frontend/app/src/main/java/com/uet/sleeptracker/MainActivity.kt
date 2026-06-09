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
import com.uet.sleeptracker.ui.components.buildReminderChoiceItems
import com.uet.sleeptracker.ui.components.buildDefaultReminderChoiceItems
import com.uet.sleeptracker.ui.components.findInitialReminderChoiceIndex
import com.uet.sleeptracker.ui.components.ReminderTimeScreen
import com.uet.sleeptracker.ui.navigation.onboardingNavGraph
import com.uet.sleeptracker.ui.screens.MainTabsScreen
import com.uet.sleeptracker.ui.screens.home.CaffeineCutoffScreen
import com.uet.sleeptracker.ui.screens.home.CaffeineLearnMoreScreen
import com.uet.sleeptracker.ui.screens.home.EveningRoutineScreen
import com.uet.sleeptracker.ui.screens.home.EveningRoutineSettingsScreen
import com.uet.sleeptracker.ui.screens.home.PeaksAndDipsScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationMenuScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationsScreen
import com.uet.sleeptracker.ui.screens.settings.*
import com.uet.sleeptracker.ui.screens.tools.SmartAlarmSettingScreen
import com.uet.sleeptracker.ui.screens.tools.SmartAlarmScheduleScreen
import com.uet.sleeptracker.ui.screens.tools.EnvironmentCheckScreen
import com.uet.sleeptracker.ui.screens.tools.SleepQualityScreen
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleeptrackerTheme {
                val navController = rememberNavController()
                val alarmList = remember { mutableStateListOf("08:00", "09:30") }
                var editingIndex by remember { mutableStateOf<Int?>(null) }
                
                NavHost(
                    navController = navController,
                    startDestination = "main_tabs",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SleepBackground)
                ) {
                    onboardingNavGraph(navController)

                    composable("main_tabs") {
                        MainTabsScreen(
                            onProfileClick = { navController.navigate("profile") },
                            onPeaksAndDipsClick = { navController.navigate("peaks_and_dips") },
                            onCaffeineCutoffClick = { navController.navigate("caffeine_cutoff") },
                            onEveningRoutineClick = { navController.navigate("evening_routine") },
                            onSmartAlarmClick = { navController.navigate("smart_alarm_settings") },
                            onSleepQualityClick = { navController.navigate("sleep_quality") },
                            onPrepBedroomClick = { navController.navigate("environment_check") }
                        )
                    }
                    composable(
                        route = "sleep_quality",
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
                        SleepQualityScreen(
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "smart_alarm_settings",
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
                        SmartAlarmSettingScreen(
                            alarms = alarmList,
                            onBackClick = { navController.popBackStack() },
                            onSettingsClick = { index ->
                                editingIndex = index
                                navController.navigate("smart_alarm_schedule")
                            },
                            onAddAlarmClick = {
                                alarmList.add("07:00")
                                editingIndex = alarmList.lastIndex
                                navController.navigate("smart_alarm_schedule")
                            }
                        )
                    }
                    composable(
                        route = "smart_alarm_schedule",
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
                        SmartAlarmScheduleScreen(
                            onBackClick = { navController.popBackStack() },
                            onDeleteClick = {
                                editingIndex?.let { index ->
                                    if (alarmList.size > 1 && index in alarmList.indices) {
                                        alarmList.removeAt(index)
                                    }
                                }
                                navController.popBackStack()
                            },
                            canDelete = alarmList.size > 1
                        )
                    }
                    composable(
                        route = "environment_check",
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
                        EnvironmentCheckScreen(
                            onRemindMeClick = { navController.popBackStack() },
                            onNotNowClick = { navController.popBackStack() },
                            onReminderTimeClick = { navController.navigate("environment_check_reminder_time") }
                        )
                    }
                    composable(
                        route = "environment_check_reminder_time",
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
                            labels = listOf("15 minutes", "30 minutes", "1 hour"),
                            highlightedLabel = "30 minutes"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Environment Check",
                            screenTitle = "Reminder Time",
                            sectionLabel = "BEFORE MELATONIN WINDOW",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = "30 minutes"
                            ),
                            onCloseClick = { navController.popBackStack() }
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
                            type = 1,
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
                            type = 1,
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
