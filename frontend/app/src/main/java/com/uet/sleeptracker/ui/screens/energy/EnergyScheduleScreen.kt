package com.uet.sleeptracker.ui.screens.energy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.AlarmTimePicker
import com.uet.sleeptracker.ui.components.MelatoninWindow

@Composable
fun EnergyScheduleScreen(
    modifier: Modifier = Modifier,
    profileIcon: Painter,
    lightningIcon: Painter,
    sleepIcon: Painter,
    editIcon: Painter,
    windDownImage: Painter,
    caffeineImage: Painter,
    energyPercent: String = "76%",
    sleepTimeText: String = "5h 35m",
    alarmTimeText: String = "06:00 Tomorrow",
    alarmActiveDays: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7),
    showTopBar: Boolean = true
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        val scale = maxWidth.value / 476f
        val hiddenTopBarHeight = if (showTopBar) 0f else 104f

        Box(
            modifier = Modifier
                .width(maxWidth)
                .height(((5000f - hiddenTopBarHeight) * scale).dp)
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0f)
                }
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-hiddenTopBarHeight).dp)
                        .width(476.dp)
                        .height(5000.dp)
                        .background(Color.Black)
                ) {
                    EnergyTimelineGridBackground()

                    if (showTopBar) {
                        EnergyTopBar(
                            profileIcon = profileIcon,
                            lightningIcon = lightningIcon,
                            energyPercent = energyPercent
                        )
                    }

                    Text(
                        text = "Total sleep time",
                        color = Color.White,
                        fontSize = 28.sp,
                        letterSpacing = (-1).sp,
                        modifier = Modifier.offset(x = 54.dp, y = 247.dp)
                    )

                    Text(
                        text = sleepTimeText,
                        color = Color(0xFF848484),
                        fontSize = 28.sp,
                        letterSpacing = (-1).sp,
                        modifier = Modifier.offset(x = 54.dp, y = 280.dp)
                    )

                    TotalSleepTimeOverlay(
                        modifier = Modifier.offset(x = 56.dp, y = 350.dp),
                        sleepIcon = sleepIcon,
                        editIcon = editIcon
                    )

                    EnergySectionCard(
                        modifier = Modifier.offset(x = 0.dp, y = 1023.dp),
                        title = "Grogginess",
                        height = 174.dp,
                        background = Color(0xB316151A)
                    )

                    EnergySectionCard(
                        modifier = Modifier.offset(x = 0.dp, y = 1332.dp),
                        title = "Morning Peak",
                        subtitle = "40m longer",
                        height = 726.dp,
                        dotColor = Color(0xFFFF5A8A)
                    )

                    EnergySectionCard(
                        modifier = Modifier.offset(x = 0.dp, y = 2061.dp),
                        title = "Afternoon dip",
                        subtitle = "12m earlier",
                        height = 505.dp,
                        dotColor = Color(0xFF721BFD)
                    )

                    EnergySectionCard(
                        modifier = Modifier.offset(x = (-9).dp, y = 2572.dp),
                        title = "Evening peak",
                        height = 470.dp,
                        width = 364.dp,
                        background = Color(0xFF333333)
                    )

                    EnergySectionCard(
                        modifier = Modifier.offset(x = 0.dp, y = 3048.dp),
                        title = "Wind-down",
                        subtitle = "2m earlier",
                        height = 156.dp,
                        dotColor = Color(0xFF721BFD)
                    )

                    EnergyMelatoninWindow(
                        modifier = Modifier.offset(x = 0.dp, y = 3210.dp)
                    )

                    Text(
                        text = "Sleep need",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.offset(x = 66.dp, y = 3349.dp)
                    )

                    Text(
                        text = "7h 45m",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.offset(x = 66.dp, y = 3374.dp)
                    )

                    ActivityCard(
                        modifier = Modifier.offset(x = 349.dp, y = 2014.dp),
                        image = caffeineImage,
                        title = "Avoid caffeine"
                    )

                    ActivityCard(
                        modifier = Modifier.offset(x = 349.dp, y = 2694.dp),
                        image = windDownImage,
                        title = "Peak activities"
                    )

                    ActivityCard(
                        modifier = Modifier.offset(x = 349.dp, y = 3062.dp),
                        image = windDownImage,
                        title = "Wind-down\nactivities"
                    )

                    ActivityCard(
                        modifier = Modifier.offset(x = 349.dp, y = 3229.dp),
                        image = windDownImage,
                        title = "Wind-down\nactivities"
                    )

                    WakeWindowBlock(
                        modifier = Modifier.offset(x = 0.dp, y = 4132.dp),
                        alarmTimeText = alarmTimeText,
                        activeDays = alarmActiveDays
                    )

                    EnergyTimelineRuler(
                        modifier = Modifier.zIndex(10f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EnergyTopBar(
    profileIcon: Painter,
    lightningIcon: Painter,
    energyPercent: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(Color(0xFF010101))
            .border(
                width = 1.dp,
                color = Color(0xFF383838)
            )
    ) {
        Text(
            text = "My energy schedule",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.offset(x = 19.dp, y = 23.dp)
        )

        Text(
            text = "Great",
            color = Color(0xFF848484),
            fontSize = 28.sp,
            modifier = Modifier.offset(x = 19.dp, y = 54.dp)
        )

        Image(
            painter = lightningIcon,
            contentDescription = null,
            modifier = Modifier
                .offset(x = 105.dp, y = 60.dp)
                .size(24.dp)
        )

        Text(
            text = energyPercent,
            color = Color(0xFF00EBAC),
            fontSize = 28.sp,
            modifier = Modifier.offset(x = 129.dp, y = 54.dp)
        )

        Image(
            painter = profileIcon,
            contentDescription = null,
            modifier = Modifier
                .offset(x = 412.dp, y = 39.dp)
                .size(41.dp)
        )
    }
}

private val EnergyTimelineLabels = listOf(
    "21", "22", "23", "00", "01", "02", "03", "04", "05", "06",
    "07", "08", "09", "09", "10", "11", "12", "13", "14", "15",
    "16", "17", "18", "19", "20", "21", "22", "23", "00", "01",
    "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
    "12", "13"
)

@Composable
private fun EnergyTimelineGridBackground() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .height(5000.dp)
    ) {
        val hourGap = 119.dp.toPx()
        val startY = 103.dp.toPx()

        EnergyTimelineLabels.indices.forEach { index ->
            val y = startY + index * hourGap

            drawLine(
                color = Color(0xFF1D1D1D),
                start = Offset(59.dp.toPx(), y),
                end = Offset(469.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@Composable
private fun EnergyTimelineRuler(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .height(5000.dp)
    ) {
        val hourGap = 119.dp.toPx()
        val startY = 103.dp.toPx()

        EnergyTimelineLabels.indices.forEach { index ->
            val y = startY + index * hourGap

            drawLine(
                color = Color.White,
                start = Offset(19.dp.toPx(), y - 60.dp.toPx()),
                end = Offset(35.dp.toPx(), y - 60.dp.toPx()),
                strokeWidth = 1.25.dp.toPx()
            )

            drawLine(
                color = Color.White,
                start = Offset(19.dp.toPx(), y - 28.dp.toPx()),
                end = Offset(27.dp.toPx(), y - 28.dp.toPx()),
                strokeWidth = 1.25.dp.toPx()
            )

            drawLine(
                color = Color.White,
                start = Offset(19.dp.toPx(), y + 28.dp.toPx()),
                end = Offset(27.dp.toPx(), y + 28.dp.toPx()),
                strokeWidth = 1.25.dp.toPx()
            )
        }
    }

    EnergyTimelineLabels.forEachIndexed { index, label ->
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.offset(
                x = 19.dp,
                y = (94 + index * 119).dp
            )
        )
    }

    Box(
        modifier = Modifier
            .offset(x = 52.dp, y = 104.dp)
            .width(4.dp)
            .height(5000.dp)
            .background(Color.Black)
    )
}

@Composable
private fun EnergySectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    width: Dp = 355.dp,
    height: Dp,
    background: Color = Color(0xFF16151A),
    dotColor: Color = Color(0xFF721BFD)
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(
                color = background,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF363636),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 77.dp, y = 18.dp)
        )

        if (subtitle != null) {
            Box(
                modifier = Modifier
                    .offset(x = 77.dp, y = 45.dp)
                    .size(10.dp)
                    .background(dotColor, CircleShape)
            )

            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.offset(x = 92.dp, y = 39.dp)
            )
        }
    }
}

