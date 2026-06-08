package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate

@Composable
fun AlarmTimePicker(
    modifier: Modifier = Modifier,
    timeText: String,
    activeDays: List<Int>,
    onClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val alarmIcon = painterResource(id = R.drawable.alarm)
    val menuIcon = painterResource(id = R.drawable.menu)

    val days = listOf(
        1 to "S",
        2 to "M",
        3 to "T",
        4 to "W",
        5 to "T",
        6 to "F",
        7 to "S"
    )

    Box(
        modifier = modifier
            .width(400.dp)
            .height(74.dp)
    ) {
        // Ellipse dot
        Box(
            modifier = Modifier
                .size(8.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-4).dp)
                .background(
                    color = Color(0xFFD9D9D9),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
        )

        // Polygon arrow
        Canvas(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterStart)
                .offset(x = 0.dp)
        ) {
            val path = Path().apply {
                moveTo(size.width * 0.2f, size.height * 0.15f)
                lineTo(size.width * 0.85f, size.height * 0.5f)
                lineTo(size.width * 0.2f, size.height * 0.85f)
                close()
            }
            rotate(degrees = 180f) {
                drawPath(
                    path = path,
                    color = Color(0xFFD9D9D9)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(385.dp)
                .height(74.dp)
                .background(
                    color = Color(0xFF222328),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = Color(0xFF353535),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() }
                .padding(start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = alarmIcon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = timeText,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    days.forEachIndexed { index, day ->
                        Text(
                            text = day.second,
                            color = if (activeDays.contains(day.first)) {
                                Color(0xFF721BFD)
                            } else {
                                Color(0xFF343434)
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 20.sp
                        )

                        if (index != days.lastIndex) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }

            Image(
                painter = menuIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onMenuClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmScheduleCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AlarmTimePicker(
                timeText = "06:00 Tomorrow",
                activeDays = listOf(1, 2, 3, 4, 5, 6, 7)
            )
        }
    }
}