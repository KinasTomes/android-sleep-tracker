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
import com.uet.sleeptracker.ui.screens.progress.EditSleepScreen
import com.uet.sleeptracker.ui.screens.tools.CaffeineCutoffScreen
import com.uet.sleeptracker.ui.screens.tools.CaffeineLearnMoreScreen
import com.uet.sleeptracker.ui.screens.tools.EveningRoutineScreen
import com.uet.sleeptracker.ui.screens.tools.EveningRoutineSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.MorningRoutineScreen
import com.uet.sleeptracker.ui.screens.tools.MorningRoutineSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.PeaksAndDipsScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationMenuScreen
import com.uet.sleeptracker.ui.screens.notifications.NotificationsScreen
import com.uet.sleeptracker.ui.screens.settings.*
import com.uet.sleeptracker.ui.screens.tools.SmartAlarmSettingScreen
import com.uet.sleeptracker.ui.screens.tools.SmartAlarmScheduleScreen
import com.uet.sleeptracker.ui.screens.tools.AlarmItem
import com.uet.sleeptracker.ui.screens.tools.SmartAlarmViewModel
import com.uet.sleeptracker.ui.screens.tools.EnvironmentCheckScreen
import com.uet.sleeptracker.ui.screens.tools.BrainDumpScreen
import com.uet.sleeptracker.ui.screens.tools.BrainDumpSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.SleepQualityScreen
import com.uet.sleeptracker.ui.screens.tools.AvoidLateMealsScreen
import com.uet.sleeptracker.ui.screens.tools.AvoidLateMealsSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.AvoidLateAlcoholScreen
import com.uet.sleeptracker.ui.screens.tools.AvoidLateAlcoholSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.BlueLightGlassesScreen
import com.uet.sleeptracker.ui.screens.tools.BlueLightGlassesSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.BlueLightControlScreen
import com.uet.sleeptracker.ui.screens.tools.EarlierWorkoutsScreen
import com.uet.sleeptracker.ui.screens.tools.EarlierWorkoutsSettingsScreen
import com.uet.sleeptracker.ui.screens.tools.MelatoninSupplementsScreen
import com.uet.sleeptracker.ui.screens.tools.MelatoninWindowScreen
import com.uet.sleeptracker.ui.screens.tools.MelatoninWindowSettingsScreen
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.uet.sleeptracker.ui.screens.tools.ToolsViewModel
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.google.firebase.messaging.FirebaseMessaging
import com.uet.sleeptracker.notifications.FcmTokenRegistrar
import com.uet.sleeptracker.notifications.LumiFirebaseMessagingService