@Composable
private fun EnergyMelatoninWindow(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(355.dp)
            .height(115.dp)
    ) {
        MelatoninWindow(
            sizeN = 355.dp,
            heightM = 115.dp
        )

        Image(
            painter = painterResource(id = R.drawable.moon),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(38.dp)
        )

        Text(
            text = "Melatonin Window",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 77.dp, y = 18.dp)
        )

        Box(
            modifier = Modifier
                .offset(x = 77.dp, y = 45.dp)
                .size(10.dp)
                .background(Color(0xFF721BFD), CircleShape)
        )

        Text(
            text = "18m earlier",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.offset(x = 92.dp, y = 39.dp)
        )
    }
}

@Composable
private fun TotalSleepTimeOverlay(
    modifier: Modifier = Modifier,
    sleepIcon: Painter,
    editIcon: Painter,
    startTime: String = "23:10",
    endTime: String = "4:45"
) {
    Box(
        modifier = modifier
            .width(415.dp)
            .height(677.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(671.dp)
                .offset(y = 3.dp)
                .background(
                    Color(0x73721CFD),
                    RoundedCornerShape(12.dp)
                )
                .border(
                    2.dp,
                    Color(0xFF8C65F2),
                    RoundedCornerShape(12.dp)
                )
        )

        Box(
            modifier = Modifier
                .offset(x = 36.dp)
                .width(70.dp)
                .height(8.dp)
                .background(Color(0xFF8D59FC), RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-33).dp)
                .width(70.dp)
                .height(8.dp)
                .background(Color(0xFF8D59FC), RoundedCornerShape(8.dp))
        )

        Image(
            painter = sleepIcon,
            contentDescription = null,
            modifier = Modifier
                .offset(x = 8.dp, y = 21.dp)
                .size(12.dp)
        )

        Text(
            text = startTime,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.offset(x = 44.dp, y = 15.dp)
        )

        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = editIcon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Press to edit sleep time",
                color = Color(0xFF8F5AFF),
                fontSize = 16.sp
            )
        }

        Text(
            text = endTime,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-52).dp, y = (-14).dp)
        )
    }
}

