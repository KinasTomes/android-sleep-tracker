package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val BrainDumpSettingsBackground = Color(0xFF121212)
private val BrainDumpSettingsDivider = Color(0xFF1A1A1D)
private val BrainDumpSettingsMuted = Color(0xFFBABABA)

@Composable
fun BrainDumpSettingsScreen(
    modifier: Modifier = Modifier,
    morningSummaryEnabled: Boolean = true,
    eveningReminderEnabled: Boolean = true,
    reminderTimeText: String = "1h 30m",
    onMorningSummaryEnabledChange: (Boolean) -> Unit = {},
    onEveningReminderEnabledChange: (Boolean) -> Unit = {},
    onCloseClick: () -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BrainDumpSettingsBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BrainDumpSettingsBackground)
        ) {
            ToolDetailTopBar(
                title = "Brain Dump",
                onBackClick = onCloseClick,
                type = 1
            )

            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 72.dp, bottom = 28.dp)
            )

            HorizontalDivider(color = BrainDumpSettingsDivider, thickness = 1.dp)

            BrainDumpToggleRow(
                title = "Morning summary",
                subtitle = "Send list summary after I wake up",
                checked = morningSummaryEnabled,
                onCheckedChange = onMorningSummaryEnabledChange
            )

            HorizontalDivider(color = BrainDumpSettingsDivider, thickness = 1.dp)

            BrainDumpReminderRow(
                title = "Reminder Time",
                subtitle = "After waketime",
                value = null,
                clickable = false,
                onClick = {}
            )

            HorizontalDivider(color = BrainDumpSettingsDivider, thickness = 1.dp)

            Spacer(modifier = Modifier.height(44.dp))

            HorizontalDivider(color = BrainDumpSettingsDivider, thickness = 1.dp)

            BrainDumpToggleRow(
                title = "Evening reminder",
                subtitle = "Send reminder to write brain dump",
                checked = eveningReminderEnabled,
                onCheckedChange = onEveningReminderEnabledChange
            )

            HorizontalDivider(color = BrainDumpSettingsDivider, thickness = 1.dp)

            BrainDumpReminderRow(
                title = "Reminder Time",
                subtitle = "Before melatonin window",
                value = reminderTimeText,
                clickable = true,
                onClick = onReminderTimeClick
            )

            HorizontalDivider(color = BrainDumpSettingsDivider, thickness = 1.dp)
        }
    }
}

@Composable
private fun BrainDumpToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(start = 21.dp, end = 42.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = BrainDumpSettingsMuted,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        BrainDumpSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun BrainDumpReminderRow(
    title: String,
    subtitle: String,
    value: String?,
    clickable: Boolean,
    onClick: () -> Unit,
) {
    val rowModifier = if (clickable) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(rowModifier)
            .height(74.dp)
            .padding(start = 21.dp, end = 21.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = BrainDumpSettingsMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        if (value != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = BrainDumpSettingsMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun BrainDumpSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "BrainDumpSwitchOffset"
    )

    Box(
        modifier = Modifier
            .width(36.dp)
            .height(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(16.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(8.dp))
                .background(if (checked) Color(0xFF00EDB9) else Color(0xFF2C2C2E))
        )

        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .size(20.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF7EC7C0) else Color(0xFF76767B))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 476, heightDp = 956)
@Composable
private fun BrainDumpSettingsScreenPreview() {
    SleeptrackerTheme {
        BrainDumpSettingsScreen()
    }
}
