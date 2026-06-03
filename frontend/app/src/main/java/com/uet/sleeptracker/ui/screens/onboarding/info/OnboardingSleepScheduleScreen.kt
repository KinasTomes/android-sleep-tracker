package com.uet.sleeptracker.ui.screens.onboarding.info

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlinx.coroutines.launch
import kotlin.math.abs
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
                        minMinutes = 20 * 60, // 20:00
                        maxMinutes = 28 * 60, // 04:00 (next day)
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
                        minMinutes = 4 * 60,  // 04:00
                        maxMinutes = 12 * 60, // 12:00
                        onTimeChange = { wakeTimeMinutes = it }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SleepPrimaryOutlinedButton(
                text = stringResource(id = R.string.sleep_schedule_next),
                onClick = {
                    onNavigateNext(
                        bedTimeMinutes / 60, bedTimeMinutes % 60,
                        wakeTimeMinutes / 60, wakeTimeMinutes % 60
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun TimePickerSection(
    label: String,
    icon: String,
    currentMinutes: Int,
    minMinutes: Int,
    maxMinutes: Int,
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
            minMinutes = minMinutes,
            maxMinutes = maxMinutes,
            onTimeChange = onTimeChange
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeRulerPicker(
    currentMinutes: Int,
    minMinutes: Int,
    maxMinutes: Int,
    onTimeChange: (Int) -> Unit
) {
    val pxPerMinute = 10f
    val snapInterval = 5 // Snap every 5 minutes
    val coroutineScope = rememberCoroutineScope()
    
    // Map initial currentMinutes to the range [minMinutes..maxMinutes]
    val initialMinutes = remember {
        val normalized = (currentMinutes % 1440 + 1440) % 1440
        if (normalized in minMinutes..maxMinutes) normalized
        else if (normalized + 1440 in minMinutes..maxMinutes) normalized + 1440
        else if (normalized - 1440 in minMinutes..maxMinutes) normalized - 1440
        else normalized
    }
    
    var scrollOffset by remember { mutableFloatStateOf(initialMinutes * pxPerMinute) }
    var lastDragTime by remember { mutableLongStateOf(0L) }
    
    // Animate to target position
    val animatedOffset = remember { Animatable(scrollOffset) }
    
    // Sync external changes of currentMinutes to scroll state
    LaunchedEffect(currentMinutes) {
        val normalized = (currentMinutes % 1440 + 1440) % 1440
        val mapped = if (normalized in minMinutes..maxMinutes) normalized
        else if (normalized + 1440 in minMinutes..maxMinutes) normalized + 1440
        else if (normalized - 1440 in minMinutes..maxMinutes) normalized - 1440
        else normalized
        
        val currentScrollMinutes = (scrollOffset / pxPerMinute).roundToInt()
        // If the difference is large (greater than snapInterval), it's an external update
        if (abs(currentScrollMinutes - mapped) > snapInterval) {
            val targetOffset = mapped * pxPerMinute
            animatedOffset.snapTo(targetOffset)
            scrollOffset = targetOffset
        }
    }
    
    // Check if we should snap (after user stops dragging)
    LaunchedEffect(scrollOffset) {
        val currentTime = System.currentTimeMillis()
        lastDragTime = currentTime
        
        kotlinx.coroutines.delay(150) // Wait for user to stop dragging
        
        if (System.currentTimeMillis() - lastDragTime >= 150) {
            // User has stopped dragging, snap to nearest 5-minute mark within bounds
            val currentMinuteFloat = scrollOffset / pxPerMinute
            val snappedMinute = ((currentMinuteFloat / snapInterval).roundToInt() * snapInterval)
                .coerceIn(minMinutes, maxMinutes)
            val normalizedMinutes = if (snappedMinute < 0) {
                (snappedMinute % 1440 + 1440) % 1440
            } else {
                snappedMinute % 1440
            }
            
            val targetOffset = snappedMinute * pxPerMinute
            
            onTimeChange(normalizedMinutes)
            
            if (abs(targetOffset - scrollOffset) > 1f) {
                // Animate to snapped position
                animatedOffset.animateTo(
                    targetValue = targetOffset,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                scrollOffset = animatedOffset.value
            }
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
                .scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollableState { delta ->
                        coroutineScope.launch {
                            val newOffset = (scrollOffset - delta).coerceIn(
                                minMinutes * pxPerMinute,
                                maxMinutes * pxPerMinute
                            )
                            scrollOffset = newOffset
                            animatedOffset.snapTo(newOffset)
                        }
                        delta
                    }
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                
                val centerOffset = width / 2
                val currentOffset = animatedOffset.value

                // Draw visible ticks within boundaries
                val startMinute = (((currentOffset - centerOffset) / pxPerMinute).toInt() - 60).coerceAtLeast(minMinutes)
                val endMinute = (((currentOffset + centerOffset) / pxPerMinute).toInt() + 60).coerceAtMost(maxMinutes)

                for (min in startMinute..endMinute) {
                    val x = centerOffset + (min * pxPerMinute) - currentOffset
                    
                    val normalizedMin = if (min < 0) (min % 1440 + 1440) % 1440 else min % 1440
                    
                    if (normalizedMin % 30 == 0) {
                        // 30-minute Tick (Dark/Bold) - from 35% to 75% of height
                        drawLine(
                            color = Color.White,
                            start = Offset(x, height * 0.35f),
                            end = Offset(x, height * 0.75f),
                            strokeWidth = 2.dp.toPx()
                        )
                        
                        // Time Label - moved higher
                        val hour = normalizedMin / 60
                        val minute = normalizedMin % 60
                        drawContext.canvas.nativeCanvas.drawText(
                            String.format("%02d:%02d", hour, minute),
                            x,
                            height * 0.15f,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 14.sp.toPx()
                                alpha = 180
                            }
                        )
                    } else if (normalizedMin % 5 == 0) {
                        // 5-minute tick (Light)
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = Offset(x, height * 0.45f),
                            end = Offset(x, height * 0.65f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
        }

        // Fixed Center Indicator (The Purple Box) - with padding around bars
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 60.dp)
                .offset(y = 5.dp) // Offset down to align with bars (bars center at 55% not 50%)
                .background(SleepPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                .border(2.dp, SleepPrimary.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Central line - same height as hour bars (40% of 100dp = 40dp)
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(2.dp)
                    .background(SleepPrimary)
            )
        }
    }
}

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
