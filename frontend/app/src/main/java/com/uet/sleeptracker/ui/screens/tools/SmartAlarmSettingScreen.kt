package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.AlarmScheduleCard
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val SmartAlarmScreenColor = Color(0xFF010101)
private val SmartAlarmAccentColor = Color(0xFF8962EC)

@Composable
fun SmartAlarmSettingScreen(
    alarms: List<AlarmItem>,
    onAlarmEnabledChange: (Int, Boolean) -> Unit,
    onAlarmActiveDaysChange: (Int, List<Boolean>) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSettingsClick: (Int) -> Unit = {},
    onAddAlarmClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SmartAlarmScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SmartAlarmScreenColor)
        ) {
            ToolDetailTopBar(
                title = "Smart Alarm",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 33.dp)
            ) {
                alarms.forEachIndexed { index, alarm ->
                    AlarmScheduleCard(
                        time = alarm.time,
                        isAlarmEnabled = alarm.isEnabled,
                        activeDays = alarm.activeDays,
                        onEnabledChange = { onAlarmEnabledChange(index, it) },
                        onActiveDaysChange = { onAlarmActiveDaysChange(index, it) },
                        onSettingsClick = { onSettingsClick(index) },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    )
                }

                AddAlarmRow(
                    onClick = onAddAlarmClick,
                    modifier = Modifier.padding(start = 16.dp, top = 11.dp)
                )
            }
        }
    }
}

@Composable
private fun AddAlarmRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.plus_button),
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )

        Text(
            text = "Alarm",
            color = SmartAlarmAccentColor,
            fontSize = 16.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101, widthDp = 476, heightDp = 956)
@Composable
private fun SmartAlarmSettingScreenPreview() {
    SleeptrackerTheme {
        SmartAlarmSettingScreen(
            alarms = listOf(AlarmItem("1", "08:00", true, listOf(true, true, true, true, true, true, true))),
            onAlarmEnabledChange = { _, _ -> },
            onAlarmActiveDaysChange = { _, _ -> }
        )
    }
}
