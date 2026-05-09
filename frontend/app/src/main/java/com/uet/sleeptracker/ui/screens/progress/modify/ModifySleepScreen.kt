package com.uet.sleeptracker.ui.screens.progress.modify

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

data class EditableSleepRecord(
    val id: String,
    val title: String,
    val bedtimeMinutes: Int,
    val wakeTimeMinutes: Int,
    val dateLabel: String? = null
)

private enum class DragHandle {
    Bedtime,
    WakeUp
}

@Composable
fun ModifySleepScreen(
    record: EditableSleepRecord,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSave: (EditableSleepRecord) -> Unit = {}
) {
    var bedtimeMinutes by remember(record.id, record.bedtimeMinutes) {
        mutableIntStateOf(record.bedtimeMinutes)
    }
    var wakeTimeMinutes by remember(record.id, record.wakeTimeMinutes) {
        mutableIntStateOf(record.wakeTimeMinutes)
    }

    val updatedRecord = record.copy(
        bedtimeMinutes = bedtimeMinutes,
        wakeTimeMinutes = wakeTimeMinutes
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF240051),
                            Color(0xFF140129),
                            SleepBackground
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            NightSkyDecor()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(
                            0.5.dp,
                            Color.White.copy(alpha = 0.4f)
                        )
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                SleepDurationDial(
                    bedtimeMinutes = bedtimeMinutes,
                    wakeTimeMinutes = wakeTimeMinutes,
                    onBedtimeChange = { bedtimeMinutes = it },
                    onWakeTimeChange = { wakeTimeMinutes = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Modify your sleep",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SleepTimeInfoCard(
                        label = "Bedtime",
                        value = formatTime12h(bedtimeMinutes),
                        icon = "B",
                        modifier = Modifier.weight(1f)
                    )
                    SleepTimeInfoCard(
                        label = "Wake up",
                        value = formatTime12h(wakeTimeMinutes),
                        icon = "W",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onSave(updatedRecord) },
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10163A),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Save",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun NightSkyDecor() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val stars = listOf(
            Offset(size.width * 0.12f, size.height * 0.04f),
            Offset(size.width * 0.5f, size.height * 0.08f),
            Offset(size.width * 0.82f, size.height * 0.05f),
            Offset(size.width * 0.2f, size.height * 0.11f),
            Offset(size.width * 0.76f, size.height * 0.12f),
            Offset(size.width * 0.93f, size.height * 0.03f)
        )

        stars.forEachIndexed { index, star ->
            val radius = if (index % 2 == 0) 2.4f else 1.4f
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = radius,
                center = star
            )
            drawLine(
                color = Color.White.copy(alpha = 0.35f),
                start = Offset(star.x - 6f, star.y),
                end = Offset(star.x + 6f, star.y),
                strokeWidth = 1f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.35f),
                start = Offset(star.x, star.y - 6f),
                end = Offset(star.x, star.y + 6f),
                strokeWidth = 1f
            )
        }
    }
}

