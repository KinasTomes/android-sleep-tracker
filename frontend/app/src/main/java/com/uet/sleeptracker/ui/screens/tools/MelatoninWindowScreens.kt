package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.HabitReminderSettingsScreen
import com.uet.sleeptracker.ui.components.HabitSettingsContent
import com.uet.sleeptracker.ui.components.ToolDetailTopBar

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 18.dp, top = 47.dp)
            ) {
                MelatoninTimeRuler()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = Color.White,
                                cornerRadius = CornerRadius(4.dp.toPx()),
                                style = Stroke(
                                    width = 1.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
                                )
                            )
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.melatonin_window_background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Icon(
                        painter = painterResource(R.drawable.bell),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(20.dp)
                    )
                }
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
private fun MelatoninTimeRuler() {
    Column(
        modifier = Modifier
            .width(80.dp)
            .height(237.dp)
    ) {
        Text(text = "21:45", color = Color.White, fontSize = 20.sp)
        repeat(7) { index ->
            Row(
                modifier = Modifier
                    .height(if (index == 6) 47.dp else 24.dp),
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
        Text(text = "22:45", color = Color.White, fontSize = 20.sp)
    }
}

@Composable
fun MelatoninWindowSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Melatonin Window",
            toggleTitle = "Remind me of daily melatonin",
            toggleSubtitle = "window times and changes",
            reminderValue = "1h 30m",
            showActions = false,
        ),
        onCloseClick = onCloseClick,
        onReminderTimeClick = onReminderTimeClick,
        modifier = modifier,
    )
}
