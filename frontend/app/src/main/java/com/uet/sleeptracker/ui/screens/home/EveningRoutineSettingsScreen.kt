package com.uet.sleeptracker.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R

private val EveningRoutineSettingsScreenColor = Color(0xFF121212)
private val EveningRoutineSettingsMutedTextColor = Color(0xFFBABABA)
private val EveningRoutineSettingsDividerColor = Color(0xFF1A1A1D)

@Composable
fun EveningRoutineSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
) {
    var remindersEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = EveningRoutineSettingsScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(EveningRoutineSettingsScreenColor)
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .background(EveningRoutineSettingsScreenColor)
            ) {
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                EveningRoutineSettingsGroup(
                    checked = remindersEnabled,
                    onCheckedChange = { remindersEnabled = it }
                )
            }
        }
    }
}

@Composable
private fun EveningRoutineSettingsGroup(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = EveningRoutineSettingsDividerColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(start = 21.dp, end = 21.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Deliver my evening routine",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Send daily reminders",
                    color = EveningRoutineSettingsMutedTextColor,
                    fontSize = 14.sp
                )
            }

            EveningRoutineSettingsSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }

        HorizontalDivider(color = EveningRoutineSettingsDividerColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
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
                        text = "Energy peak, wind-down",
                        color = EveningRoutineSettingsMutedTextColor,
                        fontSize = 14.sp
                    )
                }
            }
        }

        HorizontalDivider(color = EveningRoutineSettingsDividerColor, thickness = 1.dp)
    }
}

@Composable
private fun EveningRoutineSettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "EveningRoutineSettingsSwitchOffset"
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
