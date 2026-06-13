package com.uet.sleeptracker.ui.screens.energy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.AlarmTimePicker
import com.uet.sleeptracker.ui.components.MelatoninWindow
import java.util.Calendar
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

import com.uet.sleeptracker.ui.screens.tools.AlarmItem
import java.util.Locale

@Composable
fun EnergyScheduleScreen(
    profileIcon: Painter,
    lightningIcon: Painter,
    sleepIcon: Painter,
    editIcon: Painter,
    windDownImage: Painter,
    caffeineImage: Painter,
    modifier: Modifier = Modifier,
    viewModel: EnergyScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    showTopBar: Boolean = true,
    alarms: List<AlarmItem> = emptyList(),
    onAlarmEditClick: (AlarmItem) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    EnergyScheduleScreen(
        state = state,
        onCurrentTimeChange = { viewModel.updateCurrentTime(it) },
        profileIcon = profileIcon,
        lightningIcon = lightningIcon,
        sleepIcon = sleepIcon,
        editIcon = editIcon,
        windDownImage = windDownImage,
        caffeineImage = caffeineImage,
        modifier = modifier,
        showTopBar = showTopBar,
        alarms = alarms,
        onAlarmEditClick = onAlarmEditClick
    )
}

@Composable
fun EnergyScheduleScreen(
    state: EnergyScheduleUiState,
    onCurrentTimeChange: (EnergyTimelineTime) -> Unit,
    profileIcon: Painter,
    lightningIcon: Painter,
    sleepIcon: Painter,
    editIcon: Painter,
    windDownImage: Painter,
    caffeineImage: Painter,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    alarms: List<AlarmItem> = emptyList(),
    onAlarmEditClick: (AlarmItem) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        while (true) {
            onCurrentTimeChange(currentEnergyTimelineTime())
            delay(60_000.milliseconds)
        }
    }

    val now = Calendar.getInstance()
    val nextAlarmInfo = alarms
        .mapNotNull { alarm ->
            val nextTime = getNextOccurrence(alarm, now)
            if (nextTime != null) alarm to nextTime else null
        }
        .minByOrNull { it.second.timeInMillis }

    val alarmLabel: String
    val alarmActiveDays: List<Int>
    if (nextAlarmInfo != null) {
        val (alarm, nextTime) = nextAlarmInfo
        val dayLabel = getDayLabel(nextTime, now)
        alarmLabel = "${alarm.time} $dayLabel"
        alarmActiveDays = alarm.activeDays.mapIndexedNotNull { index, isActive ->
            if (isActive) index + 1 else null
        }
    } else {
        alarmLabel = "No alarm"
        alarmActiveDays = emptyList()
    }

    val timelineData = state.timelineData
    val timelineStartTime = state.sleepStartTime.toTimelineStartTime(hoursBefore = 2)
    val timelineLabels = buildEnergyTimelineLabels(timelineStartTime)
    val sleepStartY = state.sleepStartTime.toTimelineY(timelineStartTime)
    val totalSleepTitleY = (sleepStartY.value - TotalSleepTimeTitleOffset).dp
    val totalSleepValueY = totalSleepTitleY + 33.dp
    val phaseLayouts = timelineData.phases.map { phase ->
        EnergyPhaseLayout(
            phase = phase,
            y = phase.startTime.toTimelineY(timelineStartTime),
            height = phase.durationMinutes.toTimelineHeight()
        )
    }.withMinimumPhaseSpacing()
    val phaseLayoutById = phaseLayouts.associateBy { it.phase.id }
    val melatoninLayout = phaseLayoutById["melatonin_window"]
    val sleepNeedY = if (melatoninLayout != null) {
        melatoninLayout.y + melatoninLayout.height + 16.dp
    } else {
        timelineData.sleepNeedTime.toTimelineY(timelineStartTime)
    }
    val nextAlarmTimelineTime = if (nextAlarmInfo != null) {
        val (alarm, nextTime) = nextAlarmInfo
        val parts = alarm.time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
        EnergyTimelineTime(hour = hour, minute = minute, dayOffset = state.sleepEndTime.dayOffset + 1)
    } else {
        null
    }

    val wakeWindowStartTime = state.wakeWindowStartTime
    val wakeWindowEndTime = state.wakeWindowEndTime

    val wakeWindowY = wakeWindowStartTime.toTimelineY(timelineStartTime)
    val wakeWindowDuration = calculateDurationMinutes(wakeWindowStartTime, wakeWindowEndTime)
    val wakeWindowHeight = wakeWindowDuration.toTimelineHeight()

    val alarmTimelineTime = nextAlarmTimelineTime ?: state.alarmTime.copy(dayOffset = state.sleepEndTime.dayOffset + 1)
    val alarmTimeY = alarmTimelineTime.toTimelineY(timelineStartTime)
    val alarmOffset = alarmTimeY - wakeWindowY - 37.dp
    val contentHeight = maxOf(
        EnergyTimelineContentHeight,
        wakeWindowY.value + wakeWindowHeight.value + 100f,
        sleepNeedY.value + 100f,
        phaseLayouts.maxOfOrNull { it.y.value + it.height.value + 120f } ?: EnergyTimelineContentHeight
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        val scale = maxWidth.value / 476f
        val hiddenTopBarHeight = if (showTopBar) 0f else 104f

        Box(
            modifier = Modifier
                .width(maxWidth)
                .height(((contentHeight - hiddenTopBarHeight) * scale).dp)
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0f)
                }
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-hiddenTopBarHeight).dp)
                        .width(476.dp)
                        .height(contentHeight.dp)
                        .background(Color.Black)
                ) {
                    EnergyTimelineGridBackground(
                        height = contentHeight.dp,
                        labels = timelineLabels
                    )

                    if (showTopBar) {
                        EnergyTopBar(
                            profileIcon = profileIcon,
                            lightningIcon = lightningIcon,
                            energyPercent = state.energyPercent
                        )
                    }

                    Text(
                        text = "Total sleep time",
                        color = Color.White,
                        fontSize = 28.sp,
                        letterSpacing = (-1).sp,
                        modifier = Modifier.offset(x = 54.dp, y = totalSleepTitleY)
                    )

                    Text(
                        text = state.totalSleepTimeText,
                        color = Color(0xFF848484),
                        fontSize = 28.sp,
                        letterSpacing = (-1).sp,
                        modifier = Modifier.offset(x = 54.dp, y = totalSleepValueY)
                    )

                    val sleepDurationMinutes = calculateDurationMinutes(state.sleepStartTime, state.sleepEndTime)
                    val sleepHeight = sleepDurationMinutes.toTimelineHeight()

                    TotalSleepTimeOverlay(
                        modifier = Modifier.offset(x = 56.dp, y = sleepStartY),
                        sleepIcon = sleepIcon,
                        editIcon = editIcon,
                        startTime = state.sleepStartTime.formatTime(),
                        endTime = state.sleepEndTime.formatTime(),
                        height = sleepHeight
                    )

                    phaseLayouts.forEach { phaseLayout ->
                        EnergyPhaseBlock(phaseLayout = phaseLayout)
                    }

                    Text(
                        text = "Sleep need",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.offset(x = 66.dp, y = sleepNeedY)
                    )

                    Text(
                        text = timelineData.sleepNeedText,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.offset(x = 66.dp, y = sleepNeedY + 25.dp)
                    )

                    timelineData.activities.forEach { activity ->
                        val phaseLayout = phaseLayoutById[activity.phaseId] ?: return@forEach
                        ActivityCard(
                            modifier = Modifier.offset(
                                x = 349.dp,
                                y = phaseLayout.y + activity.yOffset
                            ),
                            image = when (activity.imageType) {
                                EnergyActivityImageType.Caffeine -> caffeineImage
                                EnergyActivityImageType.WindDown -> windDownImage
                            },
                            title = activity.title
                        )
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = 0.dp, y = wakeWindowY)
                            .width(52.dp)
                            .height(wakeWindowHeight)
                            .background(Color(0xFF222222))
                    )

                    WakeWindowBlock(
                        modifier = Modifier
                            .offset(x = 0.dp, y = wakeWindowY)
                            .zIndex(11f),
                        alarmTimeText = alarmLabel,
                        activeDays = alarmActiveDays,
                        height = wakeWindowHeight,
                        alarmOffset = alarmOffset,
                        onClickAlarm = {
                            nextAlarmInfo?.first?.let { onAlarmEditClick(it) }
                        }
                    )

                    EnergyTimelineRuler(
                        modifier = Modifier.zIndex(10f),
                        height = contentHeight.dp,
                        labels = timelineLabels,
                        currentTime = state.currentTime,
                        timelineStartTime = timelineStartTime
                    )
                }
            }
        }
    }
}