@Composable
private fun ActivityCard(
    modifier: Modifier = Modifier,
    image: Painter,
    title: String
) {
    Row(
        modifier = modifier
            .width(120.dp)
            .height(32.dp)
            .background(Color(0xFF333333), RoundedCornerShape(4.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            .padding(start = 6.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 10.sp,
            lineHeight = 10.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun WakeWindowBlock(
    modifier: Modifier = Modifier,
    alarmTimeText: String,
    activeDays: List<Int>
) {
    Box(
        modifier = modifier
            .width(476.dp)
            .height(180.dp)
    ) {
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(110.dp)
                .background(Color(0xFF222222))
        )

        Text(
            text = "Wake window",
            color = Color(0xFF848484),
            fontSize = 18.sp,
            modifier = Modifier.offset(x = 179.dp, y = 103.dp)
        )

        AlarmTimePicker(
            modifier = Modifier.offset(x = 51.dp, y = (-19).dp),
            timeText = alarmTimeText,
            activeDays = activeDays
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 476,
    heightDp = 1000
)
@Composable
private fun EnergyScheduleScreenPreview() {
    MaterialTheme {
        EnergyScheduleScreen(
            profileIcon = ColorPainter(Color(0xFF721BFD)),
            lightningIcon = ColorPainter(Color(0xFF00EBAC)),
            sleepIcon = ColorPainter(Color.White),
            editIcon = ColorPainter(Color.White),
            windDownImage = ColorPainter(Color(0xFF8D59FC)),
            caffeineImage = ColorPainter(Color(0xFFFF4F7B)),
            alarmActiveDays = listOf(1, 2, 3, 4, 5, 6, 7)
        )
    }
}
