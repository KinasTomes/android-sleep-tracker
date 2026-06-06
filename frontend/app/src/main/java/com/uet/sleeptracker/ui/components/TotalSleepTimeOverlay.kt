package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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

@Composable
fun TotalSleepTimeOverlay(
    modifier: Modifier = Modifier,
    startTime: String = "23:10",
    endTime: String = "4:45",
    onEditClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(415.dp)
            .height(677.dp)
    ) {
        // Main overlay box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(671.dp)
                .align(Alignment.TopCenter)
                .offset(y = 3.dp)
                .background(
                    color = Color(0x73721CFD),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFF8C65F2),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        // Top handle
        Box(
            modifier = Modifier
                .offset(x = 36.dp, y = 0.dp)
                .width(70.dp)
                .height(8.dp)
                .background(
                    color = Color(0xFF8D59FC),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Bottom handle
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-33).dp, y = 0.dp)
                .width(70.dp)
                .height(8.dp)
                .background(
                    color = Color(0xFF8D59FC),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Start icon
        Icon(
            painter = painterResource(id = R.drawable.zzz),
            contentDescription = null,
            tint = Color(0xFF8F5AFF),
            modifier = Modifier
                .offset(x = 8.dp, y = 21.dp)
                .size(12.dp)
        )

        // Start time
        Text(
            text = startTime,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.offset(x = 44.dp, y = 15.dp)
        )

        // Edit action
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { onEditClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.scissor),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Press to edit sleep time",
                color = Color(0xFF8F5AFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // End time
        Text(
            text = endTime,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-52).dp, y = (-14).dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TotalSleepTimePreview() {
    TotalSleepTimeOverlay(
        startTime = "23:10",
        endTime = "4:45"
    )
}