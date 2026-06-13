package com.uet.sleeptracker.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
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
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun MySleepNeedScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            MySleepNeedTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.sleep_need_header),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.sleep_need_based_on),
                color = Color(0xFF8E8E93),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            SleepNeedGauge(
                currentNeed = "7h 45m",
                progress = 0.375f
            )

            Spacer(modifier = Modifier.height(72.dp))

            Text(
                text = stringResource(R.string.sleep_need_what_is_title),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.sleep_need_what_is_desc),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 17.sp,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.sleep_need_lumi_title),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.sleep_need_lumi_desc),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 17.sp,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun MySleepNeedTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
        Text(
            text = stringResource(R.string.sleep_need_header),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SleepNeedGauge(
    currentNeed: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
        ) {
            Text(
                text = "7h",
                color = Color(0xFF8E8E93),
                fontSize = 11.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 28.dp)
            )

            Text(
                text = "9h",
                color = Color(0xFF8E8E93),
                fontSize = 11.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 28.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(BiasAlignment(horizontalBias = progress * 2 - 1, verticalBias = 1f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentNeed,
                    color = Color(0xFF6C63FF),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color.White.copy(alpha = 0.5f))
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF721BFD))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.65f)
                    .align(Alignment.Center)
                    .background(Color(0xFF3F00AB))
            ) {
                Text(
                    text = stringResource(R.string.sleep_need_most_people).uppercase(),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MySleepNeedScreenPreview() {
    SleeptrackerTheme {
        MySleepNeedScreen()
    }
}
