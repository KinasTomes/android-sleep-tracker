package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val PeaksScreenColor = Color(0xFF121212)
private val PeaksMutedTextColor = Color(0xFFBABABA)
private val PeaksItemStrokeColor = Color(0xFF1A1A1D)

@Composable
fun PeaksAndDipsScreen(
    modifier: Modifier = Modifier,
    morningForecastEnabled: Boolean = true,
    morningForecastTime: String = "1h",
    peakRemindersEnabled: Boolean = true,
    peakRemindersTime: String = "15m",
    dipReminderEnabled: Boolean = true,
    dipReminderTime: String = "15m",
    onMorningForecastEnabledChange: (Boolean) -> Unit = {},
    onPeakRemindersEnabledChange: (Boolean) -> Unit = {},
    onDipReminderEnabledChange: (Boolean) -> Unit = {},
    onCloseClick: () -> Unit = {},
    onReminderTimeClick: (sectionLabel: String, selectedTime: String, recommendedTime: String) -> Unit = { _, _, _ -> },
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PeaksScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PeaksScreenColor)
        ) {
            ToolDetailTopBar(
                title = "Your Peaks And Dips",
                onBackClick = onCloseClick,
                type = 1
            )

            // Body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .background(PeaksScreenColor)
            ) {
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Morning Forecast Group
                PeaksGroup(
                    title = "Morning forecast",
                    description = "Summary of daily energy updates",
                    checked = morningForecastEnabled,
                    onCheckedChange = onMorningForecastEnabledChange,
                    sectionLabel = "AFTER WAKE",
                    reminderTitle = "Reminder Time",
                    reminderDescription = "After wake",
                    value = morningForecastTime,
                    recommendedTime = "1 hour",
                    onReminderTimeClick = onReminderTimeClick
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Peak Reminders Group
                PeaksGroup(
                    title = "Peak reminders",
                    description = "Peak times and updates",
                    checked = peakRemindersEnabled,
                    onCheckedChange = onPeakRemindersEnabledChange,
                    sectionLabel = "BEFORE PEAKS",
                    reminderTitle = "Reminder Time",
                    reminderDescription = "Before peaks",
                    value = peakRemindersTime,
                    recommendedTime = "15 minutes",
                    onReminderTimeClick = onReminderTimeClick
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Dip Reminder Group
                PeaksGroup(
                    title = "Dip reminder",
                    description = "Dip time and updates",
                    checked = dipReminderEnabled,
                    onCheckedChange = onDipReminderEnabledChange,
                    sectionLabel = "BEFORE DIP",
                    reminderTitle = "Reminder Time",
                    reminderDescription = "Before dip",
                    value = dipReminderTime,
                    recommendedTime = "15 minutes",
                    onReminderTimeClick = onReminderTimeClick
                )
            }
        }
    }
}

@Composable
private fun PeaksGroup(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    sectionLabel: String,
    reminderTitle: String,
    reminderDescription: String,
    value: String,
    recommendedTime: String,
    onReminderTimeClick: (sectionLabel: String, selectedTime: String, recommendedTime: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val reminderRowInteraction = remember { MutableInteractionSource() }
    val isReminderRowPressed by reminderRowInteraction.collectIsPressedAsState()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider(color = PeaksItemStrokeColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(start = 21.dp, end = 21.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = PeaksMutedTextColor,
                    fontSize = 14.sp
                )
            }

            CustomSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }

        HorizontalDivider(color = PeaksItemStrokeColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .background(
                    if (isReminderRowPressed) Color.White.copy(alpha = 0.06f) else Color.Transparent
                )
                .clickable(
                    interactionSource = reminderRowInteraction,
                    indication = null,
                    onClick = {
                        onReminderTimeClick(sectionLabel, value.toReminderOptionLabel(), recommendedTime)
                    }
                )
                .padding(start = 21.dp, end = 21.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = reminderTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reminderDescription,
                        color = PeaksMutedTextColor,
                        fontSize = 14.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = value,
                    color = PeaksMutedTextColor,
                    fontSize = 14.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        HorizontalDivider(color = PeaksItemStrokeColor, thickness = 1.dp)
    }
}

private fun String.toReminderOptionLabel(): String {
    return when (trim().lowercase()) {
        "1h" -> "1 hour"
        "15m" -> "15 minutes"
        "30m" -> "30 minutes"
        "2h" -> "2 hours"
        else -> this
    }
}

@Composable
private fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "SwitchOffset"
    )

    Box(
        modifier = modifier
            .width(36.dp)
            .height(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Track (pill shape) - smaller than circle
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(16.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(8.dp))
                .background(if (checked) Color(0xFF00EDB9) else Color(0xFF2C2C2E))
        )

        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF7EC7C0) else Color(0xFF76767B))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF222328, widthDp = 402, heightDp = 874)
@Composable
private fun PeaksAndDipsScreenPreview() {
    SleeptrackerTheme {
        PeaksAndDipsScreen()
    }
}
