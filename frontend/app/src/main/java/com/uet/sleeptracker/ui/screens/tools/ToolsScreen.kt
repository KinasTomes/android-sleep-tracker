package com.uet.sleeptracker.ui.screens.tools

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepAudioCard
import com.uet.sleeptracker.ui.components.SleepSuggestionCardDetailLine
import com.uet.sleeptracker.ui.components.SleepSuggestionCardTextStyle
import com.uet.sleeptracker.ui.components.SleepSuggestionCard
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle

private val ToolsMutedText = Color(0xFF989898)

@Composable
fun ToolsScreen(
    modifier: Modifier = Modifier,
    alarms: List<AlarmItem> = emptyList(),
    isAudioPlaying: Boolean = false,
    onAudioClick: () -> Unit = {},
    onSmartAlarmClick: () -> Unit = {},
    onSleepQualityClick: () -> Unit = {},
    onPrepBedroomClick: () -> Unit = {},
    onBrainDumpClick: () -> Unit = {},
    onMelatoninWindowClick: () -> Unit = {},
    onMelatoninSupplementsClick: () -> Unit = {},
    onBlueLightGlassesClick: () -> Unit = {},
    onAvoidLateMealsClick: () -> Unit = {},
    onEarlierWorkoutsClick: () -> Unit = {},
    onAvoidLateAlcoholClick: () -> Unit = {},
    onCaffeineCutoffClick: () -> Unit = {},
    onEveningRoutineClick: () -> Unit = {},
    onMorningRoutineClick: () -> Unit = {},
    onBlueLightControlClick: () -> Unit = {},
    onPeaksAndDipsClick: () -> Unit = {},
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    val now = Calendar.getInstance()
    val nextAlarmInfo = alarms
        .mapNotNull { alarm ->
            val nextTime = getNextOccurrence(alarm, now)
            if (nextTime != null) alarm to nextTime else null
        }
        .minByOrNull { it.second.timeInMillis }

    val alarmLabel: String
    val alarmActiveDays: List<Boolean>
    if (nextAlarmInfo != null) {
        val (alarm, nextTime) = nextAlarmInfo
        val dayLabel = getDayLabel(nextTime, now)
        alarmLabel = "${alarm.time} $dayLabel"
        alarmActiveDays = alarm.activeDays
    } else {
        alarmLabel = "No alarm"
        alarmActiveDays = List(7) { false }
    }

    val annotatedDays = buildAnnotatedString {
        val daysString = "S M T W T F S"
        append(daysString)
        for (day in 0..6) {
            val isActive = alarmActiveDays.getOrElse(day) { false }
            val color = if (isActive) Color.White else ToolsMutedText
            addStyle(
                style = SpanStyle(color = color),
                start = day * 2,
                end = day * 2 + 1
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .background(SleepBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 19.dp, end = 20.dp, top = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            SleepSuggestionCard(
                modifier = Modifier.weight(1f),
                title = "Smart Alarm",
                backgroundImageResId = R.drawable.smart_alarm,
                textStyle = SleepSuggestionCardTextStyle.Details,
                detailLines = listOf(
                    SleepSuggestionCardDetailLine(alarmLabel, ToolsMutedText),
                    SleepSuggestionCardDetailLine(annotatedDays, Color.Unspecified)
                ),
                onClick = onSmartAlarmClick
            )
            SleepSuggestionCard(
                modifier = Modifier.weight(1f),
                title = "Sleep quality",
                backgroundImageResId = R.drawable.sleep_quality,
                textStyle = SleepSuggestionCardTextStyle.Details,
                detailLines = listOf(
                    SleepSuggestionCardDetailLine("Monitor progress by", ToolsMutedText),
                    SleepSuggestionCardDetailLine("tracking how you feel", ToolsMutedText)
                ),
                onClick = onSleepQualityClick
            )
        }

        SectionTitle(
            text = "Sleep aids",
            modifier = Modifier.padding(start = 19.dp, top = 34.dp)
        )

        SleepAudioCard(
            title = "Sleep sounds",
            subtitle = if (isAudioPlaying) "Playing..." else "Ambient Music",
            onClick = onAudioClick,
            isPlaying = isAudioPlaying,
            modifier = Modifier.padding(start = 19.dp, end = 19.dp, top = 6.dp)
        )

        EyebrowTitle(
            text = "GUIDES",
            modifier = Modifier.padding(start = 19.dp, top = 26.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 19.dp, end = 19.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            SleepSuggestionCard(
                title = "Write a brain dump",
                subtitle = "Ease your mind before bed",
                backgroundImageResId = R.drawable.write_a_brain_dump,
                onClick = onBrainDumpClick,
                modifier = Modifier.weight(1f)
            )
            SleepSuggestionCard(
                title = "Prep the bedroom",
                subtitle = "Guidance to an\nenvironment check",
                backgroundImageResId = R.drawable.prep_the_bedroom,
                onClick = onPrepBedroomClick,
                modifier = Modifier.weight(1f)
            )
        }

        SectionTitle(
            text = "Habit Reminders",
            modifier = Modifier.padding(start = 19.dp, top = 50.dp)
        )

        Text(
            text = "Optimize routines with nudges aligned with your daily energy schedule",
            color = ToolsMutedText,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 19.dp, end = 20.dp, top = 11.dp)
        )

        ReminderRailSection(
            title = "START AT THE RIGHT TIME",
            items = listOf(
                ToolIconItem("Melatonin\nwindow", R.drawable.melatonin_window, onMelatoninWindowClick),
                ToolIconItem("Melatonin\nsupplements", R.drawable.melatonin_supplement, onMelatoninSupplementsClick),
                ToolIconItem("Blue light\nglasses", R.drawable.blue_light_glasses, onBlueLightGlassesClick)
            ),
            modifier = Modifier.padding(top = 44.dp),
            scrollState = horizontalScrollState
        )

        ReminderRailSection(
            title = "STOP AT THE RIGHT TIME",
            items = listOf(
                ToolIconItem("Caffeine", R.drawable.ceffeine, onCaffeineCutoffClick),
                ToolIconItem("Meals", R.drawable.meals, onAvoidLateMealsClick),
                ToolIconItem("Workouts", R.drawable.workouts, onEarlierWorkoutsClick),
                ToolIconItem("Alcohol", R.drawable.alcohols, onAvoidLateAlcoholClick)
            ),
            modifier = Modifier.padding(top = 42.dp),
            scrollState = rememberScrollState()
        )

        ReminderRailSection(
            title = "OPTIMIZE ROUTINES",
            items = listOf(
                ToolIconItem("Evening", R.drawable.evening, onEveningRoutineClick),
                ToolIconItem("Morning", R.drawable.morning, onMorningRoutineClick),
                ToolIconItem("Blue Light", R.drawable.blue_light, onBlueLightControlClick),
                ToolIconItem("Peaks & Dips", R.drawable.paeks_n_dips, onPeaksAndDipsClick)
            ),
            modifier = Modifier.padding(top = 42.dp, bottom = 140.dp),
            scrollState = rememberScrollState()
        )
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 24.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier
    )
}

@Composable
private fun EyebrowTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier
    )
}

