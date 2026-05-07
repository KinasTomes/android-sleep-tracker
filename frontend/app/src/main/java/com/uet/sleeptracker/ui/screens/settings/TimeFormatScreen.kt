package com.uet.sleeptracker.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun TimeFormatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    initialFormat24h: Boolean = true,
    onFormatChange: (Boolean) -> Unit = {}
) {
    var is24h by remember { mutableStateOf(initialFormat24h) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            TimeFormatTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            TimeFormatSectionHeader(title = stringResource(R.string.time_format_preferences_header))
            
            TimeFormatItem(
                label = stringResource(R.string.time_format_12h),
                isSelected = !is24h,
                onClick = {
                    is24h = false
                    onFormatChange(false)
                }
            )
            
            TimeFormatItem(
                label = stringResource(R.string.time_format_24h),
                isSelected = is24h,
                onClick = {
                    is24h = true
                    onFormatChange(true)
                }
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.time_format_desc_1),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = stringResource(R.string.time_format_desc_2),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = stringResource(R.string.time_format_desc_3),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
private fun TimeFormatTopBar(onBackClick: () -> Unit) {
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
            text = stringResource(R.string.time_format_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TimeFormatSectionHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF2C2C2E))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF000000))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = title,
                color = SleepOnSurfaceVariant,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF2C2C2E))
    }
}

@Composable
private fun TimeFormatItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f)
            )
            
            TimeFormatRadioButton(isSelected = isSelected)
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(start = 16.dp),
            thickness = 0.5.dp,
            color = Color(0xFF2C2C2E)
        )
    }
}

@Composable
private fun TimeFormatRadioButton(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.White else Color(0xFF3A3A3C),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeFormatScreenPreview() {
    SleeptrackerTheme {
        TimeFormatScreen()
    }
}
