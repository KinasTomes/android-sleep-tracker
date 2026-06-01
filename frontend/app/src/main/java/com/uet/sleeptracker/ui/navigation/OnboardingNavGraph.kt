package com.uet.sleeptracker.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.uet.sleeptracker.ui.screens.onboarding.*
import com.uet.sleeptracker.ui.screens.onboarding.info.*

fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
    // Survey Steps
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
            onLearnMore = { }
        )
    }
    composable("info_sleep_need") {
        OnboardingSleepNeedIntroScreen(
            onNavigateNext = { navController.navigate("info_sleep_schedule") }
        )
    }
    composable("info_sleep_schedule") {
        OnboardingSleepScheduleScreen(
            onNavigateNext = { _, _, _, _ -> navController.navigate("info_weekend_sleep_schedule") },
            onBack = { navController.popBackStack() }
        )
    }
    composable("info_weekend_sleep_schedule") {
        OnboardingWeekendSleepScheduleScreen(
            onNavigateNext = { _, _, _, _ -> navController.navigate("info_sleep_need_result") },
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
            onWhyClick = { }
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
            onWhyClick = { }
        )
    }
    composable("info_sleep_need_result") {
        OnboardingSleepNeedResultScreen(
            onLooksRight = { navController.navigate("info_sleep_debt") },
            onHowCalculated = { },
            onAdjustSleepNeed = { navController.navigate("info_edit_sleep_need") }
        )
    }
    composable("info_sleep_debt") {
        OnboardingSleepDebtInfoScreen(
            onPrimaryAction = { navController.navigate("info_data_permission") },
            onAdjustSleepNeed = { navController.navigate("info_edit_sleep_need") }
        )
    }
    composable("info_edit_sleep_need") {
        OnboardingEditSleepNeedScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }
}
