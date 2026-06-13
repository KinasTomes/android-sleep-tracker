package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private const val StepMinutes = 5
private const val BaseSleepNeedMinutes = 7 * 60 + 45
private const val GaugeMinMinutes = 7 * 60
private const val GaugeMaxMinutes = 9 * 60

@Composable
fun OnboardingEditSleepNeedScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var sleepNeedMinutes by rememberSaveable { mutableIntStateOf(BaseSleepNeedMinutes) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            EditSleepNeedTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SleepBackground)
                    .navigationBarsPadding()
                    .padding(horizontal = 28.dp, vertical = 18.dp)
            ) {
                SleepPrimaryOutlinedButton(
                    text = stringResource(R.string.sleep_need_edit_save),
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    height = 56.dp,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            EditSleepNeedGauge(
                currentMinutes = sleepNeedMinutes,
                onIncrease = { sleepNeedMinutes += StepMinutes },
                onDecrease = { sleepNeedMinutes -= StepMinutes },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = stringResource(R.string.sleep_need_edit_title),
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.sleep_need_edit_hint),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.sleep_need_edit_accuracy),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EditSleepNeedTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = stringResource(R.string.sleep_need_edit_header),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun EditSleepNeedGauge(
    currentMinutes: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overflowRangeMinutes = 30f
    val valueRangeMinutes = (GaugeMaxMinutes - GaugeMinMinutes).toFloat()
    val normalized = (currentMinutes - GaugeMinMinutes) / valueRangeMinutes
    val clamped = normalized.coerceIn(0f, 1f)
    val overflowFraction = when {
        currentMinutes < GaugeMinMinutes -> ((GaugeMinMinutes - currentMinutes) / overflowRangeMinutes).coerceIn(0f, 1f)
        currentMinutes > GaugeMaxMinutes -> ((currentMinutes - GaugeMaxMinutes) / overflowRangeMinutes).coerceIn(0f, 1f)
        else -> 0f
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()
        val totalWidthPx = with(density) { maxWidth.toPx() }
        val gaugeWidthFraction = 0.96f
        val gaugeWidthPx = totalWidthPx * gaugeWidthFraction
        val gaugeWidthDp = maxWidth * gaugeWidthFraction
        val horizontalPaddingDp = (maxWidth - gaugeWidthDp) / 2

        val sideButtonSize = 60.dp
        val barHeight = 26.dp
        val barTop = 154.dp
        val labelTop = 82.dp
        val maxOverflowOffset = 56.dp
        val labelStyle = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Normal
        )
        val labelText = formatSleepNeed(currentMinutes)

        val maxOverflowPx = with(density) { maxOverflowOffset.toPx() }
        val indicatorXInGauge = clamped * gaugeWidthPx
        val overflowShiftPx = when {
            currentMinutes < GaugeMinMinutes -> -(overflowFraction * maxOverflowPx)
            currentMinutes > GaugeMaxMinutes -> overflowFraction * maxOverflowPx
            else -> 0f
        }
        val lineXInGauge = (indicatorXInGauge + overflowShiftPx).coerceIn(0f, gaugeWidthPx - 2f)
        val lineXTotalPx = with(density) { horizontalPaddingDp.toPx() } + lineXInGauge

        val edgePadding = 6.dp
        val edgePaddingPx = with(density) { edgePadding.toPx() }
        val labelWidthPx = textMeasurer.measure(
            text = labelText,
            style = labelStyle
        ).size.width.toFloat()
        val minLabelX = edgePaddingPx
        val maxLabelX = totalWidthPx - labelWidthPx - edgePaddingPx
        val labelX = (lineXTotalPx - labelWidthPx / 2).coerceIn(minLabelX, maxLabelX)

        val lineOffsetDp = with(density) { lineXTotalPx.toDp() - 1.dp }
        val labelOffsetDp = with(density) { labelX.toDp() }
        val labelWidthDp = with(density) { labelWidthPx.toDp() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(x = labelOffsetDp)
                    .offset(y = labelTop)
                    .width(labelWidthDp)
            ) {
                Text(
                    text = labelText,
                    color = SleepPrimary,
                    style = labelStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(gaugeWidthFraction)
                    .height(barHeight)
                    .offset(y = barTop)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF6E1EF7))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.58f)
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF431184))
                ) {
                    Text(
                        text = stringResource(R.string.sleep_need_most_people),
                        color = Color.White.copy(alpha = 0.24f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = lineOffsetDp, y = labelTop + 32.dp)
                    .size(width = 2.dp, height = 65.5.dp)
                    .background(Color.White.copy(alpha = 0.9f))
            )

            Box(
                modifier = Modifier
                    .width(gaugeWidthDp)
                    .offset(y = barTop + 44.dp)
                    .align(Alignment.TopCenter)
            ) {
                GaugeBoundaryLabel(
                    text = stringResource(R.string.sleep_need_result_min),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                )
                GaugeBoundaryLabel(
                    text = stringResource(R.string.sleep_need_result_most_people_start),
                    modifier = Modifier.offset(x = gaugeWidthDp * (30f / 120f))
                )

                val label3Text = stringResource(R.string.sleep_need_result_most_people_end)
                val label3WidthDp = with(density) {
                    textMeasurer.measure(
                        text = label3Text,
                        style = TextStyle(fontSize = 14.sp)
                    ).size.width.toDp()
                }
                GaugeBoundaryLabel(
                    text = label3Text,
                    modifier = Modifier.offset(x = gaugeWidthDp * (100f / 120f) - label3WidthDp)
                )

                GaugeBoundaryLabel(
                    text = stringResource(R.string.sleep_need_result_max),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                )
            }

            GaugeAdjustButton(
                symbol = "−",
                size = sideButtonSize,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 4.dp, y = barTop - 17.dp),
                onClick = onDecrease
            )

            GaugeAdjustButton(
                symbol = "+",
                size = sideButtonSize,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = barTop - 17.dp),
                onClick = onIncrease
            )
        }
    }
}

@Composable
private fun GaugeAdjustButton(
    symbol: String,
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val opacity = remember { Animatable(1f) }

    Surface(
        modifier = modifier
            .size(size)
            .graphicsLayer(alpha = opacity.value),
        shape = CircleShape,
        color = SleepPrimary,
        shadowElevation = 0.dp
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    opacity.animateTo(0.5f, animationSpec = tween(100))
                    opacity.animateTo(1f, animationSpec = tween(150))
                }
                onClick()
            }
        ) {
            Text(
                text = symbol,
                color = Color.Black,
                fontSize = 42.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun GaugeBoundaryLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = Color(0xFF7A7A7F),
        fontSize = 14.sp
    )
}

private fun formatSleepNeed(totalMinutes: Int): String {
    val sign = if (totalMinutes < 0) "-" else ""
    val absoluteMinutes = kotlin.math.abs(totalMinutes)
    val hours = absoluteMinutes / 60
    val minutes = absoluteMinutes % 60
    return if (minutes == 0) {
        "$sign${hours}h"
    } else {
        "$sign${hours}h ${minutes}m"
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun OnboardingEditSleepNeedScreenPreview() {
    SleeptrackerTheme {
        OnboardingEditSleepNeedScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
