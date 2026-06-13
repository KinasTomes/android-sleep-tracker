package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.HabitReminderActionButtons
import com.uet.sleeptracker.ui.components.HabitReminderSettingsRows
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val BlueLightControlBackground = Color(0xFF111111)

@Composable
fun BlueLightControlScreen(
    modifier: Modifier = Modifier,
    brightLightEnabled: Boolean = true,
    avoidBrightLightEnabled: Boolean = true,
    showActionButtons: Boolean = true,
    onBrightLightEnabledChange: (Boolean) -> Unit = {},
    onAvoidBrightLightEnabledChange: (Boolean) -> Unit = {},
    onRemindMeClick: () -> Unit = {},
    onNotNowClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BlueLightControlBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BlueLightControlBackground)
        ) {
            ToolDetailTopBar(
                title = "Blue light control",
                onBackClick = onCloseClick,
                type = 1,
            )

            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(
                    start = 21.dp,
                    end = 21.dp,
                    top = 72.dp,
                    bottom = 34.dp,
                ),
            )

            HabitReminderSettingsRows(
                toggleTitle = "Remind me",
                toggleSubtitle = "to get bright light",
                reminderValue = "",
                reminderSubtitle = "Before melatonin window",
                checked = brightLightEnabled,
                onCheckedChange = onBrightLightEnabledChange,
                onReminderTimeClick = null,
            )

            Spacer(modifier = Modifier.height(36.dp))

            HabitReminderSettingsRows(
                toggleTitle = "Remind me",
                toggleSubtitle = "to avoid bright light",
                reminderValue = "4h",
                reminderSubtitle = "Before melatonin window",
                checked = avoidBrightLightEnabled,
                onCheckedChange = onAvoidBrightLightEnabledChange,
                onReminderTimeClick = null,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (showActionButtons) {
                HabitReminderActionButtons(
                    onRemindMeClick = onRemindMeClick,
                    onNotNowClick = onNotNowClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111111)
@Composable
private fun BlueLightControlScreenPreview() {
    SleeptrackerTheme {
        BlueLightControlScreen()
    }
}
