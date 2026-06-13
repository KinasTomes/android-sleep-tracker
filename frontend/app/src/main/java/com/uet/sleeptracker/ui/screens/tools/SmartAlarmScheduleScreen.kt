package com.uet.sleeptracker.ui.screens.tools

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.AlarmDayChip
import com.uet.sleeptracker.ui.components.AlarmPillToggle
import com.uet.sleeptracker.ui.components.SectionLabelBand
import com.uet.sleeptracker.ui.components.SleepTimeRulerPicker
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.components.scaleDp
import com.uet.sleeptracker.ui.components.scaleSp
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlin.math.min

private val SmartAlarmScheduleBackground = Color(0xFF010101)
private val SmartAlarmScheduleMuted = Color(0xFF989898)
private val SmartAlarmScheduleSectionDivider = Color(0xFF141414)
private val SmartAlarmScheduleHeaderBand = Color(0xFF16151A)
private const val SmartAlarmScheduleDesignRowWidth = 477f

private val SmartAlarmDays = listOf("S", "M", "T", "W", "T", "F", "S")

@Composable
fun SmartAlarmScheduleScreen(
    alarm: AlarmItem,
    onEnabledChange: (Boolean) -> Unit,
    onActiveDaysChange: (List<Boolean>) -> Unit,
    onTimeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onGentleWakeUpClick: () -> Unit = {},
    onSoundAndHapticsClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    canDelete: Boolean = true,
) {
    val isAlarmEnabled = alarm.isEnabled
    val currentMinutes = parseTimeStringToMinutes(alarm.time)
    val activeDays = alarm.activeDays

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SmartAlarmScheduleBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SmartAlarmScheduleBackground)
        ) {
            ToolDetailTopBar(
                title = "Smart Alarm",
                onBackClick = onBackClick
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 39.dp)
            ) {
                val contentScale = min(1f, maxWidth.value / SmartAlarmScheduleDesignRowWidth)
                val horizontalInset = scaleDp(10.dp, contentScale)
                val chipWidth = scaleDp(57.dp, contentScale)
                val chipHeight = scaleDp(40.dp, contentScale)
                val chipSpacing = scaleDp(8.dp, contentScale)

                Column(modifier = Modifier.fillMaxSize()) {
                    val allDaysDisabled = activeDays.none { it }

                    SmartAlarmHero(
                        currentMinutes = currentMinutes,
                        isAlarmEnabled = isAlarmEnabled,
                        allDaysDisabled = allDaysDisabled,
                        onToggleChange = onEnabledChange,
                        scale = contentScale,
                        modifier = Modifier.padding(start = 16.dp, end = 26.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = horizontalInset, end = horizontalInset, top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(chipSpacing)
                    ) {
                        SmartAlarmDays.forEachIndexed { index, label ->
                            AlarmDayChip(
                                label = label,
                                isDayActive = activeDays.getOrElse(index) { false },
                                isAlarmEnabled = isAlarmEnabled,
                                chipWidth = chipWidth,
                                chipHeight = chipHeight,
                                scale = contentScale,
                                onClick = {
                                    val newDays = activeDays.toMutableList()
                                    if (index in newDays.indices) {
                                        newDays[index] = !newDays[index]
                                        onActiveDaysChange(newDays)
                                    }
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        color = SmartAlarmScheduleSectionDivider,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 29.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clipToBounds()
                    ) {
                        SleepTimeRulerPicker(
                            currentMinutes = currentMinutes,
                            onTimeChange = onTimeChange
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    SectionLabelBand(
                        text = "SCIENCE TO FEEL LESS GROGGY",
                        backgroundColor = SmartAlarmScheduleHeaderBand,
                        textColor = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )

                    SmartAlarmNavigationRow(
                        title = "Gentle wake up",
                        subtitle = "Wake gradually and naturally",
                        trailingText = "Disabled",
                        onClick = onGentleWakeUpClick,
                        modifier = Modifier.padding(top = 18.dp)
                    )

                    HorizontalDivider(
                        color = SmartAlarmScheduleSectionDivider,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )

                    SmartAlarmNavigationRow(
                        title = "Sound & haptics",
                        subtitle = "Volume, Songs, Vibration",
                        trailingText = null,
                        onClick = onSoundAndHapticsClick
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (canDelete) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onDeleteClick
                                )
                                .padding(bottom = 60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.bin),
                                contentDescription = "Delete",
                                tint = Color(0xFFDA2751),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Delete",
                                color = Color(0xFFDA2751),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartAlarmHero(
    currentMinutes: Int,
    isAlarmEnabled: Boolean,
    allDaysDisabled: Boolean,
    onToggleChange: (Boolean) -> Unit,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val isAlarmActuallyEnabled = isAlarmEnabled && !allDaysDisabled
    val titleColor = if (isAlarmActuallyEnabled) Color.White else SmartAlarmScheduleMuted
    val subtitleColor = Color.White

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = if (isAlarmActuallyEnabled) R.drawable.alarm_enabled else R.drawable.alarm_disabled
            ),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${formatScheduleTime(currentMinutes)} Schedule",
                color = titleColor,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Normal
            )

            when {
                allDaysDisabled -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DisabledText(subtitleColor)

                        Text(
                            text = "Tap a day to enable",
                            color = titleColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                !isAlarmEnabled -> {
                    DisabledText(subtitleColor)
                }

                else -> {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (!allDaysDisabled) {
            AlarmPillToggle(
                checked = isAlarmEnabled,
                onCheckedChange = onToggleChange,
                scale = scale,
            )
        }
    }
}

@Composable
private fun SmartAlarmNavigationRow(
    title: String,
    subtitle: String,
    trailingText: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(start = 20.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = SmartAlarmScheduleMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }

        if (trailingText != null) {
            Text(
                text = trailingText,
                color = SmartAlarmScheduleMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Icon(
            painter = painterResource(id = R.drawable.right),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@SuppressLint("DefaultLocale")
private fun formatScheduleTime(minutes: Int): String {
    val normalizedMinutes = ((minutes % 1440) + 1440) % 1440
    val hour = normalizedMinutes / 60
    val minute = normalizedMinutes % 60
    return String.format("%02d:%02d", hour, minute)
}

@Composable
private fun DisabledText(color: Color) {
    Text(
        text = "Disabled",
        color = color,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF010101, widthDp = 476, heightDp = 956)
@Composable
private fun SmartAlarmScheduleScreenPreview() {
    SleeptrackerTheme {
        SmartAlarmScheduleScreen(
            alarm = AlarmItem("1", "08:00", true, listOf(true, true, true, true, true, true, true)),
            onEnabledChange = {},
            onActiveDaysChange = {},
            onTimeChange = {}
        )
    }
}

private fun parseTimeStringToMinutes(time: String): Int {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return hour * 60 + minute
}