@Composable
private fun EnergyTopBar(
    profileIcon: Painter,
    lightningIcon: Painter,
    energyPercent: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(Color(0xFF010101))
            .border(
                width = 1.dp,
                color = Color(0xFF383838)
            )
    ) {
        Text(
            text = "My energy schedule",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.offset(x = 19.dp, y = 23.dp)
        )

        Text(
            text = "Great",
            color = Color(0xFF848484),
            fontSize = 28.sp,
            modifier = Modifier.offset(x = 19.dp, y = 54.dp)
        )

        Image(
            painter = lightningIcon,
            contentDescription = null,
            modifier = Modifier
                .offset(x = 105.dp, y = 60.dp)
                .size(24.dp)
        )

        Text(
            text = energyPercent,
            color = Color(0xFF00EBAC),
            fontSize = 28.sp,
            modifier = Modifier.offset(x = 129.dp, y = 54.dp)
        )

        Image(
            painter = profileIcon,
            contentDescription = null,
            modifier = Modifier
                .offset(x = 412.dp, y = 39.dp)
                .size(41.dp)
        )
    }
}

private const val TotalSleepTimeTitleOffset = 96f
private fun List<EnergyPhaseLayout>.withMinimumPhaseSpacing(): List<EnergyPhaseLayout> {
    var previousBottom = Float.NEGATIVE_INFINITY

    return sortedBy { it.y.value }.map { layout ->
        val resolvedY = maxOf(
            layout.y.value,
            previousBottom + EnergyPhaseMinSpacing
        )

        previousBottom = resolvedY + layout.height.value
        layout.copy(y = resolvedY.dp)
    }
}

