package com.uet.sleeptracker.ui.screens.tools

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.HabitReminderSettingsRows
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val CaffeineScreenColor = Color(0xFF121212)
private val CaffeineMutedTextColor = Color(0xFFBABABA)
private val CaffeineDividerColor = Color(0xFF1A1A1D)

@Composable
fun CaffeineCutoffScreen(
    modifier: Modifier = Modifier,
    reminderTimeText: String = "10h",
    onCloseClick: () -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
    onLearnMoreClick: () -> Unit = {},
) {
    var reminderEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaffeineScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CaffeineScreenColor)
        ) {
            ToolDetailTopBar(
                title = "Caffeine Cutoff",
                onBackClick = onCloseClick,
                type = 1
            )

            CaffeineHero()

            Text(
                text = "Reminder time",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 48.dp, bottom = 34.dp)
            )

            HabitReminderSettingsRows(
                toggleTitle = "Remind me",
                toggleSubtitle = "to avoid caffeine",
                reminderValue = reminderTimeText,
                reminderSubtitle = "Before melatonin window",
                checked = reminderEnabled,
                onCheckedChange = { reminderEnabled = it },
                onReminderTimeClick = onReminderTimeClick,
            )

            Spacer(modifier = Modifier.height(68.dp))

            CaffeineLearnMoreRow(onClick = onLearnMoreClick)
        }
    }
}

@Composable
private fun CaffeineHero() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(116.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF21160A),
                        Color(0xFF2B1A08),
                        Color(0xFF121212)
                    )
                )
            )
            .padding(horizontal = 21.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF7A3E08)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "☕",
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = "Caffeine Cutoff",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "11:50",
                color = Color(0xFF898883),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun CaffeineLearnMoreRow(onClick: () -> Unit) {
    HorizontalDivider(color = CaffeineDividerColor, thickness = 1.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .clickable(onClick = onClick)
            .padding(start = 21.dp, end = 21.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Learn more",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        Icon(
            painter = painterResource(id = R.drawable.right),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }

    HorizontalDivider(color = CaffeineDividerColor, thickness = 1.dp)
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 402, heightDp = 874)
@Composable
private fun CaffeineCutoffScreenPreview() {
    SleeptrackerTheme {
        CaffeineCutoffScreen()
    }
}
