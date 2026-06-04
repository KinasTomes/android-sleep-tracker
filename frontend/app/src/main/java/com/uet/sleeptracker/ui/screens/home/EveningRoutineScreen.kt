package com.uet.sleeptracker.ui.screens.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryButton
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
private val EveningRoutineScreenColor = Color(0xFF121212)
private val EveningRoutineDividerColor = Color(0xFF1A1A1D)
private val EveningRoutineMutedTextColor = Color(0xFFBABABA)

@Composable
fun EveningRoutineScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onAddActivitiesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLearnMoreClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = EveningRoutineScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(EveningRoutineScreenColor)
        ) {
            PeaksTopBar(
                title = "Evening Routine",
                trailingContent = {
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2C2C2E))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )

            EveningRoutineHero()

            HorizontalDivider(color = EveningRoutineDividerColor, thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 21.dp, end = 21.dp, top = 48.dp)
            ) {
                Text(
                    text = "Think less at the end of the day",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Add activities for a typical weekday night and we'll organize your evening to match your daily energy levels.",
                    color = EveningRoutineMutedTextColor,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
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
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(88.dp))

            HorizontalDivider(color = EveningRoutineDividerColor, thickness = 1.dp)
            EveningRoutineMenuRow(
                label = "Settings",
                onClick = onSettingsClick
            )
            HorizontalDivider(color = EveningRoutineDividerColor, thickness = 1.dp)
            EveningRoutineMenuRow(
                label = "Learn more",
                onClick = onLearnMoreClick
            )
            HorizontalDivider(color = EveningRoutineDividerColor, thickness = 1.dp)
        }
    }
}

@Composable
private fun EveningRoutineHero() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(113.dp)
            .padding(start = 21.dp, end = 21.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.evening_routine_icon),
                contentDescription = "Evening routine",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = "Evening Routine",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun EveningRoutineMenuRow(
    label: String,
    onClick: () -> Unit,
) {
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
            text = label,
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
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 402, heightDp = 874)
@Composable
fun EveningRoutineScreenPreview() {
    SleeptrackerTheme {
        EveningRoutineScreen()
    }
}