@Composable
private fun EnergyTimelineGridBackground(
    height: Dp,
    labels: List<String>
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .height(height)
    ) {
        val hourGap = 119.dp.toPx()
        val startY = 103.dp.toPx()

        labels.indices.forEach { index ->
            val y = startY + index * hourGap

            drawLine(
                color = Color(0xFF1D1D1D),
                start = Offset(59.dp.toPx(), y),
                end = Offset(469.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@Composable
private fun EnergyTimelineRuler(
    modifier: Modifier = Modifier,
    height: Dp,
    labels: List<String>,
    currentTime: EnergyTimelineTime,
    timelineStartTime: EnergyTimelineTime
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .height(height)
    ) {
        val hourGap = 119.dp.toPx()
        val startY = 103.dp.toPx()

        labels.indices.forEach { index ->
            val y = startY + index * hourGap

            drawLine(
                color = Color(0xFF888888),
                start = Offset(19.dp.toPx(), y - 60.dp.toPx()),
                end = Offset(35.dp.toPx(), y - 60.dp.toPx()),
                strokeWidth = 1.25.dp.toPx()
            )

            drawLine(
                color = Color(0xFF353535),
                start = Offset(19.dp.toPx(), y - 28.dp.toPx()),
                end = Offset(27.dp.toPx(), y - 28.dp.toPx()),
                strokeWidth = 1.25.dp.toPx()
            )

            drawLine(
                color = Color(0xFF353535),
                start = Offset(19.dp.toPx(), y + 28.dp.toPx()),
                end = Offset(27.dp.toPx(), y + 28.dp.toPx()),
                strokeWidth = 1.25.dp.toPx()
            )
        }
    }

    labels.forEachIndexed { index, label ->
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.offset(
                x = 19.dp,
                y = (94 + index * 119).dp
            )
        )
    }

    Box(
        modifier = Modifier
            .offset(x = 52.dp, y = 104.dp)
            .width(4.dp)
            .height(height)
            .background(Color.Black)
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .height(height)
    ) {
        val currentTimeY = currentTime.toTimelineY(timelineStartTime).toPx()

        if (currentTimeY in 0f..height.toPx()) {
            drawLine(
                color = Color.White,
                start = Offset(52.dp.toPx(), currentTimeY),
                end = Offset(469.dp.toPx(), currentTimeY),
                strokeWidth = 1.4.dp.toPx()
            )

            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = Offset(54.dp.toPx(), currentTimeY)
            )
        }
    }
}

private fun currentEnergyTimelineTime(): EnergyTimelineTime {
    val calendar = Calendar.getInstance()

    return EnergyTimelineTime(
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE),
        dayOffset = 1
    )
}

