package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EditSleepBackground),
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
                startTime = startTime,
                endTime = endTime,
                duration = duration,
            )

            Spacer(modifier = Modifier.height(NapsTopSpacing))

            HorizontalDivider(
                thickness = 1.dp,
                color = EditSleepDivider,
            )

            NapsRow()

            HorizontalDivider(
                thickness = 1.dp,
                color = EditSleepDivider,
            )

            Spacer(modifier = Modifier.height(80.dp))
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
) {
    val startMinutes = timelineMinutes(startTime)
    val endMinutes = timelineMinutes(endTime, afterMinutes = startMinutes)
    val timelineStartHour = floorHour(startMinutes) - 1
    val timelineEndHour = ceilHour(endMinutes) + 1
    val timelineHeight =
        EditSleepTimelineTopInset * 2 + EditSleepHourHeight * (timelineEndHour - timelineStartHour)
    val editorTop = EditSleepTimelineTopInset +
        EditSleepHourHeight * ((startMinutes - timelineStartHour * 60) / 60f)
    val editorHeight = (endMinutes - startMinutes)
        .coerceAtLeast(30)
        .let { minutes -> EditSleepHourHeight * (minutes / 60f) }
    val summaryTitleY = (editorTop - 48.dp).coerceAtLeast(8.dp)
    val summaryValueY = summaryTitleY + 20.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(timelineHeight),
    ) {
        TimeRuler(
            startHour = timelineStartHour,
            endHour = timelineEndHour,
            modifier = Modifier
                .width(EditSleepRulerWidth)
                .height(timelineHeight),
        )

        Text(
            text = "Total sleep time",
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            modifier = Modifier.offset(x = 71.dp, y = summaryTitleY),
        )

        Text(
            text = duration,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 16.sp,
            lineHeight = 18.sp,
            modifier = Modifier.offset(x = 71.dp, y = summaryValueY),
        )

        TotalSleepTimeOverlay(
            startTime = startTime,
            endTime = endTime,
            overlayHeight = editorHeight,
            timeFontSize = 14.sp,
            editFontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 71.dp, end = 18.dp)
                .offset(y = editorTop + 2.dp),
        )
    }
}

@Composable
private fun TimeRuler(
    startHour: Int,
    endHour: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(endHour - startHour) { hourIndex ->
                val hourTop =
                    EditSleepTimelineTopInset.toPx() + EditSleepHourHeight.toPx() * hourIndex
                listOf(0.25f, 0.5f, 0.75f).forEach { fraction ->
                    val tickWidth = if (fraction == 0.5f) 16.dp else 8.dp
                    val y = hourTop + EditSleepHourHeight.toPx() * fraction
                    drawLine(
                        color = Color(0xFFD9D9D9),
                        start = Offset(17.dp.toPx(), y),
                        end = Offset((17.dp + tickWidth).toPx(), y),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
            }
        }

        (startHour..endHour).forEachIndexed { index, hour ->
            val hourCenterY = EditSleepTimelineTopInset + EditSleepHourHeight * index
            Text(
                text = (hour % 24).toString().padStart(2, '0'),
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
private fun NapsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp, vertical = 28.dp)
            .height(72.dp)
            .background(EditSleepTopBar, RoundedCornerShape(8.dp))
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
            text = "15m",
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

private fun floorHour(minutes: Int): Int = minutes / 60

private fun ceilHour(minutes: Int): Int = (minutes + 59) / 60

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
