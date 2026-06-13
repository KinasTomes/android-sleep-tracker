package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.TotalSleepTimeOverlay
import com.uet.sleeptracker.ui.theme.SleepPrimary

private val EditSleepBackground = Color(0xFF111111)
private val EditSleepTopBar = Color(0xFF222328)
private val EditSleepDivider = Color(0xFF363636)
private val EditSleepMuted = Color.White.copy(alpha = 0.5f)
private val EditSleepHourHeight = 112.dp
private val EditSleepTimelineTopInset = 12.dp
private val EditSleepRulerWidth = 48.dp
private val NapsTopSpacing = 16.dp

@Composable
fun EditSleepScreen(
    title: String,
    startTime: String,
    endTime: String,
    duration: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var napMinutes by rememberSaveable { mutableIntStateOf(15) }
    var draftNapMinutes by rememberSaveable { mutableIntStateOf(napMinutes) }
    var showNapsOverlay by rememberSaveable { mutableStateOf(false) }
    var selectedStartTime by rememberSaveable(startTime) { mutableStateOf(startTime) }
    var selectedEndTime by rememberSaveable(endTime) { mutableStateOf(endTime) }
    var selectedDuration by rememberSaveable(duration) { mutableStateOf(duration) }
    var showDurationOverlay by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(EditSleepBackground),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            EditSleepTopBar(
                title = title,
                onCloseClick = onCloseClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                SleepEditorTimeline(
                    startTime = selectedStartTime,
                    endTime = selectedEndTime,
                    duration = selectedDuration,
                    onTimeClick = { showDurationOverlay = true },
                )

                Spacer(modifier = Modifier.height(NapsTopSpacing))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = EditSleepDivider,
                )

                NapsRow(
                    napMinutes = napMinutes,
                    onClick = {
                        draftNapMinutes = napMinutes
                        showNapsOverlay = true
                    },
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (showNapsOverlay) {
            NapsEditorOverlay(
                napMinutes = draftNapMinutes,
                onDecrease = {
                    draftNapMinutes = (draftNapMinutes - 5).coerceAtLeast(0)
                },
                onIncrease = {
                    draftNapMinutes = (draftNapMinutes + 5).coerceAtMost(180)
                },
                onCancel = {
                    draftNapMinutes = napMinutes
                    showNapsOverlay = false
                },
                onSave = {
                    napMinutes = draftNapMinutes
                    showNapsOverlay = false
                },
            )
        }

        if (showDurationOverlay) {
            EditSleepDurationOverlay(
                initialStartMinutes = clockMinutes(selectedStartTime),
                initialEndMinutes = clockMinutes(selectedEndTime),
                onCancel = { showDurationOverlay = false },
                onSave = { newStartMinutes, newEndMinutes ->
                    selectedStartTime = formatClockTime(newStartMinutes)
                    selectedEndTime = formatClockTime(newEndMinutes)
                    selectedDuration = formatSleepDuration(
                        startMinutes = newStartMinutes,
                        endMinutes = newEndMinutes,
                    )
                    showDurationOverlay = false
                },
            )
        }
    }
}

@Composable
private fun EditSleepTopBar(
    title: String,
    onCloseClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EditSleepTopBar),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp),
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.Center),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 18.dp)
                    .size(36.dp)
                    .background(Color(0xFF2C2C2E), CircleShape)
                    .clickable(onClick = onCloseClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFF55565C),
        )
    }
}

@Composable
private fun SleepEditorTimeline(
    startTime: String,
    endTime: String,
    duration: String,
    onTimeClick: () -> Unit,
) {
    val startMinutes = timelineMinutes(startTime)
    val endMinutes = timelineMinutes(endTime, afterMinutes = startMinutes)
    val timelineStartMinutes = startMinutes - 60
    val timelineEndMinutes = endMinutes + 60
    val timelineHeight = EditSleepHourHeight *
        ((timelineEndMinutes - timelineStartMinutes) / 60f)
    val editorTop = EditSleepHourHeight
    val editorHeight = (endMinutes - startMinutes)
        .coerceAtLeast(30)
        .let { minutes -> EditSleepHourHeight * (minutes / 60f) }
    val summaryTitleY = (editorTop - 48.dp).coerceAtLeast(8.dp)
    val summaryValueY = summaryTitleY + 20.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(timelineHeight)
            .clipToBounds(),
    ) {
        TimeRuler(
            startMinutes = timelineStartMinutes,
            endMinutes = timelineEndMinutes,
            modifier = Modifier
                .width(EditSleepRulerWidth)
                .height(timelineHeight),
        )

        Text(
            text = "Total sleep time",
            color = Color.White,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            modifier = Modifier.offset(x = 68.dp, y = summaryTitleY),
        )

        Text(
            text = duration,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 20.sp,
            lineHeight = 18.sp,
            modifier = Modifier.offset(x = 68.dp, y = summaryValueY),
        )

        TotalSleepTimeOverlay(
            startTime = startTime,
            endTime = endTime,
            overlayHeight = editorHeight,
            timeFontSize = 16.sp,
            editFontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 68.dp, end = 18.dp)
                .offset(y = editorTop + 2.dp),
            onStartTimeClick = onTimeClick,
            onEndTimeClick = onTimeClick,
        )
    }
}

