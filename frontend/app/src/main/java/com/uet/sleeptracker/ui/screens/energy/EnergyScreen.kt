package com.uet.sleeptracker.ui.screens.energy

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import com.uet.sleeptracker.R

@Composable
fun EnergyScreen(
    modifier: Modifier = Modifier
) {
    EnergyScheduleScreen(
        modifier = modifier,
        profileIcon = ColorPainter(Color.Transparent),
        lightningIcon = painterResource(id = R.drawable.lightning),
        sleepIcon = painterResource(id = R.drawable.zzz),
        editIcon = painterResource(id = R.drawable.scissor),
        windDownImage = painterResource(id = R.drawable.evening),
        caffeineImage = painterResource(id = R.drawable.ceffeine),
        showTopBar = false
    )
}
