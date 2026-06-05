package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val AudioCardShape = RoundedCornerShape(12.dp)
private val AudioCardBackground = Color(0xFF16151A)
private val AudioCardBorder = Color(0xFF363636)
private val AudioCardMutedText = Color(0xFF989898)

@Composable
fun SleepAudioCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
    ) {
        LayeredAudioCardShadow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 4.dp)
                .offset(y = 12.dp),
            alpha = 0.30f
        )

        LayeredAudioCardShadow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 2.dp)
                .offset(y = 6.dp),
            alpha = 0.70f
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(80.dp)
                .clip(AudioCardShape)
                .background(AudioCardBackground)
                .border(1.dp, AudioCardBorder, AudioCardShape)
                .clickable(onClick = onClick)
        ) {
            PlayBadge(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 92.dp, end = 20.dp, top = 19.dp, bottom = 18.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    color = AudioCardMutedText,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 1.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PlayBadge(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(45.dp)
            .clip(CircleShape)
            .border(width = 2.dp, color = Color.White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.play_button),
            contentDescription = null,
            modifier = Modifier
                .size(width = 18.dp, height = 19.dp)
                .offset(x = 1.5.dp)
        )
    }
}

@Composable
private fun LayeredAudioCardShadow(
    alpha: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(AudioCardShape)
            .background(AudioCardBackground.copy(alpha = alpha))
            .border(1.dp, AudioCardBorder.copy(alpha = alpha), AudioCardShape)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SleepAudioCardPreview() {
    SleeptrackerTheme {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .padding(20.dp)
        ) {
            SleepAudioCard(
                title = "Sleep sounds",
                subtitle = "Ambient Music",
                onClick = {}
            )
        }
    }
}