@Composable
private fun TimeRuler(
    startMinutes: Int,
    endMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val firstTickMinutes = ceilToQuarterHour(startMinutes)
        val tickMinutes = generateSequence(firstTickMinutes) { it + 15 }
            .takeWhile { it <= endMinutes }
            .toList()

        Canvas(modifier = Modifier.fillMaxSize()) {
            tickMinutes
                .filter { it % 60 != 0 }
                .forEach { minute ->
                    val minuteInHour = minute % 60
                    val tickWidth = if (minuteInHour == 30) 16.dp else 8.dp
                    val y = EditSleepHourHeight.toPx() *
                        ((minute - startMinutes) / 60f)

                    drawLine(
                        color = Color(0xFFD9D9D9),
                        start = Offset(17.dp.toPx(), y),
                        end = Offset((17.dp + tickWidth).toPx(), y),
                        strokeWidth = 2.dp.toPx(),
                    )
            }
        }

        tickMinutes
            .filter { it % 60 == 0 }
            .forEach { minute ->
            val hourCenterY = EditSleepHourHeight *
                ((minute - startMinutes) / 60f)
            Text(
                text = ((minute / 60) % 24).toString().padStart(2, '0'),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(
                    x = 8.dp,
                    y = hourCenterY - 8.dp,
                )
                    .width(28.dp)
                    .height(16.dp),
            )
        }
    }
}

@Composable
private fun NapsRow(
    napMinutes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp, vertical = 28.dp)
            .height(72.dp)
            .background(EditSleepTopBar, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Naps",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${napMinutes}m",
            color = EditSleepMuted,
            fontSize = 16.sp,
        )
        Icon(
            painter = painterResource(id = R.drawable.right),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp),
        )
    }
}

@Composable
private fun NapsEditorOverlay(
    napMinutes: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.58f)),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 33.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(EditSleepTopBar, RoundedCornerShape(16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Naps",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(top = 13.dp),
                )

                Text(
                    text = "Total time spent napping today",
                    color = Color.White,
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 63.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NapAdjustmentButton(
                        isPlus = false,
                        onClick = onDecrease,
                    )

                    Text(
                        text = "${napMinutes}m",
                        color = Color.White,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                    )

                    NapAdjustmentButton(
                        isPlus = true,
                        onClick = onIncrease,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                NapsDialogButton(
                    text = "CANCEL",
                    filled = false,
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                )
                NapsDialogButton(
                    text = "SAVE",
                    filled = true,
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun NapAdjustmentButton(
    isPlus: Boolean,
    onClick: () -> Unit,
) {
    Canvas(
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = onClick),
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val halfLine = 15.dp.toPx()

        drawLine(
            color = Color(0xFFD9D9D9),
            start = Offset(center.x - halfLine, center.y),
            end = Offset(center.x + halfLine, center.y),
            strokeWidth = 2.dp.toPx(),
        )
        if (isPlus) {
            drawLine(
                color = Color(0xFFD9D9D9),
                start = Offset(center.x, center.y - halfLine),
                end = Offset(center.x, center.y + halfLine),
                strokeWidth = 2.dp.toPx(),
            )
        }
    }
}

@Composable
private fun NapsDialogButton(
    text: String,
    filled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .height(72.dp)
            .background(
                color = if (filled) SleepPrimary else Color.Black,
                shape = shape,
            )
            .then(
                if (filled) {
                    Modifier
                } else {
                    Modifier.border(
                        width = 2.dp,
                        color = SleepPrimary,
                        shape = shape,
                    )
                },
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            lineHeight = 24.sp,
        )
    }
}

private fun timelineMinutes(
    time: String,
    afterMinutes: Int? = null,
): Int {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    var result = hour * 60 + minute
    if (hour < 12) {
        result += 24 * 60
    }
    if (afterMinutes != null && result <= afterMinutes) {
        result += 24 * 60
    }
    return result
}

private fun clockMinutes(time: String): Int {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return (hour * 60 + minute).coerceIn(0, 24 * 60 - 1)
}

private fun formatClockTime(minutes: Int): String {
    val normalized = ((minutes % (24 * 60)) + (24 * 60)) % (24 * 60)
    val hour = normalized / 60
    val minute = normalized % 60
    return "%02d:%02d".format(hour, minute)
}

private fun formatSleepDuration(
    startMinutes: Int,
    endMinutes: Int,
): String {
    val normalizedStart = ((startMinutes % (24 * 60)) + (24 * 60)) % (24 * 60)
    var normalizedEnd = ((endMinutes % (24 * 60)) + (24 * 60)) % (24 * 60)
    if (normalizedEnd <= normalizedStart) {
        normalizedEnd += 24 * 60
    }
    val durationMinutes = normalizedEnd - normalizedStart
    return "${durationMinutes / 60}h ${durationMinutes % 60}m"
}

private fun ceilToQuarterHour(minutes: Int): Int =
    ((minutes + 14) / 15) * 15

@Preview(showBackground = true, backgroundColor = 0xFF111111, widthDp = 476, heightDp = 1000)
@Composable
private fun EditSleepScreenPreview() {
    EditSleepScreen(
        title = "Sunday night",
        startTime = "23:00",
        endTime = "06:10",
        duration = "7h 25m",
        onCloseClick = {},
    )
}
