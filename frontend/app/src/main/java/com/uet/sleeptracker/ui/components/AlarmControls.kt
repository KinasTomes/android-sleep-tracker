package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val AlarmDaySelectedBorder = Color(0xFF8F5AF4)
private val AlarmDayMutedBorder = Color(0xFF838383)
private val AlarmDayDeselectedBackground = Color(0xFF343434)
private val AlarmDayMutedBackground = Color(0xFF4F4F4F)
private val AlarmSwitchThumbOn = Color(0xFF8AD2CE)
private val AlarmSwitchThumbOff = Color(0xFFBABABA)
private val AlarmSwitchTrackOff = Color(0xFF363636)
private val AlarmDayChipShape = RoundedCornerShape(8.dp)

@Composable
fun AlarmDayChip(
    label: String,
    isDayActive: Boolean,
    isAlarmEnabled: Boolean,
    chipWidth: Dp,
    chipHeight: Dp,
    scale: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when {
        !isDayActive -> AlarmDayDeselectedBackground
        isAlarmEnabled -> SleepPrimary
        else -> AlarmDayMutedBackground
    }
    val borderColor = when {
        !isDayActive -> Color.Transparent
        isAlarmEnabled -> AlarmDaySelectedBorder
        else -> AlarmDayMutedBorder
    }

    Box(
        modifier = modifier
            .width(chipWidth)
            .height(chipHeight)
            .background(backgroundColor, AlarmDayChipShape)
            .then(
                if (isDayActive) {
                    Modifier.border(1.dp, borderColor, AlarmDayChipShape)
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = scaleSp(24.sp, scale),
            lineHeight = scaleSp(26.sp, scale),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun AlarmPillToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val trackColor = if (checked) SleepPrimary else AlarmSwitchTrackOff
    val thumbColor = if (checked) AlarmSwitchThumbOn else AlarmSwitchThumbOff
    val thumbOffsetX = if (checked) scaleDp(13.dp, scale) else 0.dp

    Box(
        modifier = modifier
            .width(scaleDp(33.dp, scale))
            .height(scaleDp(20.dp, scale))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(scaleDp(33.dp, scale))
                .height(scaleDp(12.dp, scale))
                .align(Alignment.Center)
                .background(trackColor, CircleShape)
        )

        Box(
            modifier = Modifier
                .size(scaleDp(20.dp, scale))
                .offset(x = thumbOffsetX)
                .background(thumbColor, CircleShape)
        )
    }
}

fun scaleDp(base: Dp, scale: Float): Dp = (base.value * scale).dp

fun scaleSp(base: androidx.compose.ui.unit.TextUnit, scale: Float) =
    (base.value * scale).sp

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AlarmDayChipPreview() {
    SleeptrackerTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Inactive day
            AlarmDayChip(
                label = "M",
                isDayActive = false,
                isAlarmEnabled = false,
                chipWidth = 40.dp,
                chipHeight = 40.dp,
                scale = 1f,
                onClick = {}
            )
            // Active day, alarm enabled
            AlarmDayChip(
                label = "T",
                isDayActive = true,
                isAlarmEnabled = true,
                chipWidth = 40.dp,
                chipHeight = 40.dp,
                scale = 1f,
                onClick = {}
            )
            // Active day, alarm disabled
            AlarmDayChip(
                label = "W",
                isDayActive = true,
                isAlarmEnabled = false,
                chipWidth = 40.dp,
                chipHeight = 40.dp,
                scale = 1f,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AlarmPillTogglePreview() {
    SleeptrackerTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlarmPillToggle(
                checked = false,
                onCheckedChange = {},
                scale = 1f
            )
            AlarmPillToggle(
                checked = true,
                onCheckedChange = {},
                scale = 1f
            )
        }
    }
}
