package com.uet.sleeptracker.ui.screens.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun NotificationMenuScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            NotificationMenuTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                NotificationMenuCard(
                    title = stringResource(R.string.notification_lumi_title),
                    description = stringResource(R.string.notification_lumi_desc)
                )
            }
            item {
                NotificationMenuCard(
                    title = stringResource(R.string.notification_lumi_tools_title),
                    description = stringResource(R.string.notification_lumi_tools_desc)
                )
            }
            item {
                NotificationMenuCard(
                    title = stringResource(R.string.notification_energy_title),
                    description = stringResource(R.string.notification_energy_desc)
                )
            }
        }
    }
}

@Composable
private fun NotificationMenuTopBar(onBackClick: () -> Unit) {
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
            text = stringResource(R.string.notification_menu_title),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NotificationMenuCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SleepSurface,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = SleepOnSurfaceVariant,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationMenuScreenPreview() {
    SleeptrackerTheme {
        NotificationMenuScreen()
    }
}