class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                registerFcmToken()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannels()
        requestNotificationPermissionAndRegisterToken()
        setContent {
            SleeptrackerTheme {
                val navController = rememberNavController()
                val smartAlarmViewModel: SmartAlarmViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                val toolsViewModel: ToolsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                
                NavHost(
                    navController = navController,
                    startDestination = "main_tabs",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SleepBackground)
                ) {
                    onboardingNavGraph(navController)

                    composable("main_tabs") {
                        val blueLightRemindMeClicked by toolsViewModel.blueLightGlassesRemindMeClicked.collectAsState(initial = false)
                        val avoidLateMealsRemindMeClicked by toolsViewModel.avoidLateMealsRemindMeClicked.collectAsState(initial = false)
                        val earlierWorkoutsRemindMeClicked by toolsViewModel.earlierWorkoutsRemindMeClicked.collectAsState(initial = false)
                        val avoidLateAlcoholRemindMeClicked by toolsViewModel.avoidLateAlcoholRemindMeClicked.collectAsState(initial = false)
                        val isAudioPlaying by toolsViewModel.isAudioPlaying.collectAsState(initial = false)

                        MainTabsScreen(
                            alarms = smartAlarmViewModel.alarms,
                            isAudioPlaying = isAudioPlaying,
                            onAudioClick = { toolsViewModel.toggleAudioPlayback() },
                            onAlarmEditClick = { alarm ->
                                val index = smartAlarmViewModel.alarms.indexOfFirst { it.id == alarm.id }
                                if (index != -1) {
                                    smartAlarmViewModel.editingIndex = index
                                    navController.navigate("smart_alarm_schedule")
                                }
                            },
                            onProfileClick = { navController.navigate("profile") },
                            onPeaksAndDipsClick = { navController.navigate("peaks_and_dips") },
                            onCaffeineCutoffClick = { navController.navigate("caffeine_cutoff") },
                            onEveningRoutineClick = { navController.navigate("evening_routine") },
                            onMorningRoutineClick = { navController.navigate("morning_routine") },
                            onBlueLightControlClick = { navController.navigate("blue_light_control") },
                            onSmartAlarmClick = { navController.navigate("smart_alarm_settings") },
                            onSleepQualityClick = { navController.navigate("sleep_quality") },
                            onPrepBedroomClick = { navController.navigate("environment_check") },
                            onBrainDumpClick = { navController.navigate("brain_dump") },
                            onMelatoninWindowClick = { navController.navigate("melatonin_window") },
                            onMelatoninSupplementsClick = { navController.navigate("melatonin_supplements") },
                            onBlueLightGlassesClick = {
                                if (blueLightRemindMeClicked) {
                                    navController.navigate("blue_light_glasses_settings")
                                } else {
                                    navController.navigate("blue_light_glasses")
                                }
                            },
                            onAvoidLateMealsClick = {
                                if (avoidLateMealsRemindMeClicked) {
                                    navController.navigate("avoid_late_meals_settings")
                                } else {
                                    navController.navigate("avoid_late_meals")
                                }
                            },
                            onEarlierWorkoutsClick = {
                                if (earlierWorkoutsRemindMeClicked) {
                                    navController.navigate("earlier_workouts_settings")
                                } else {
                                    navController.navigate("earlier_workouts")
                                }
                            },
                            onAvoidLateAlcoholClick = {
                                if (avoidLateAlcoholRemindMeClicked) {
                                    navController.navigate("avoid_late_alcohol_settings")
                                } else {
                                    navController.navigate("avoid_late_alcohol")
                                }
                            },
                            onSleepRecordClick = { title, startTime, endTime, duration ->
                                navController.navigate(
                                    "edit_sleep/${Uri.encode(title)}/${Uri.encode(startTime)}/${Uri.encode(endTime)}/${Uri.encode(duration)}"
                                )
                            },
                            onNavigateToNaps = { title, startTime, endTime, duration ->
                                navController.navigate(
                                    "edit_sleep_naps/${Uri.encode(title)}/${Uri.encode(startTime)}/${Uri.encode(endTime)}/${Uri.encode(duration)}"
                                )
                            },
                        )
                    }
                    composable(
                        route = "edit_sleep/{title}/{startTime}/{endTime}/{duration}",
                        arguments = listOf(
                            navArgument("title") { type = NavType.StringType },
                            navArgument("startTime") { type = NavType.StringType },
                            navArgument("endTime") { type = NavType.StringType },
                            navArgument("duration") { type = NavType.StringType },
                        ),
                    ) { backStackEntry ->
                        EditSleepScreen(
                            title = backStackEntry.arguments?.getString("title").orEmpty(),
                            startTime = backStackEntry.arguments?.getString("startTime").orEmpty(),
                            endTime = backStackEntry.arguments?.getString("endTime").orEmpty(),
                            duration = backStackEntry.arguments?.getString("duration").orEmpty(),
                            onCloseClick = { navController.popBackStack() },
                        )
                    }
                    composable(
                        route = "edit_sleep_naps/{title}/{startTime}/{endTime}/{duration}",
                        arguments = listOf(
                            navArgument("title") { type = NavType.StringType },
                            navArgument("startTime") { type = NavType.StringType },
                            navArgument("endTime") { type = NavType.StringType },
                            navArgument("duration") { type = NavType.StringType },
                        ),
                    ) { backStackEntry ->
                        EditSleepScreen(
                            title = backStackEntry.arguments?.getString("title").orEmpty(),
                            startTime = backStackEntry.arguments?.getString("startTime").orEmpty(),
                            endTime = backStackEntry.arguments?.getString("endTime").orEmpty(),
                            duration = backStackEntry.arguments?.getString("duration").orEmpty(),
                            openNapsOnEntry = true,
                            onCloseClick = { navController.popBackStack() },
                        )
                    }
                    composable(
                        route = "melatonin_window",
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
                        MelatoninWindowScreen(
                            onCloseClick = { navController.popBackStack() },
                            onSettingsClick = { navController.navigate("melatonin_window_settings") },
                        )
                    }
                    composable("melatonin_window_settings") {
                        val reminderTime by toolsViewModel.melatoninReminderTime.collectAsState(initial = "15 minutes")
                        val reminderEnabled by toolsViewModel.melatoninReminderEnabled.collectAsState(initial = true)

                        MelatoninWindowSettingsScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            onRemindersEnabledChange = { toolsViewModel.setMelatoninReminderEnabled(it) },
                            onReminderTimeClick = { navController.navigate("melatonin_window_reminder") },
                            onCloseClick = { navController.popBackStack() },
                        )
                    }
                    composable("melatonin_window_reminder") {
                        val reminderTime by toolsViewModel.melatoninReminderTime.collectAsState(initial = "15 minutes")
                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf(
                                "At melatonin window",
                                "5 minutes",
                                "15 minutes",
                                "20 minutes",
                                "30 minutes",
                            ),
                            highlightedLabel = "15 minutes",
                        )
                        ReminderTimeScreen(
                            headerTitle = "Melatonin Window",
                            screenTitle = "Reminder time",
                            sectionLabel = "BEFORE MELATONIN WINDOW",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = reminderTime,
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setMelatoninReminderTime(option.label)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() },
                        )
                    }
                    composable("melatonin_supplements") {
                        val remindMeClicked by toolsViewModel.melatoninSupplementsRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.melatoninSupplementsReminderTime.collectAsState(initial = "4 hours")
                        val savedEnabled by toolsViewModel.melatoninSupplementsReminderEnabled.collectAsState(initial = false)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedTime, savedEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempMelatoninSupplementsTime == null) {
                                toolsViewModel.startMelatoninSupplementsOnboarding(savedTime, savedEnabled)
                            }
                        }

                        val reminderTime = if (isShowingActionButtons) {
                            toolsViewModel.tempMelatoninSupplementsTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempMelatoninSupplementsEnabled ?: savedEnabled
                        } else {
                            savedEnabled
                        }

                        MelatoninSupplementsScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            showActionButtons = isShowingActionButtons,
                            onRemindersEnabledChange = { enabled ->
                                toolsViewModel.setMelatoninSupplementsEnabled(enabled, isShowingActionButtons)
                            },
                            onReminderTimeClick = {
                                navController.navigate("melatonin_supplements_reminder")
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitMelatoninSupplementsOnboarding()
                                navController.popBackStack()
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelMelatoninSupplementsOnboarding()
                                navController.popBackStack()
                            },
                            onCloseClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelMelatoninSupplementsOnboarding()
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("melatonin_supplements_reminder") {
                        val remindMeClicked by toolsViewModel.melatoninSupplementsRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.melatoninSupplementsReminderTime.collectAsState(initial = "4 hours")
                        val isShowingActionButtons = !remindMeClicked

                        val currentTime = if (isShowingActionButtons) {
                            toolsViewModel.tempMelatoninSupplementsTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("4 hours", "5 hours", "6 hours"),
                            highlightedLabel = "4 hours",
                        )
                        ReminderTimeScreen(
                            headerTitle = "Melatonin Supplements",
                            screenTitle = "Reminder time",
                            sectionLabel = "BEFORE MELATONIN WINDOW",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = currentTime
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setMelatoninSupplementsTime(option.label, isShowingActionButtons)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() },
                        )
                    }
                    composable("blue_light_glasses") {
                        BlueLightGlassesScreen(
                            onCloseClick = { navController.popBackStack() },
                            onGetStartedClick = { navController.navigate("blue_light_glasses_settings") },
                        )
                    }
                    composable("blue_light_glasses_settings") {
                        val remindMeClicked by toolsViewModel.blueLightGlassesRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.blueLightGlassesReminderTime.collectAsState(initial = "1h 30m")
                        val savedEnabled by toolsViewModel.blueLightGlassesReminderEnabled.collectAsState(initial = true)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedTime, savedEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempBlueLightGlassesTime == null) {
                                toolsViewModel.startBlueLightOnboarding(savedTime, savedEnabled)
                            }
                        }

                        val reminderTime = if (isShowingActionButtons) {
                            toolsViewModel.tempBlueLightGlassesTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempBlueLightGlassesEnabled ?: savedEnabled
                        } else {
                            savedEnabled
                        }

                        BlueLightGlassesSettingsScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            showActionButtons = isShowingActionButtons,
                            onRemindersEnabledChange = { enabled ->
                                toolsViewModel.setBlueLightGlassesEnabled(enabled, isShowingActionButtons)
                            },
                            onReminderTimeClick = {
                                navController.navigate("blue_light_glasses_reminder_time")
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitBlueLightOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelBlueLightOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onCloseClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelBlueLightOnboarding()
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("blue_light_glasses_reminder_time") {
                        val remindMeClicked by toolsViewModel.blueLightGlassesRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.blueLightGlassesReminderTime.collectAsState(initial = "1h 30m")
                        val isShowingActionButtons = !remindMeClicked

                        val currentTime = if (isShowingActionButtons) {
                            toolsViewModel.tempBlueLightGlassesTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("30m", "1h", "1h 30m", "2h"),
                            highlightedLabel = "1h 30m"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Wear Orange Glasses",
                            screenTitle = "Reminder Time",
                            sectionLabel = "BEFORE BEDTIME",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = currentTime
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setBlueLightGlassesTime(option.label, isShowingActionButtons)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable("avoid_late_meals") {
                        AvoidLateMealsScreen(
                            onCloseClick = { navController.popBackStack() },
                            onGetStartedClick = { navController.navigate("avoid_late_meals_settings") },
                        )
                    }
                    composable("avoid_late_meals_settings") {
                        val remindMeClicked by toolsViewModel.avoidLateMealsRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.avoidLateMealsReminderTime.collectAsState(initial = "3h")
                        val savedEnabled by toolsViewModel.avoidLateMealsReminderEnabled.collectAsState(initial = true)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedTime, savedEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempAvoidLateMealsTime == null) {
                                toolsViewModel.startAvoidLateMealsOnboarding(savedTime, savedEnabled)
                            }
                        }

                        val reminderTime = if (isShowingActionButtons) {
                            toolsViewModel.tempAvoidLateMealsTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempAvoidLateMealsEnabled ?: savedEnabled
                        } else {
                            savedEnabled
                        }

                        AvoidLateMealsSettingsScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            showActionButtons = isShowingActionButtons,
                            onRemindersEnabledChange = { enabled ->
                                toolsViewModel.setAvoidLateMealsEnabled(enabled, isShowingActionButtons)
                            },
                            onReminderTimeClick = {
                                navController.navigate("avoid_late_meals_reminder_time")
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitAvoidLateMealsOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelAvoidLateMealsOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onCloseClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelAvoidLateMealsOnboarding()
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("avoid_late_meals_reminder_time") {
                        val remindMeClicked by toolsViewModel.avoidLateMealsRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.avoidLateMealsReminderTime.collectAsState(initial = "3h")
                        val isShowingActionButtons = !remindMeClicked

                        val currentTime = if (isShowingActionButtons) {
                            toolsViewModel.tempAvoidLateMealsTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("2h", "3h", "4h"),
                            highlightedLabel = "3h"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Avoid Late Meals",
                            screenTitle = "Reminder Time",
                            sectionLabel = "BEFORE BEDTIME",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = currentTime
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setAvoidLateMealsTime(option.label, isShowingActionButtons)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable("avoid_late_alcohol") {
                        AvoidLateAlcoholScreen(
                            onCloseClick = { navController.popBackStack() },
                            onGetStartedClick = { navController.navigate("avoid_late_alcohol_settings") },
                        )
                    }
                    composable("avoid_late_alcohol_settings") {
                        val remindMeClicked by toolsViewModel.avoidLateAlcoholRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.avoidLateAlcoholReminderTime.collectAsState(initial = "4h")
                        val savedEnabled by toolsViewModel.avoidLateAlcoholReminderEnabled.collectAsState(initial = true)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedTime, savedEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempAvoidLateAlcoholTime == null) {
                                toolsViewModel.startAvoidLateAlcoholOnboarding(savedTime, savedEnabled)
                            }
                        }

                        val reminderTime = if (isShowingActionButtons) {
                            toolsViewModel.tempAvoidLateAlcoholTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempAvoidLateAlcoholEnabled ?: savedEnabled
                        } else {
                            savedEnabled
                        }

                        AvoidLateAlcoholSettingsScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            showActionButtons = isShowingActionButtons,
                            onRemindersEnabledChange = { enabled ->
                                toolsViewModel.setAvoidLateAlcoholEnabled(enabled, isShowingActionButtons)
                            },
                            onReminderTimeClick = {
                                navController.navigate("avoid_late_alcohol_reminder_time")
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitAvoidLateAlcoholOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelAvoidLateAlcoholOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onCloseClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelAvoidLateAlcoholOnboarding()
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("avoid_late_alcohol_reminder_time") {
                        val remindMeClicked by toolsViewModel.avoidLateAlcoholRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.avoidLateAlcoholReminderTime.collectAsState(initial = "4h")
                        val isShowingActionButtons = !remindMeClicked

                        val currentTime = if (isShowingActionButtons) {
                            toolsViewModel.tempAvoidLateAlcoholTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("3h", "4h", "5h"),
                            highlightedLabel = "4h"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Avoid Late Alcohol",
                            screenTitle = "Reminder Time",
                            sectionLabel = "BEFORE BEDTIME",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = currentTime
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setAvoidLateAlcoholTime(option.label, isShowingActionButtons)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable("earlier_workouts") {
                        EarlierWorkoutsScreen(
                            onCloseClick = { navController.popBackStack() },
                            onGetStartedClick = { navController.navigate("earlier_workouts_settings") },
                        )
                    }
                    composable("earlier_workouts_settings") {
                        val remindMeClicked by toolsViewModel.earlierWorkoutsRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.earlierWorkoutsReminderTime.collectAsState(initial = "1h")
                        val savedEnabled by toolsViewModel.earlierWorkoutsReminderEnabled.collectAsState(initial = true)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedTime, savedEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempEarlierWorkoutsTime == null) {
                                toolsViewModel.startEarlierWorkoutsOnboarding(savedTime, savedEnabled)
                            }
                        }

                        val reminderTime = if (isShowingActionButtons) {
                            toolsViewModel.tempEarlierWorkoutsTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempEarlierWorkoutsEnabled ?: savedEnabled
                        } else {
                            savedEnabled
                        }

                        EarlierWorkoutsSettingsScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            showActionButtons = isShowingActionButtons,
                            onRemindersEnabledChange = { enabled ->
                                toolsViewModel.setEarlierWorkoutsEnabled(enabled, isShowingActionButtons)
                            },
                            onReminderTimeClick = {
                                navController.navigate("earlier_workouts_reminder_time")
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitEarlierWorkoutsOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelEarlierWorkoutsOnboarding()
                                navController.popBackStack("main_tabs", false)
                            },
                            onCloseClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelEarlierWorkoutsOnboarding()
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("earlier_workouts_reminder_time") {
                        val remindMeClicked by toolsViewModel.earlierWorkoutsRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.earlierWorkoutsReminderTime.collectAsState(initial = "1h")
                        val isShowingActionButtons = !remindMeClicked

                        val currentTime = if (isShowingActionButtons) {
                            toolsViewModel.tempEarlierWorkoutsTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("1h", "2h", "3h"),
                            highlightedLabel = "1h"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Earlier Workouts",
                            screenTitle = "Reminder Time",
                            sectionLabel = "BEFORE BEDTIME",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = currentTime
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setEarlierWorkoutsTime(option.label, isShowingActionButtons)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "brain_dump",
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
                        val todos by toolsViewModel.brainDumpTodos.collectAsState(initial = emptyList())
                        BrainDumpScreen(
                            todos = todos,
                            onTodoAdd = { text ->
                                val current = todos.toMutableList()
                                val nextId = (current.maxOfOrNull { it.id } ?: -1) + 1
                                current.add(com.uet.sleeptracker.data.BrainDumpTodoItem(id = nextId, text = text, completed = false))
                                toolsViewModel.setBrainDumpTodos(current)
                            },
                            onTodoCheckedChange = { index, checked ->
                                val current = todos.toMutableList()
                                if (index in current.indices) {
                                    current[index] = current[index].copy(completed = checked)
                                    toolsViewModel.setBrainDumpTodos(current)
                                }
                            },
                            onTodoDelete = { id ->
                                val current = todos.toMutableList()
                                current.removeAll { it.id == id }
                                toolsViewModel.setBrainDumpTodos(current)
                            },
                            onCloseClick = { navController.popBackStack() },
                            onSetReminderClick = { navController.navigate("brain_dump_settings") }
                        )
                    }
                    composable(
                        route = "brain_dump_settings",
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
                        val morningEnabled by toolsViewModel.brainDumpMorningSummaryEnabled.collectAsState(initial = true)
                        val eveningEnabled by toolsViewModel.brainDumpEveningReminderEnabled.collectAsState(initial = true)
                        val reminderTimeText by toolsViewModel.brainDumpEveningReminderTime.collectAsState(initial = "15 minutes")

                        BrainDumpSettingsScreen(
                            morningSummaryEnabled = morningEnabled,
                            eveningReminderEnabled = eveningEnabled,
                            reminderTimeText = reminderTimeText,
                            onMorningSummaryEnabledChange = { toolsViewModel.setBrainDumpMorningSummaryEnabled(it) },
                            onEveningReminderEnabledChange = { toolsViewModel.setBrainDumpEveningReminderEnabled(it) },
                            onCloseClick = { navController.popBackStack() },
                            onReminderTimeClick = { navController.navigate("brain_dump_reminder_time") }
                        )
                    }
                    composable(
                        route = "brain_dump_reminder_time",
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
                        val reminderTimeText by toolsViewModel.brainDumpEveningReminderTime.collectAsState(initial = "15 minutes")
                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("15 minutes", "30 minutes", "1 hour", "2 hours"),
                            highlightedLabel = "15 minutes"
                        )

                        ReminderTimeScreen(
                            headerTitle = "Brain Dump",
                            screenTitle = "Reminder time",
                            sectionLabel = "AFTER WAKE",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = reminderTimeText
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setBrainDumpEveningReminderTime(option.label)
                                navController.popBackStack()
                            },
                            onCloseClick = { navController.popBackStack() }
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
                        val selectedMoodIndex by toolsViewModel.sleepQualityTodayMood.collectAsState()
                        val reminderEnabled by toolsViewModel.sleepQualityReminderEnabled.collectAsState()
                        val reminderTimeText by toolsViewModel.sleepQualityReminderTime.collectAsState()

                        SleepQualityScreen(
                            selectedMoodIndex = selectedMoodIndex,
                            reminderEnabled = reminderEnabled,
                            reminderTimeText = reminderTimeText ?: "1h 30m",
                            onMoodChange = { toolsViewModel.setSleepQualityTodayMood(it) },
                            onReminderEnabledChange = { toolsViewModel.setSleepQualityReminderEnabled(it) },
                            onReminderTimeClick = { navController.navigate("sleep_quality_reminder_time") },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "sleep_quality_reminder_time",
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
                        val reminderTimeText by toolsViewModel.sleepQualityReminderTime.collectAsState()
                        val reminderChoices = buildReminderChoiceItems(
                            labels = listOf("30 minutes", "1 hour", "1h 30m", "2 hours"),
                            highlightedLabel = "1h 30m"
                        )
                        ReminderTimeScreen(
                            headerTitle = "Sleep Quality",
                            screenTitle = "Reminder time",
                            sectionLabel = "AFTER WAKETIME",
                            options = reminderChoices,
                            initialSelectedIndex = findInitialReminderChoiceIndex(
                                options = reminderChoices,
                                selectedLabel = reminderTimeText ?: "1h 30m"
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setSleepQualityReminderTime(option.label)
                                navController.popBackStack()
                            },
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
                            alarms = smartAlarmViewModel.alarms,
                            onAlarmEnabledChange = { index, enabled ->
                                smartAlarmViewModel.updateAlarmEnabled(index, enabled)
                            },
                            onAlarmActiveDaysChange = { index, activeDays ->
                                smartAlarmViewModel.updateAlarmActiveDays(index, activeDays)
                            },
                            onBackClick = { navController.popBackStack() },
                            onSettingsClick = { index ->
                                smartAlarmViewModel.editingIndex = index
                                navController.navigate("smart_alarm_schedule")
                            },
                            onAddAlarmClick = {
                                smartAlarmViewModel.addAlarm("07:00")
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
                        val editIdx = smartAlarmViewModel.editingIndex
                        val alarm = editIdx?.let { smartAlarmViewModel.alarms.getOrNull(it) }
                        if (alarm != null && editIdx != null) {
                            SmartAlarmScheduleScreen(
                                alarm = alarm,
                                onEnabledChange = { smartAlarmViewModel.updateAlarmEnabled(editIdx, it) },
                                onActiveDaysChange = { smartAlarmViewModel.updateAlarmActiveDays(editIdx, it) },
                                onTimeChange = { smartAlarmViewModel.updateAlarmTime(editIdx, it) },
                                onBackClick = { navController.popBackStack() },
                                onDeleteClick = {
                                    smartAlarmViewModel.deleteAlarm(editIdx)
                                    navController.popBackStack()
                                },
                                canDelete = smartAlarmViewModel.alarms.size > 1
                            )
                        }
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
                        val remindMeClicked by toolsViewModel.environmentRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.environmentReminderTime.collectAsState(initial = "30 minutes")
                        val savedEnabled by toolsViewModel.environmentReminderEnabled.collectAsState(initial = true)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedTime, savedEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempEnvironmentReminderTime == null) {
                                toolsViewModel.startEnvironmentCheckOnboarding(savedTime, savedEnabled)
                            }
                        }

                        val reminderTime = if (isShowingActionButtons) {
                            toolsViewModel.tempEnvironmentReminderTime ?: savedTime
                        } else {
                            savedTime
                        }

                        val reminderEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempEnvironmentReminderEnabled ?: savedEnabled
                        } else {
                            savedEnabled
                        }

                        EnvironmentCheckScreen(
                            reminderTime = reminderTime,
                            initialRemindersEnabled = reminderEnabled,
                            showActionButtons = isShowingActionButtons,
                            onRemindersEnabledChange = { enabled ->
                                toolsViewModel.setEnvironmentReminderEnabled(enabled, isShowingActionButtons)
                            },
                            onReminderTimeClick = {
                                navController.navigate("environment_check_reminder_time")
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitEnvironmentCheckOnboarding()
                                navController.popBackStack()
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelEnvironmentCheckOnboarding()
                                navController.popBackStack()
                            },
                            onBackClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelEnvironmentCheckOnboarding()
                                }
                                navController.popBackStack()
                            }
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
                        val remindMeClicked by toolsViewModel.environmentRemindMeClicked.collectAsState(initial = false)
                        val savedTime by toolsViewModel.environmentReminderTime.collectAsState(initial = "30 minutes")
                        val isShowingActionButtons = !remindMeClicked

                        val currentTime = if (isShowingActionButtons) {
                            toolsViewModel.tempEnvironmentReminderTime ?: savedTime
                        } else {
                            savedTime
                        }

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
                                selectedLabel = currentTime
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setEnvironmentReminderTime(option.label, isShowingActionButtons)
                                navController.popBackStack()
                            },
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
                        val reminderEnabled by toolsViewModel.eveningRoutineReminderEnabled.collectAsState(initial = true)
                        EveningRoutineSettingsScreen(
                            initialEnabled = reminderEnabled,
                            onCheckedChange = { toolsViewModel.setEveningRoutineReminderEnabled(it) },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                    composable("morning_routine") {
                        MorningRoutineScreen(
                            onCloseClick = { navController.popBackStack() },
                            onSettingsClick = { navController.navigate("morning_routine_settings") },
                        )
                    }
                    composable("morning_routine_settings") {
                        val reminderEnabled by toolsViewModel.morningRoutineReminderEnabled.collectAsState(initial = true)
                        MorningRoutineSettingsScreen(
                            initialEnabled = reminderEnabled,
                            onCheckedChange = { toolsViewModel.setMorningRoutineReminderEnabled(it) },
                            onCloseClick = { navController.popBackStack() },
                        )
                    }
                    composable("blue_light_control") {
                        val remindMeClicked by toolsViewModel.blueLightControlRemindMeClicked.collectAsState(initial = false)
                        val savedBrightEnabled by toolsViewModel.blueLightControlBrightLightEnabled.collectAsState(initial = true)
                        val savedAvoidEnabled by toolsViewModel.blueLightControlAvoidBrightLightEnabled.collectAsState(initial = true)

                        val isShowingActionButtons = !remindMeClicked

                        androidx.compose.runtime.LaunchedEffect(isShowingActionButtons, savedBrightEnabled, savedAvoidEnabled) {
                            if (isShowingActionButtons && toolsViewModel.tempBlueLightControlBrightLightEnabled == null) {
                                toolsViewModel.startBlueLightControlOnboarding(savedBrightEnabled, savedAvoidEnabled)
                            }
                        }

                        val brightEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempBlueLightControlBrightLightEnabled ?: savedBrightEnabled
                        } else {
                            savedBrightEnabled
                        }

                        val avoidEnabled = if (isShowingActionButtons) {
                            toolsViewModel.tempBlueLightControlAvoidBrightLightEnabled ?: savedAvoidEnabled
                        } else {
                            savedAvoidEnabled
                        }

                        BlueLightControlScreen(
                            brightLightEnabled = brightEnabled,
                            avoidBrightLightEnabled = avoidEnabled,
                            showActionButtons = isShowingActionButtons,
                            onBrightLightEnabledChange = { enabled ->
                                toolsViewModel.setBlueLightControlBrightEnabled(enabled, isShowingActionButtons)
                            },
                            onAvoidBrightLightEnabledChange = { enabled ->
                                toolsViewModel.setBlueLightControlAvoidEnabled(enabled, isShowingActionButtons)
                            },
                            onRemindMeClick = {
                                toolsViewModel.commitBlueLightControlOnboarding()
                                navController.popBackStack()
                            },
                            onNotNowClick = {
                                toolsViewModel.cancelBlueLightControlOnboarding()
                                navController.popBackStack()
                            },
                            onCloseClick = {
                                if (isShowingActionButtons) {
                                    toolsViewModel.cancelBlueLightControlOnboarding()
                                }
                                navController.popBackStack()
                            }
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
                        val reminderTimeText by toolsViewModel.caffeineReminderTime.collectAsState(initial = "10h")
                        val reminderEnabled by toolsViewModel.caffeineReminderEnabled.collectAsState(initial = true)

                        CaffeineCutoffScreen(
                            reminderTimeText = reminderTimeText,
                            initialRemindersEnabled = reminderEnabled,
                            onRemindersEnabledChange = { toolsViewModel.setCaffeineReminderEnabled(it) },
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
                        val morningEnabled by toolsViewModel.peaksAndDipsMorningForecastEnabled.collectAsState(initial = true)
                        val morningTime by toolsViewModel.peaksAndDipsMorningForecastTime.collectAsState(initial = "1 hour")
                        val peakEnabled by toolsViewModel.peaksAndDipsPeakRemindersEnabled.collectAsState(initial = true)
                        val peakTime by toolsViewModel.peaksAndDipsPeakRemindersTime.collectAsState(initial = "15 minutes")
                        val dipEnabled by toolsViewModel.peaksAndDipsDipReminderEnabled.collectAsState(initial = true)
                        val dipTime by toolsViewModel.peaksAndDipsDipReminderTime.collectAsState(initial = "15 minutes")

                        val morningTimeShort = morningTime.toShortReminderTimeLabel()
                        val peakTimeShort = peakTime.toShortReminderTimeLabel()
                        val dipTimeShort = dipTime.toShortReminderTimeLabel()

                        PeaksAndDipsScreen(
                            morningForecastEnabled = morningEnabled,
                            morningForecastTime = morningTimeShort,
                            peakRemindersEnabled = peakEnabled,
                            peakRemindersTime = peakTimeShort,
                            dipReminderEnabled = dipEnabled,
                            dipReminderTime = dipTimeShort,
                            onMorningForecastEnabledChange = { toolsViewModel.setPeaksAndDipsMorningForecastEnabled(it) },
                            onPeakRemindersEnabledChange = { toolsViewModel.setPeaksAndDipsPeakRemindersEnabled(it) },
                            onDipReminderEnabledChange = { toolsViewModel.setPeaksAndDipsDipReminderEnabled(it) },
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
                            onSelectedOption = { option ->
                                when (sectionLabel) {
                                    "AFTER WAKE" -> toolsViewModel.setPeaksAndDipsMorningForecastTime(option.label)
                                    "BEFORE PEAKS" -> toolsViewModel.setPeaksAndDipsPeakRemindersTime(option.label)
                                    "BEFORE DIP" -> toolsViewModel.setPeaksAndDipsDipReminderTime(option.label)
                                }
                                navController.popBackStack()
                            },
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
                        val reminderTimeText by toolsViewModel.caffeineReminderTime.collectAsState(initial = "10h")
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
                                selectedLabel = reminderTimeText
                            ),
                            type = 1,
                            onSelectedOption = { option ->
                                toolsViewModel.setCaffeineReminderTime(option.label)
                                navController.popBackStack()
                            },
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

    private fun requestNotificationPermissionAndRegisterToken() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                registerFcmToken()
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            registerFcmToken()
        }
    }

    private fun registerFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            FcmTokenRegistrar.register(token)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = getSystemService(NotificationManager::class.java)

        val habitChannel = NotificationChannel(
            LumiFirebaseMessagingService.CHANNEL_HABIT_NUDGES,
            "Habit reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val alarmAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val alarmChannel = NotificationChannel(
            LumiFirebaseMessagingService.CHANNEL_ALARMS,
            "Smart alarms",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            setSound(alarmSound, alarmAttributes)
        }

        manager.createNotificationChannel(habitChannel)
        manager.createNotificationChannel(alarmChannel)
    }
}

private fun String.toShortReminderTimeLabel(): String {
    return when (trim().lowercase()) {
        "1 hour" -> "1h"
        "15 minutes" -> "15m"
        "30 minutes" -> "30m"
        "2 hours" -> "2h"
        else -> this
    }
}
