package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = Offset(size.width / 2, size.height / 2)
            )

            drawCircle(
                color = Color(0xFF010101),
                radius = 6.dp.toPx(),
                center = Offset(size.width / 2 + 4.dp.toPx(), size.height / 2 - 3.dp.toPx())
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 80, heightDp = 80)
@Composable
fun SleepWindowPreview() {
    SleepWindow()
}
