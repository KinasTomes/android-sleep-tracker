package com.uet.sleeptracker.ui.screens.onboarding.info

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingSleepScheduleScreen(
    onNavigateNext: (Int, Int, Int, Int) -> Unit,
    onBack: () -> Unit
) {
    // State for times (in minutes from 0 to 1439)
    var bedTimeMinutes by remember { mutableIntStateOf(23 * 60) } // Default 23:00
    var wakeTimeMinutes by remember { mutableIntStateOf(7 * 60) }  // Default 07:00

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.sleep_schedule_title),
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SleepBackground
                )
            )
        },
        containerColor = SleepBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.sleep_schedule_headline),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Selection Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF1C1C1E)
            ) {
                Column(modifier = Modifier.padding(vertical = 24.dp)) {
                    // 1. In Bed Section
                    TimePickerSection(
                        label = stringResource(id = R.string.sleep_schedule_in_bed),
                        icon = "🌙",
                        currentMinutes = bedTimeMinutes,
                        onTimeChange = { bedTimeMinutes = it }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 24.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )

                    // 2. Wake Up Section
                    TimePickerSection(
                        label = stringResource(id = R.string.sleep_schedule_wake_up),
                        icon = "☀️",
                        currentMinutes = wakeTimeMinutes,
                        onTimeChange = { wakeTimeMinutes = it }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = { 
                    // Return values
                    onNavigateNext(
                        bedTimeMinutes / 60, bedTimeMinutes % 60,
                        wakeTimeMinutes / 60, wakeTimeMinutes % 60
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, SleepPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(
                    text = stringResource(id = R.string.sleep_schedule_next),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

@Composable
fun TimePickerSection(
    label: String,
    icon: String,
    currentMinutes: Int,
    onTimeChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontSize = 22.sp
                    )
                )
            }
            
            val hour = currentMinutes / 60
            val min = currentMinutes % 60
            val startTime = formatTime(currentMinutes - 15)
            val endTime = formatTime(currentMinutes + 15)
            
            Text(
                text = "$startTime - $endTime",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TimeRulerPicker(
            currentMinutes = currentMinutes,
            onTimeChange = onTimeChange
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeRulerPicker(
    currentMinutes: Int,
    onTimeChange: (Int) -> Unit
) {
    val scrollState = rememberScrollState(initial = currentMinutes * 10) // 1 minute = 10 pixels
    
    // Sync scroll to minutes
    LaunchedEffect(scrollState.value) {
        val newMinutes = (scrollState.value / 10f).roundToInt() % 1440
        if (newMinutes != currentMinutes) {
            onTimeChange(if (newMinutes < 0) newMinutes + 1440 else newMinutes)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        // The Scrollable Ruler
        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScrollable(scrollState)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val centerY = height / 2
                val pxPerMinute = 10f
                
                val centerOffset = width / 2
                val scrollOffset = scrollState.value.toFloat()

                // Draw visible ticks
                val startMinute = ((scrollOffset - centerOffset) / pxPerMinute).toInt() - 60
                val endMinute = ((scrollOffset + centerOffset) / pxPerMinute).toInt() + 60

                for (min in startMinute..endMinute) {
                    val x = centerOffset + (min * pxPerMinute) - scrollOffset
                    
                    val normalizedMin = if (min < 0) (min % 1440 + 1440) % 1440 else min % 1440
                    
                    if (normalizedMin % 60 == 0) {
                        // Hour Tick
                        drawLine(
                            color = Color.White,
                            start = Offset(x, height * 0.3f),
                            end = Offset(x, height * 0.7f),
                            strokeWidth = 2.dp.toPx()
                        )
                        
                        // Time Label
                        drawContext.canvas.nativeCanvas.drawText(
                            String.format("%02d:00", normalizedMin / 60),
                            x,
                            height * 0.2f,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 14.sp.toPx()
                                alpha = 180
                            }
                        )
                    } else if (normalizedMin % 15 == 0) {
                        // 15 min tick
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = Offset(x, height * 0.4f),
                            end = Offset(x, height * 0.6f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
        }

        // Fixed Center Indicator (The Purple Box)
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 70.dp)
                .background(SleepPrimary.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                .border(1.dp, SleepPrimary.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Central line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.White)
            )
        }
    }
}

// Helper to handle horizontal scroll logic
@Composable
fun Modifier.horizontalScrollable(state: androidx.compose.foundation.ScrollState) = this.horizontalScroll(state)

@SuppressLint("DefaultLocale")
private fun formatTime(minutes: Int): String {
    val totalMinutes = if (minutes < 0) minutes + 1440 else minutes % 1440
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return String.format("%02d:%02d", h, m)
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun OnboardingSleepScheduleScreenPreview() {
    SleeptrackerTheme {
        OnboardingSleepScheduleScreen(onNavigateNext = {_,_,_,_ ->}, onBack = {})
    }
}