@Composable
private fun EnergyPhaseBlock(
    phaseLayout: EnergyPhaseLayout
) {
    val phase = phaseLayout.phase

    when (phase.style) {
        EnergyPhaseStyle.Melatonin -> {
            EnergyMelatoninWindow(
                modifier = Modifier.offset(x = 0.dp, y = phaseLayout.y),
                height = phaseLayout.height,
                title = phase.title,
                subtitle = phase.subtitle
            )
        }

        EnergyPhaseStyle.EveningPeak -> {
            EnergySectionCard(
                modifier = Modifier.offset(x = (-9).dp, y = phaseLayout.y),
                title = phase.title,
                height = phaseLayout.height,
                width = 364.dp,
                background = Color(0xFF333333)
            )
        }

        EnergyPhaseStyle.Grogginess -> {
            GrogginessWaveCard(
                modifier = Modifier.offset(x = 0.dp, y = phaseLayout.y),
                title = phase.title,
                height = phaseLayout.height
            )
        }

        EnergyPhaseStyle.Standard -> {
            EnergySectionCard(
                modifier = Modifier.offset(x = 0.dp, y = phaseLayout.y),
                title = phase.title,
                subtitle = phase.subtitle,
                height = phaseLayout.height,
                dotColor = if (phase.id == "morning_peak") {
                    Color(0xFFFF5A8A)
                } else {
                    Color(0xFF721BFD)
                }
            )
        }
    }
}

@Composable
private fun GrogginessWaveCard(
    modifier: Modifier = Modifier,
    title: String,
    width: Dp = 355.dp,
    height: Dp
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(shape)
            .background(Color(0xB316151A))
            .border(
                width = 2.dp,
                color = Color(0xFF363636),
                shape = shape
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val spacing = 28.dp.toPx()
            val amplitude = 18.dp.toPx()
            val waveLength = 92.dp.toPx()
            val strokeWidth = 1.25.dp.toPx()
            val diagonalSpan = size.width + size.height

            rotate(
                degrees = 120f,
                pivot = Offset(size.width / 2f, size.height / 2f)
            ) {
                var baseY = -diagonalSpan

                while (baseY < diagonalSpan * 1.5f) {
                    val path = Path().apply {
                        moveTo(-diagonalSpan, baseY)

                        var x = -diagonalSpan
                        while (x < diagonalSpan * 1.5f) {
                            cubicTo(
                                x + waveLength * 0.25f,
                                baseY - amplitude,
                                x + waveLength * 0.75f,
                                baseY + amplitude,
                                x + waveLength,
                                baseY
                            )
                            x += waveLength
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color.White.copy(alpha = 0.16f),
                        style = Stroke(width = strokeWidth)
                    )

                    baseY += spacing
                }
            }
        }

        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 77.dp, y = 18.dp)
        )
    }
}

@Composable
private fun EnergySectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    width: Dp = 355.dp,
    height: Dp,
    background: Color = Color(0xFF16151A),
    dotColor: Color = Color(0xFF721BFD)
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(
                color = background,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF363636),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 77.dp, y = 18.dp)
        )

        if (subtitle != null) {
            Box(
                modifier = Modifier
                    .offset(x = 77.dp, y = 45.dp)
                    .size(10.dp)
                    .background(dotColor, CircleShape)
            )

            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.offset(x = 92.dp, y = 39.dp)
            )
        }
    }
}

@Composable
private fun EnergyMelatoninWindow(
    modifier: Modifier = Modifier,
    height: Dp,
    title: String,
    subtitle: String?
) {
    Box(
        modifier = modifier
            .width(355.dp)
            .height(height)
    ) {
        MelatoninWindow(
            sizeN = 355.dp,
            heightM = height
        )

        Image(
            painter = painterResource(id = R.drawable.moon),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(38.dp)
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 77.dp, y = 18.dp)
        )

        if (subtitle != null) {
            Box(
                modifier = Modifier
                    .offset(x = 77.dp, y = 45.dp)
                    .size(10.dp)
                    .background(Color(0xFF721BFD), CircleShape)
            )

            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.offset(x = 92.dp, y = 39.dp)
            )
        }
    }
}

