package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepPrimary

private val ProgressBarWidth = 43.dp
private val ProgressBarCornerRadius = 6.dp
private val ProgressBarShape = RoundedCornerShape(ProgressBarCornerRadius)

@Composable
fun SleepProgressBar(
    modifier: Modifier = Modifier,
    barHeight: Dp = 157.dp
) {
    ProgressSleepBarFrame(
        modifier = modifier,
        barHeight = barHeight,
        containerColor = SleepPrimary.copy(alpha = 0.5f),
        borderColor = Color(0xFF915DFF)
    )
}

@Composable
fun MissingSleepProgressBar(
    modifier: Modifier = Modifier,
    barHeight: Dp = 159.dp
) {
    ProgressSleepBarFrame(
        modifier = modifier,
        barHeight = barHeight,
        containerColor = Color(0x80333333),
        borderColor = Color(0xFF4F4F4F),
        centerContent = {
            Image(
                painter = painterResource(id = R.drawable.add_sleep),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }
    )
}

@Composable
private fun ProgressSleepBarFrame(
    modifier: Modifier = Modifier,
    barHeight: Dp,
    containerColor: Color,
    borderColor: Color,
    centerContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .width(ProgressBarWidth)
            .height(barHeight)
            .clip(ProgressBarShape)
            .background(containerColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = ProgressBarShape
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.zzz),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp)
                .size(16.dp)
        )

        if (centerContent != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                centerContent()
            }
        }

        Image(
            painter = painterResource(id = R.drawable.sun),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 2.dp)
                .size(12.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
private fun SleepProgressBarPreview() {
    Box(
        modifier = Modifier
            .size(width = 103.dp, height = 216.dp),
        contentAlignment = Alignment.Center
    ) {
        SleepProgressBar()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
private fun MissingSleepProgressBarPreview() {
    Box(
        modifier = Modifier
            .size(width = 103.dp, height = 216.dp),
        contentAlignment = Alignment.Center
    ) {
        MissingSleepProgressBar()
    }
}
