package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uet.sleeptracker.R

@Composable
fun SleepWindow(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(width = 45.dp, height = 22.dp)
    ) {
        Canvas(
            modifier = Modifier.size(width = 45.dp, height = 22.dp)
        ) {
            val stroke = 3.dp.toPx()

            drawLine(
                color = Color.White,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.White,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.moon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 80, heightDp = 80)
@Composable
fun SleepWindowPreview() {
    SleepWindow()
}
