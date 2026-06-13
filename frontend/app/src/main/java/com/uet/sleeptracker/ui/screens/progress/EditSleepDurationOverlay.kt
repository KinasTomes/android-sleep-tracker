package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.SleepTimeRulerPicker
import com.uet.sleeptracker.ui.theme.SleepPrimary
import java.util.Locale

private val DurationDialogBackground = Color(0xFF222328)
private val DurationDialogDivider = Color(0xFF111111)
private val BedtimeValueColor = Color(0xFF855FF0)
private val WakeTimeValueColor = Color(0xFFD34680)

@Composable
internal fun EditSleepDurationOverlay(
    initialStartMinutes: Int,
    initialEndMinutes: Int,
    onCancel: () -> Unit,
    onSave: (startMinutes: Int, endMinutes: Int) -> Unit,
) {
    var draftStartMinutes by remember(initialStartMinutes) {
        mutableIntStateOf(normalizeDayMinutes(initialStartMinutes))
    }
    var draftEndMinutes by remember(initialEndMinutes) {
        mutableIntStateOf(normalizeDayMinutes(initialEndMinutes))
    }
    val startRange = remember(initialStartMinutes) {
        (initialStartMinutes - 90)..(initialStartMinutes + 90)
    }
    val endRange = remember(initialEndMinutes) {
        (initialEndMinutes - 90)..(initialEndMinutes + 90)
    }

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
                    .height(396.dp)
                    .background(DurationDialogBackground, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp),
            ) {
                SleepTimeEditorSection(
                    label = "Fell Asleep",
                    currentMinutes = draftStartMinutes,
                    valueColor = BedtimeValueColor,
                    minMinutes = startRange.first,
                    maxMinutes = startRange.last,
                    onTimeChange = { draftStartMinutes = it },
                )

                SleepTimeEditorSection(
                    label = "Woke Up",
                    currentMinutes = draftEndMinutes,
                    valueColor = WakeTimeValueColor,
                    minMinutes = endRange.first,
                    maxMinutes = endRange.last,
                    onTimeChange = { draftEndMinutes = it },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                DurationDialogButton(
                    text = "CANCEL",
                    filled = false,
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                )
                DurationDialogButton(
                    text = "SAVE",
                    filled = true,
                    onClick = {
                        onSave(
                            normalizeDayMinutes(draftStartMinutes),
                            normalizeDayMinutes(draftEndMinutes),
                        )
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SleepTimeEditorSection(
    label: String,
    currentMinutes: Int,
    valueColor: Color,
    minMinutes: Int,
    maxMinutes: Int,
    onTimeChange: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(198.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(77.dp)
                .padding(horizontal = 21.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = formatClockTime(currentMinutes),
                color = valueColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = DurationDialogDivider,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(115.dp)
                .clipToBounds()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            SleepTimeRulerPicker(
                currentMinutes = currentMinutes,
                onTimeChange = onTimeChange,
                minMinutes = minMinutes,
                maxMinutes = maxMinutes,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = DurationDialogDivider,
        )
    }
}

@Composable
private fun DurationDialogButton(
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
            fontWeight = FontWeight.Normal,
        )
    }
}

private fun normalizeDayMinutes(minutes: Int): Int =
    ((minutes % (24 * 60)) + (24 * 60)) % (24 * 60)

private fun formatClockTime(minutes: Int): String {
    val normalized = normalizeDayMinutes(minutes)
    return String.format(
        Locale.US,
        "%02d:%02d",
        normalized / 60,
        normalized % 60,
    )
}
