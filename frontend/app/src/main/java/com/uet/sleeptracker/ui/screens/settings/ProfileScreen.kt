package com.uet.sleeptracker.ui.screens.settings

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            ProfileTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                ProfileHeader(onEditProfileClick = onEditProfileClick)
            }

            item {
                ProfileSectionHeader(title = stringResource(R.string.profile_section_my_sleep))
                ProfileListItem(
                    label = stringResource(R.string.profile_my_sleep_need),
                    value = "7h 45m"
                )
                ProfileListItem(
                    label = stringResource(R.string.profile_wake_time_goal),
                    value = "5:25a - 6:25a"
                )
            }

            item {
                ProfileSectionHeader(title = stringResource(R.string.profile_section_personalize))
                ProfileListItem(
                    label = stringResource(R.string.profile_data_sources),
                    value = "Today, 14:55"
                )
                ProfileListItem(
                    label = stringResource(R.string.profile_notifications),
                    value = stringResource(R.string.profile_enabled)
                )
                ProfileListItem(
                    label = stringResource(R.string.profile_calendars),
                    value = stringResource(R.string.profile_enabled)
                )
                ProfileListItem(
                    label = stringResource(R.string.profile_time_format),
                    value = stringResource(R.string.profile_24h_clock)
                )
                ProfileListItem(
                    label = stringResource(R.string.profile_language),
                    value = stringResource(R.string.profile_english)
                )
            }

            item {
                ProfileSectionHeader(title = stringResource(R.string.profile_section_membership))
                ProfileListItem(
                    label = stringResource(R.string.profile_subscription),
                    value = stringResource(R.string.profile_trial)
                )
                ProfileListItem(label = stringResource(R.string.profile_support))
                ProfileListItem(label = stringResource(R.string.profile_report_bug))
                ProfileListItem(label = stringResource(R.string.profile_terms_privacy))
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LogoutButton(onClick = onLogoutClick)
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun ProfileTopBar(onBackClick: () -> Unit) {
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
            text = stringResource(R.string.profile_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ProfileHeader(onEditProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFF9500), Color(0xFF5856D6))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(1.5.dp, Color(0xFF8E8E93), CircleShape)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(16.dp)
                            .border(
                                width = 1.5.dp,
                                color = Color(0xFF8E8E93),
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            )
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3A3A3C))
                    .border(2.dp, SleepBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-1).dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = stringResource(R.string.profile_edit_profile),
            color = Color(0xFF9D7BFF),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onEditProfileClick() }
        )
    }
}

@Composable
private fun ProfileSectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF151517))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = SleepOnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun ProfileListItem(
    label: String,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            
            if (value != null) {
                Text(
                    text = value,
                    color = SleepOnSurfaceVariant,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            
            Text(
                text = "›",
                color = SleepOnSurfaceVariant,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.offset(y = (-1).dp)
            )
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(start = 16.dp),
            thickness = 0.5.dp,
            color = Color(0xFF2C2C2E)
        )
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SleepPrimary)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 48.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_logout),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SleeptrackerTheme {
        ProfileScreen()
    }
}
