package com.uet.sleeptracker.ui.screens.tools

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val SleepQualityScreenColor = Color(0xFF121212)
private val SleepQualityMutedTextColor = Color(0xFFBABABA)
private val SleepQualityDividerColor = Color(0xFF1A1A1D)

@Composable
fun SleepQualityScreen(
    modifier: Modifier = Modifier,
    reminderTimeText: String = "1h 30m",
    onCloseClick: () -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
) {
    var selectedMoodIndex by remember { mutableIntStateOf(2) }
    var reminderEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepQualityScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SleepQualityScreenColor)
        ) {
            ToolDetailTopBar(
                title = "Sleep Quality",
                onBackClick = onCloseClick,
                type = 1
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SleepQualityHero()

                SleepQualityMoodSelector(
                    selectedIndex = selectedMoodIndex,
                    onSelectedIndexChange = { selectedMoodIndex = it },
                    modifier = Modifier.padding(top = 34.dp)
                )

                Text(
                    text = "Recent check-ins",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 21.dp, top = 46.dp, bottom = 34.dp)
                )

                HorizontalDivider(color = SleepQualityDividerColor, thickness = 1.dp)

                SleepQualityRecentCheckIns()

                HorizontalDivider(color = SleepQualityDividerColor, thickness = 1.dp)

                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 21.dp, top = 48.dp, bottom = 36.dp)
                )

                HorizontalDivider(color = SleepQualityDividerColor, thickness = 1.dp)

                SleepQualityReminderToggleRow(
                    checked = reminderEnabled,
                    onCheckedChange = { reminderEnabled = it }
                )

                SleepQualityReminderTimeRow(
                    value = reminderTimeText,
                    onClick = onReminderTimeClick
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun SleepQualityHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0C0C0A),
                        Color(0xFF2B0E1B),
                        Color(0xFF3C1125)
                    )
                )
            )
            .padding(start = 21.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.sleep_quality_icon),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 64.dp)
                .size(48.dp)
        )

        Text(
            text = "Sleep Quality:",
            color = Color.White,
            fontSize = 24.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 123.dp)
        )

        Text(
            text = "Today - Tuesday, June 9th",
            color = Color.White,
            fontSize = 20.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 160.dp)
        )
    }
}

@Composable
private fun SleepQualityMoodSelector(
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconIds = listOf(
        R.drawable.face_poor,
        R.drawable.face_notgood,
        R.drawable.face_neutral,
        R.drawable.face_good,
        R.drawable.face_awesome
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF333333)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconIds.forEachIndexed { index, iconId ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(if (index == selectedIndex) SleepPrimary else Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onSelectedIndexChange(index) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 37.dp, end = 41.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Poor",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "Awesome",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun SleepQualityRecentCheckIns() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(186.dp)
            .padding(start = 21.dp, end = 21.dp, top = 26.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(id = R.drawable.stack_icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "How many days in a row can you\ncomplete this habit? You’ve got this!",
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 23.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("WED", "THU", "FRI", "SAT", "SUN", "MON", "TUE").forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF414141))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = day,
                        color = Color(0xFF414141),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun SleepQualityReminderToggleRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
                text = "Morning reminder",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "after sleep inertia",
                color = SleepQualityMutedTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        SleepQualitySwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }

    HorizontalDivider(color = SleepQualityDividerColor, thickness = 1.dp)
}

@Composable
private fun SleepQualityReminderTimeRow(
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(73.dp)
            .clickable(onClick = onClick)
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
                    text = "Reminder Time",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "After waketime",
                    color = SleepQualityMutedTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Text(
            text = value,
            color = SleepQualityMutedTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }

    HorizontalDivider(color = SleepQualityDividerColor, thickness = 1.dp)
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun SleepQualitySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "SleepQualitySwitchOffset"
    )

    Box(
        modifier = modifier
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
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF7EC7C0) else Color(0xFF76767B))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 402, heightDp = 1000)
@Composable
private fun SleepQualityScreenPreview() {
    SleeptrackerTheme {
        SleepQualityScreen()
    }
}
