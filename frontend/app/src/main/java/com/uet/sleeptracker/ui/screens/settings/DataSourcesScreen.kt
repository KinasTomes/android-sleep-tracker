package com.uet.sleeptracker.ui.screens.settings

import androidx.compose.foundation.background
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
fun DataSourcesScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            DataSourcesTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ConnectedSection()
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
                HowItWorksSection()
            }
            
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun DataSourcesTopBar(onBackClick: () -> Unit) {
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
            text = stringResource(R.string.data_sources_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ConnectedSection() {
    Column {
        Text(
            text = stringResource(R.string.data_sources_connected),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.data_sources_connected_desc),
            color = SleepOnSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        DataSourceCard()
    }
}

@Composable
private fun DataSourceCard() {
    Surface(
        color = SleepSurface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF32283D), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Simplified Motion Icon
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF6E40C9).copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.data_sources_motion_activity),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.data_sources_phone_motion),
                    color = SleepOnSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.data_sources_last_used),
                    color = SleepOnSurfaceVariant,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Upload/Share Icon Placeholder
                Box(
                    modifier = Modifier.size(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "↑",
                        color = SleepOnSurfaceVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun HowItWorksSection() {
    Column {
        Text(
            text = stringResource(R.string.data_sources_how_it_works),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        HowItWorksItem(
            title = stringResource(R.string.data_sources_apple_health_title),
            description = stringResource(R.string.data_sources_apple_health_desc)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        HowItWorksItem(
            title = stringResource(R.string.data_sources_lumi_detection_title),
            description = stringResource(R.string.data_sources_lumi_detection_desc)
        )
    }
}

@Composable
private fun HowItWorksItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            color = SleepOnSurfaceVariant,
            fontSize = 15.sp,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DataSourcesScreenPreview() {
    SleeptrackerTheme {
        DataSourcesScreen()
    }
}
