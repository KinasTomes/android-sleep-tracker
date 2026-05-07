package com.uet.sleeptracker.ui.screens.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepSurface
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            NotificationsTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                NotificationStatusCard()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.notifications_steps_title),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                NotificationStepItem(
                    icon = { SettingsIcon() },
                    text = stringResource(R.string.notifications_step_1)
                )
                Spacer(modifier = Modifier.height(20.dp))
                NotificationStepItem(
                    icon = { LumiIcon() },
                    text = stringResource(R.string.notifications_step_2)
                )
                Spacer(modifier = Modifier.height(20.dp))
                NotificationStepItem(
                    icon = { BellStepIcon() },
                    text = stringResource(R.string.notifications_step_3)
                )
                Spacer(modifier = Modifier.height(20.dp))
                NotificationStepItem(
                    icon = { ToggleIcon() },
                    text = stringResource(R.string.notifications_step_4)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.notifications_steps_footer),
                    color = SleepOnSurfaceVariant,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.notifications_stay_tuned_title),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.notifications_stay_tuned_desc),
                    color = SleepOnSurfaceVariant,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun NotificationsTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "‹",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.notifications_title),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NotificationStatusCard() {
    Surface(
        color = SleepSurface,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFF3A3A3C)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                BellGraphic(color = Color.White)
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                Text(
                    text = stringResource(R.string.notifications_card_title),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.notifications_card_enabled),
                    color = SleepOnSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun BellGraphic(color: Color) {
    Box(
        modifier = Modifier.size(20.dp, 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, color, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
                .background(color)
        )
    }
}

@Composable
private fun NotificationStepItem(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun SettingsIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8E8E93)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
            )
        }
    }
}

@Composable
private fun LumiIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6B4EE0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "LUMI",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun BellStepIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF3B30)),
        contentAlignment = Alignment.Center
    ) {
        BellGraphic(color = Color.White)
    }
}

@Composable
private fun ToggleIcon() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp, 18.dp)
                .clip(CircleShape)
                .background(Color(0xFF34C759))
                .padding(2.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    SleeptrackerTheme {
        NotificationsScreen()
    }
}