@Composable
private fun TotalSleepTimeOverlay(
    modifier: Modifier = Modifier,
    sleepIcon: Painter,
    editIcon: Painter,
    startTime: String = "23:10",
    endTime: String = "4:45",
    height: Dp = 677.dp
) {
    Box(
        modifier = modifier
            .width(415.dp)
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - 6.dp)
                .offset(y = 3.dp)
                .background(
                    Color(0x73721CFD),
                    RoundedCornerShape(12.dp)
                )
                .border(
                    2.dp,
                    Color(0xFF8C65F2),
                    RoundedCornerShape(12.dp)
                )
        )

        Box(
            modifier = Modifier
                .offset(x = 36.dp)
                .width(70.dp)
                .height(8.dp)
                .background(Color(0xFF8D59FC), RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-33).dp)
                .width(70.dp)
                .height(8.dp)
                .background(Color(0xFF8D59FC), RoundedCornerShape(8.dp))
        )

        Image(
            painter = sleepIcon,
            contentDescription = null,
            modifier = Modifier
                .offset(x = 8.dp, y = 21.dp)
                .size(12.dp)
        )

        Text(
            text = startTime,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 44.dp, y = 15.dp)
        )

        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = editIcon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Press to edit sleep time",
                color = Color(0xFF8F5AFF),
                fontSize = 16.sp
            )
        }

        Text(
            text = endTime,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-52).dp, y = (-14).dp)
        )
    }
}

@Composable
private fun ActivityCard(
    modifier: Modifier = Modifier,
    image: Painter,
    title: String
) {
    Row(
        modifier = modifier
            .width(120.dp)
            .height(32.dp)
            .background(Color(0xFF333333), RoundedCornerShape(4.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            .padding(start = 6.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 10.sp,
            lineHeight = 10.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun WakeWindowBlock(
    modifier: Modifier = Modifier,
    alarmTimeText: String,
    activeDays: List<Int>,
    height: Dp = 110.dp,
    alarmOffset: Dp = 18.5.dp,
    onClickAlarm: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(476.dp)
            .height(height + 70.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val lineStart = 0f
            val lineEnd = 469.dp.toPx()
            val grayColor = Color(0xFF353535)
            val strokeThickness = 3f

            // Top boundary line of the wake window (shifted down slightly to avoid clipping)
            drawLine(
                color = grayColor,
                start = Offset(lineStart, strokeThickness / 2),
                end = Offset(lineEnd, strokeThickness / 2),
                strokeWidth = strokeThickness
            )

            // Bottom boundary line of the wake window
            val bottomY = height.toPx()
            drawLine(
                color = grayColor,
                start = Offset(lineStart, bottomY),
                end = Offset(lineEnd, bottomY),
                strokeWidth = strokeThickness
            )
        }

        Text(
            text = "Wake window",
            color = Color(0xFF848484),
            fontSize = 18.sp,
            modifier = Modifier.offset(x = 179.dp, y = height - 7.dp)
        )

        AlarmTimePicker(
            modifier = Modifier.offset(x = 54.dp, y = alarmOffset),
            timeText = alarmTimeText,
            activeDays = activeDays,
            onClick = onClickAlarm
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 476,
    heightDp = 1000
)
@Composable
private fun EnergyScheduleScreenPreview() {
    MaterialTheme {
        EnergyScheduleScreen(
            state = EnergyScheduleUiState(),
            onCurrentTimeChange = {},
            profileIcon = ColorPainter(Color(0xFF721BFD)),
            lightningIcon = ColorPainter(Color(0xFF00EBAC)),
            sleepIcon = ColorPainter(Color.White),
            editIcon = ColorPainter(Color.White),
            windDownImage = ColorPainter(Color(0xFF8D59FC)),
            caffeineImage = ColorPainter(Color(0xFFFF4F7B)),
            alarms = listOf(
                AlarmItem(id = "1", time = "06:05", isEnabled = true, activeDays = listOf(true, true, true, true, true, true, true))
            )
        )
    }
}

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