@Composable
private fun SleepDurationDial(
    bedtimeMinutes: Int,
    wakeTimeMinutes: Int,
    onBedtimeChange: (Int) -> Unit,
    onWakeTimeChange: (Int) -> Unit
) {
    var activeHandle by remember { mutableStateOf<DragHandle?>(null) }

    Box(
        modifier = Modifier
            .size(286.dp)
            .pointerInput(bedtimeMinutes, wakeTimeMinutes) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = min(size.width, size.height) * 0.39f
                        val bedtimeOffset = polarToOffset(center, radius, bedtimeMinutes)
                        val wakeOffset = polarToOffset(center, radius, wakeTimeMinutes)
                        val bedtimeDistance = offset.minus(bedtimeOffset).getDistance()
                        val wakeDistance = offset.minus(wakeOffset).getDistance()
                        activeHandle = if (bedtimeDistance <= wakeDistance) {
                            DragHandle.Bedtime
                        } else {
                            DragHandle.WakeUp
                        }
                    },
                    onDragEnd = { activeHandle = null },
                    onDragCancel = { activeHandle = null }
                ) { change, _ ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val minutes = angleToMinutes(change.position, center)
                    when (activeHandle) {
                        DragHandle.Bedtime -> onBedtimeChange(minutes)
                        DragHandle.WakeUp -> onWakeTimeChange(minutes)
                        null -> Unit
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val ringSize = min(size.width, size.height) * 0.78f
            val ringTopLeft = Offset(center.x - ringSize / 2f, center.y - ringSize / 2f)
            val strokeWidth = ringSize * 0.13f
            val activeSweep = sleepDurationMinutes(bedtimeMinutes, wakeTimeMinutes) / 1440f * 360f

            drawArc(
                color = Color(0xFF1A1E45),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = ringTopLeft,
                size = Size(ringSize, ringSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color(0xFF5A11C5), Color(0xFFB53FFE), Color(0xFF5A11C5)),
                    center = center
                ),
                startAngle = minutesToDrawAngle(bedtimeMinutes),
                sweepAngle = activeSweep,
                useCenter = false,
                topLeft = ringTopLeft,
                size = Size(ringSize, ringSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            val innerSize = ringSize * 0.7f
            drawCircle(
                color = Color(0xFF0F0422),
                radius = innerSize / 2f,
                center = center
            )

            drawCircle(
                color = Color(0xFF3E1B78),
                radius = innerSize / 2f,
                center = center,
                style = Stroke(width = 2f)
            )

            repeat(48) { index ->
                val minute = index * 30
                val angleRad = minutesToRadians(minute)
                val tickStart = Offset(
                    x = center.x + (cos(angleRad) * (innerSize * 0.47f)).toFloat(),
                    y = center.y + (sin(angleRad) * (innerSize * 0.47f)).toFloat()
                )
                val tickEnd = Offset(
                    x = center.x + (cos(angleRad) * (innerSize * 0.5f)).toFloat(),
                    y = center.y + (sin(angleRad) * (innerSize * 0.5f)).toFloat()
                )
                drawLine(
                    color = Color(0xFF6E4AAE).copy(alpha = if (index % 2 == 0) 0.6f else 0.35f),
                    start = tickStart,
                    end = tickEnd,
                    strokeWidth = if (index % 2 == 0) 2f else 1f
                )
            }

            val labelRadius = innerSize * 0.62f
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                    alpha = 170
                }
                drawText("12", center.x, center.y - labelRadius + 5f, paint)
                drawText("3", center.x + labelRadius, center.y + 5f, paint)
                drawText("6", center.x, center.y + labelRadius + 5f, paint)
                drawText("9", center.x - labelRadius, center.y + 5f, paint)
            }

            val handleRadius = ringSize * 0.39f
            val bedtimeOffset = polarToOffset(center, handleRadius, bedtimeMinutes)
            val wakeOffset = polarToOffset(center, handleRadius, wakeTimeMinutes)

            drawHandle(bedtimeOffset, Color(0xFF7E2FFF), "B")
            drawHandle(wakeOffset, Color(0xFF7E2FFF), "W")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatDuration(sleepDurationMinutes(bedtimeMinutes, wakeTimeMinutes)),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sleep duration",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHandle(
    center: Offset,
    color: Color,
    label: String
) {
    drawCircle(color = Color.White, radius = 16f, center = center)
    drawCircle(color = color, radius = 14f, center = center)
    drawContext.canvas.nativeCanvas.drawText(
        label,
        center.x,
        center.y + 5f,
        android.graphics.Paint().apply {
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 14.sp.toPx()
            this.color = android.graphics.Color.WHITE
            isFakeBoldText = true
        }
    )
}

@Composable
private fun SleepTimeInfoCard(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF11163D)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = icon,
                    color = Color(0xFF747787),
                    fontSize = 11.sp
                )
                Text(
                    text = label,
                    color = Color(0xFF747787),
                    fontSize = 11.sp
                )
            }
            Text(
                text = value,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun minutesToDrawAngle(minutes: Int): Float = (minutes / 1440f * 360f) - 90f

private fun minutesToRadians(minutes: Int): Double = (minutes / 1440.0) * (2 * PI) - PI / 2

private fun polarToOffset(center: Offset, radius: Float, minutes: Int): Offset {
    val angle = minutesToRadians(minutes)
    return Offset(
        x = center.x + cos(angle).toFloat() * radius,
        y = center.y + sin(angle).toFloat() * radius
    )
}

private fun angleToMinutes(position: Offset, center: Offset): Int {
    val angle = atan2(position.y - center.y, position.x - center.x)
    var normalized = angle + PI / 2
    if (normalized < 0) normalized += PI * 2
    val minutes = ((normalized / (PI * 2)) * 1440.0).roundToInt()
    return snapMinutes(minutes % 1440)
}

private fun snapMinutes(minutes: Int): Int {
    val snapped = (minutes / 5f).roundToInt() * 5
    return ((snapped % 1440) + 1440) % 1440
}

private fun sleepDurationMinutes(bedtimeMinutes: Int, wakeTimeMinutes: Int): Int {
    val normalizedWake = if (wakeTimeMinutes <= bedtimeMinutes) wakeTimeMinutes + 1440 else wakeTimeMinutes
    return normalizedWake - bedtimeMinutes
}

private fun formatDuration(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return "${hours}hr ${minutes}m"
}

private fun formatTime12h(minutes: Int): String {
    val normalized = ((minutes % 1440) + 1440) % 1440
    val hour24 = normalized / 60
    val minute = normalized % 60
    val amPm = if (hour24 < 12) "AM" else "PM"
    val hour12 = when (val hour = hour24 % 12) {
        0 -> 12
        else -> hour
    }
    return "%02d : %02d %s".format(hour12, minute, amPm)
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun ModifySleepScreenPreview() {
    SleeptrackerTheme {
        ModifySleepScreen(
            record = EditableSleepRecord(
                id = "mon-2026-05-05",
                title = "Last night (Monday)",
                bedtimeMinutes = 23 * 60,
                wakeTimeMinutes = 6 * 60 + 12
            )
        )
    }
}