@Composable
private fun ReminderRailSection(
    title: String,
    items: List<ToolIconItem>,
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        EyebrowTitle(
            text = title,
            modifier = Modifier.padding(start = 19.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(start = 19.dp, end = 19.dp, top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items.forEach { item ->
                ReminderIconItem(item = item)
            }
        }
    }
}

@Composable
private fun ReminderIconItem(
    item: ToolIconItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = 88.dp, height = 130.dp)
            .clickable(onClick = item.onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = item.label,
            color = ToolsMutedText,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

private data class ToolIconItem(
    val label: String,
    @param:DrawableRes val imageRes: Int,
    val onClick: () -> Unit = {},
)

private fun getNextOccurrence(alarm: AlarmItem, now: Calendar): Calendar? {
    if (!alarm.isEnabled || alarm.activeDays.none { it }) return null
    val parts = alarm.time.split(":")
    if (parts.size != 2) return null
    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null

    for (offset in 0..7) {
        val candidate = (now.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, offset)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dayOfWeekIndex = candidate.get(Calendar.DAY_OF_WEEK) - 1
        if (alarm.activeDays.getOrElse(dayOfWeekIndex) { false }) {
            if (candidate.after(now)) {
                return candidate
            }
        }
    }
    return null
}

private fun getDayLabel(nextTime: Calendar, now: Calendar): String {
    val nextYear = nextTime.get(Calendar.YEAR)
    val nextDayOfYear = nextTime.get(Calendar.DAY_OF_YEAR)

    val todayYear = now.get(Calendar.YEAR)
    val todayDayOfYear = now.get(Calendar.DAY_OF_YEAR)

    val tomorrow = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
    val tomorrowYear = tomorrow.get(Calendar.YEAR)
    val tomorrowDayOfYear = tomorrow.get(Calendar.DAY_OF_YEAR)

    return when {
        nextYear == todayYear && nextDayOfYear == todayDayOfYear -> "Today"
        nextYear == tomorrowYear && nextDayOfYear == tomorrowDayOfYear -> "Tomorrow"
        else -> {
            nextTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US) ?: ""
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ToolsScreenPreview() {
    SleeptrackerTheme {
        ToolsScreen()
    }
}
