 package com.uet.sleeptracker.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private val RulerMinorTickColor = Color(0xFF585858)
private val RulerMajorTickColor = Color.White
private const val RulerSnapMinutes = 5

@SuppressLint("DefaultLocale")
@Composable
fun SleepTimeRulerPicker(
    currentMinutes: Int,
    onTimeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minMinutes: Int = 3 * 60,
    maxMinutes: Int = 15 * 60,
) {
    val density = LocalDensity.current
    val pxPerMinute = with(density) { 20.dp.toPx() / 5f }
    val coroutineScope = rememberCoroutineScope()

    val initialMinutes = remember(currentMinutes, minMinutes, maxMinutes) {
        mapMinutesIntoRange(
            currentMinutes = currentMinutes,
            minMinutes = minMinutes,
            maxMinutes = maxMinutes
        )
    }

    var scrollOffset by remember { mutableFloatStateOf(initialMinutes * pxPerMinute) }
    var lastDragTime by remember { mutableLongStateOf(0L) }
    val animatedOffset = remember { Animatable(scrollOffset) }

    LaunchedEffect(currentMinutes, minMinutes, maxMinutes) {
        val mappedMinutes = mapMinutesIntoRange(
            currentMinutes = currentMinutes,
            minMinutes = minMinutes,
            maxMinutes = maxMinutes
        )
        val currentScrollMinutes = (scrollOffset / pxPerMinute).roundToInt()
        if (abs(currentScrollMinutes - mappedMinutes) > RulerSnapMinutes) {
            val targetOffset = mappedMinutes * pxPerMinute
            animatedOffset.snapTo(targetOffset)
            scrollOffset = targetOffset
        }
    }

    LaunchedEffect(scrollOffset, minMinutes, maxMinutes) {
        val dragTimestamp = System.currentTimeMillis()
        lastDragTime = dragTimestamp
        delay(150)

        if (System.currentTimeMillis() - lastDragTime >= 150) {
            val currentMinuteFloat = scrollOffset / pxPerMinute
            val snappedMinute = ((currentMinuteFloat / RulerSnapMinutes).roundToInt() * RulerSnapMinutes)
                .coerceIn(minMinutes, maxMinutes)
            val normalizedMinutes = normalizeMinutes(snappedMinute)
            val targetOffset = snappedMinute * pxPerMinute

            onTimeChange(normalizedMinutes)

            if (abs(targetOffset - scrollOffset) > 1f) {
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
        modifier = modifier
            .fillMaxWidth()
            .height(106.dp),
        contentAlignment = Alignment.Center
    ) {
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
                val centerX = width / 2f
                val currentOffset = animatedOffset.value
                val labelY = with(density) { 18.sp.toPx() }
                val tickTop = with(density) { 40.dp.toPx() }
                val tickBottom = tickTop + with(density) { 46.dp.toPx() }

                val startMinute = (((currentOffset - centerX) / pxPerMinute).toInt() - 15)
                    .coerceAtLeast(minMinutes)
                val endMinute = (((currentOffset + centerX) / pxPerMinute).toInt() + 15)
                    .coerceAtMost(maxMinutes)

                for (minute in startMinute..endMinute) {
                    if (minute % RulerSnapMinutes != 0) continue

                    val x = centerX + (minute * pxPerMinute) - currentOffset
                    val normalizedMinute = normalizeMinutes(minute)
                    val isHalfHour = normalizedMinute % 30 == 0

                    drawLine(
                        color = if (isHalfHour) RulerMajorTickColor else RulerMinorTickColor,
                        start = Offset(x, tickTop),
                        end = Offset(x, tickBottom),
                        strokeWidth = if (isHalfHour) 1.6.dp.toPx() else 1.dp.toPx()
                    )

                    if (isHalfHour) {
                        drawContext.canvas.nativeCanvas.drawText(
                            formatRulerTime(normalizedMinute),
                            x,
                            labelY,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 16.sp.toPx()
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .width(3.dp)
                .height(60.dp)
                .offset(y = 6.dp)
                .background(Color.Transparent, RoundedCornerShape(2.dp))
                .border(1.dp, SleepPrimary, RoundedCornerShape(2.dp))
        )
    }
}

private fun mapMinutesIntoRange(
    currentMinutes: Int,
    minMinutes: Int,
    maxMinutes: Int,
): Int {
    val normalized = normalizeMinutes(currentMinutes)
    return when {
        normalized in minMinutes..maxMinutes -> normalized
        normalized + 1440 in minMinutes..maxMinutes -> normalized + 1440
        normalized - 1440 in minMinutes..maxMinutes -> normalized - 1440
        else -> normalized.coerceIn(minMinutes, maxMinutes)
    }
}

private fun normalizeMinutes(minutes: Int): Int {
    return ((minutes % 1440) + 1440) % 1440
}

@SuppressLint("DefaultLocale")
private fun formatRulerTime(minutes: Int): String {
    val normalized = normalizeMinutes(minutes)
    val hour = normalized / 60
    val minute = normalized % 60
    return String.format("%02d:%02d", hour, minute)
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 476)
@Composable
private fun SleepTimeRulerPickerPreview() {
    SleeptrackerTheme {
        Box(
            modifier = Modifier
                .background(SleepBackground)
                .padding(horizontal = 12.dp, vertical = 24.dp)
        ) {
            SleepTimeRulerPicker(
                currentMinutes = 6 * 60,
                onTimeChange = {}
            )
        }
    }
}
