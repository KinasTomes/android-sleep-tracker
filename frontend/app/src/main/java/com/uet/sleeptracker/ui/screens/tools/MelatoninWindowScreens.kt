package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.HabitReminderSettingsScreen
import com.uet.sleeptracker.ui.components.HabitSettingsContent
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val MelatoninBackground = Color(0xFF111111)
private val MelatoninDivider = Color(0xFF1A1A1D)

@Composable
fun MelatoninWindowScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MelatoninBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MelatoninBackground)
        ) {
            ToolDetailTopBar(
                title = "Melatonin Window",
                onBackClick = onCloseClick,
                type = 1,
            )

            Row(
                modifier = Modifier.padding(start = 21.dp, top = 39.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.melatonin_window_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(52.dp)
                )
                Column(modifier = Modifier.padding(start = 19.dp)) {
                    Text(text = "Melatonin Window", color = Color.White, fontSize = 20.sp)
                    Text(
                        text = "Reminder",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 18.dp, top = 47.dp)
            ) {
                val backgroundStart = 54.dp
                val backgroundEndPadding = 4.dp
                val backgroundWidth = maxWidth - backgroundStart - backgroundEndPadding
                val backgroundHeight = backgroundWidth / 2.005f
                val backgroundTop = 24.dp

                Image(
                    painter = painterResource(R.drawable.melatonin_window_background),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .offset(x = backgroundStart, y = backgroundTop)
                        .width(backgroundWidth)
                        .height(backgroundHeight)
                )

                MelatoninTimeRuler(
                    backgroundTop = backgroundTop,
                    backgroundHeight = backgroundHeight,
                    modifier = Modifier
                        .width(82.dp)
                        .height(backgroundTop + backgroundHeight + 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(132.dp))
            HorizontalDivider(color = MelatoninDivider, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(69.dp)
                    .clickable(onClick = onSettingsClick)
                    .padding(horizontal = 21.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Settings", color = Color.White, fontSize = 16.sp)
                Icon(
                    painter = painterResource(R.drawable.right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            HorizontalDivider(color = MelatoninDivider, thickness = 1.dp)
        }
    }
}

@Composable
private fun MelatoninTimeRuler(
    backgroundTop: Dp,
    backgroundHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(MelatoninBackground)
    ) {
        MelatoninRulerTime(
            time = "21:45",
            modifier = Modifier.offset(y = backgroundTop - 12.dp)
        )
        MelatoninRulerTime(
            time = "22:45",
            modifier = Modifier.offset(y = backgroundTop + backgroundHeight - 12.dp)
        )

        MelatoninRulerTick(
            modifier = Modifier.offset(y = backgroundTop - 19.5.dp)
        )
        repeat(7) { index ->
            val fraction = (index + 1) / 8f
            MelatoninRulerTick(
                modifier = Modifier.offset(
                    y = backgroundTop + (backgroundHeight * fraction) - 1.5.dp
                )
            )
        }
        MelatoninRulerTick(
            modifier = Modifier.offset(y = backgroundTop + backgroundHeight + 16.5.dp)
        )
    }
}

@Composable
private fun MelatoninRulerTime(
    time: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = time,
        color = Color.White,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        modifier = modifier,
    )
}

@Composable
private fun MelatoninRulerTick(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(3.dp)
                .background(Color(0xFF721BFD))
        )
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .width(46.dp)
                .height(3.dp)
                .background(Color(0xFF838383))
        )
    }
}

@Composable
fun MelatoninWindowSettingsScreen(
    modifier: Modifier = Modifier,
    reminderTime: String = "15 minutes",
    initialRemindersEnabled: Boolean = true,
    onRemindersEnabledChange: (Boolean) -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Melatonin Window",
            toggleTitle = "Remind me of daily melatonin",
            toggleSubtitle = "window times and changes",
            reminderValue = reminderTime,
            initialEnabled = initialRemindersEnabled,
            showActions = false,
        ),
        onCloseClick = onCloseClick,
        onCheckedChange = onRemindersEnabledChange,
        onReminderTimeClick = onReminderTimeClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun MelatoninWindowScreenPreview() {
    SleeptrackerTheme {
        MelatoninWindowScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun MelatoninWindowSettingsScreenPreview() {
    SleeptrackerTheme {
        MelatoninWindowSettingsScreen()
    }
}
