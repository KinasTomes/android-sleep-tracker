package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val AlarmScheduleCardShape = RoundedCornerShape(16.dp)
private val AlarmScheduleCardBorder = Color(0xFF363636)
private val AlarmSettingsColor = Color(0xFF8962EC)
private const val AlarmDesignRowWidth = 411f

private val DefaultAlarmDays = listOf("S", "M", "T", "W", "T", "F", "S")

@Composable
fun AlarmScheduleCard(
    time: String,
    modifier: Modifier = Modifier,
    settingsLabel: String = "Settings",
    initialAlarmEnabled: Boolean = true,
    initialActiveDays: List<Boolean> = List(DefaultAlarmDays.size) { true },
    onSettingsClick: (() -> Unit)? = null,
) {
    var isAlarmEnabled by rememberSaveable { mutableStateOf(initialAlarmEnabled) }
    val activeDays = remember {
        mutableStateListOf<Boolean>().apply {
            addAll(initialActiveDays.normalizedToWeek())
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .border(1.2.dp, AlarmScheduleCardBorder, AlarmScheduleCardShape)
    ) {
        val designHorizontalInset = 18.dp
        val availableRowWidth = maxWidth - (designHorizontalInset * 2)
        val contentScale = min(1f, availableRowWidth.value / AlarmDesignRowWidth)
        val horizontalInset = scaleDp(18.dp, contentScale)
        val chipWidth = scaleDp(57.dp, contentScale)
        val chipHeight = scaleDp(40.dp, contentScale)
        val chipSpacing = scaleDp(2.dp, contentScale)
        val timeFontSize = scaleSp(28.sp, contentScale)
        val timeLineHeight = scaleSp(34.sp, contentScale)
        val settingsFontSize = scaleSp(16.sp, contentScale)
        val settingsLineHeight = scaleSp(18.sp, contentScale)

        Text(
            text = time,
            color = Color.White,
            fontSize = timeFontSize,
            lineHeight = timeLineHeight,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = horizontalInset, top = scaleDp(23.dp, contentScale))
        )

        AlarmPillToggle(
            checked = isAlarmEnabled,
            onCheckedChange = { isAlarmEnabled = it },
            scale = contentScale,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = scaleDp(27.dp, contentScale),
                    end = scaleDp(24.dp, contentScale)
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = horizontalInset,
                    end = horizontalInset,
                    top = scaleDp(81.dp, contentScale)
                ),
            horizontalArrangement = Arrangement.spacedBy(chipSpacing)
        ) {
            DefaultAlarmDays.forEachIndexed { index, label ->
                AlarmDayChip(
                    label = label,
                    isDayActive = activeDays[index],
                    isAlarmEnabled = isAlarmEnabled,
                    chipWidth = chipWidth,
                    chipHeight = chipHeight,
                    scale = contentScale,
                    onClick = { activeDays[index] = !activeDays[index] }
                )
            }
        }

        Text(
            text = settingsLabel,
            color = AlarmSettingsColor,
            fontSize = settingsFontSize,
            lineHeight = settingsLineHeight,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = horizontalInset, top = scaleDp(151.dp, contentScale), bottom = scaleDp(24.dp, contentScale))
                .then(
                    if (onSettingsClick != null) {
                        Modifier.clickable(onClick = onSettingsClick)
                    } else {
                        Modifier
                    }
                )
        )
    }
}

private fun List<Boolean>.normalizedToWeek(): List<Boolean> {
    return List(DefaultAlarmDays.size) { index -> getOrNull(index) ?: true }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AlarmScheduleCardPreview() {
    SleeptrackerTheme {
        Box(
            modifier = Modifier
                .background(SleepBackground)
                .padding(16.dp)
        ) {
            AlarmScheduleCard(
                time = "08:00",
                initialAlarmEnabled = true,
                initialActiveDays = listOf(true, true, true, true, true, true, true)
            )
        }
    }
}
