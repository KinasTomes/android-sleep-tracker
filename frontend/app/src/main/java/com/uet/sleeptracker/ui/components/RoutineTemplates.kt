package com.uet.sleeptracker.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R

private val RoutineBackground = Color(0xFF121212)
private val RoutineDivider = Color(0xFF1A1A1D)
private val RoutineMuted = Color(0xFFBABABA)

data class RoutineDetailContent(
    val title: String,
    val phaseLabel: String? = null,
    val headline: String,
    val description: String,
    @param:DrawableRes val iconRes: Int,
)

@Composable
fun RoutineDetailScreen(
    content: RoutineDetailContent,
    onCloseClick: () -> Unit,
    onAddActivitiesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RoutineBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(RoutineBackground)
        ) {
            ToolDetailTopBar(
                title = content.title,
                onBackClick = onCloseClick,
                type = 1,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(113.dp)
                    .padding(horizontal = 21.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(content.iconRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                Spacer(modifier = Modifier.width(22.dp))

                Column {
                    Text(
                        text = content.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    content.phaseLabel?.let { phaseLabel ->
                        Text(
                            text = phaseLabel,
                            color = RoutineMuted,
                            fontSize = 20.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }

            HorizontalDivider(color = RoutineDivider, thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, top = 48.dp)
            ) {
                Text(
                    text = content.headline,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = content.description,
                    color = RoutineMuted,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                )
                Spacer(modifier = Modifier.height(52.dp))
                SleepPrimaryButton(
                    text = "ADD ACTIVITIES",
                    onClick = onAddActivitiesClick,
                    modifier = Modifier.fillMaxWidth(),
                    height = 74.dp,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(88.dp))

            HorizontalDivider(color = RoutineDivider, thickness = 1.dp)
            RoutineMenuRow(label = "Settings", onClick = onSettingsClick)
            HorizontalDivider(color = RoutineDivider, thickness = 1.dp)
            RoutineMenuRow(label = "Learn more", onClick = onLearnMoreClick)
            HorizontalDivider(color = RoutineDivider, thickness = 1.dp)
        }
    }
}

@Composable
fun RoutineSettingsScreen(
    title: String,
    toggleTitle: String,
    reminderSubtitle: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var enabled by remember(title) { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RoutineBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(RoutineBackground)
        ) {
            ToolDetailTopBar(
                title = title,
                onBackClick = onCloseClick,
                type = 1,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .background(RoutineBackground)
            ) {
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 24.dp,
                        bottom = 24.dp,
                    ),
                )
                HabitReminderSettingsRows(
                    toggleTitle = toggleTitle,
                    toggleSubtitle = "Send daily reminders",
                    reminderValue = "",
                    reminderSubtitle = reminderSubtitle,
                    checked = enabled,
                    onCheckedChange = { enabled = it },
                    onReminderTimeClick = null,
                )
            }
        }
    }
}

@Composable
private fun RoutineMenuRow(
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 21.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )
        Icon(
            painter = painterResource(R.drawable.right),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp),
        )
    }
}
