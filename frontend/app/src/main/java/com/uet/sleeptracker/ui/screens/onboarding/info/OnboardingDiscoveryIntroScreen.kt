package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun OnboardingDiscoveryIntroScreen(
    onNavigateNext: () -> Unit,
    onLearnMore: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SleepBackground)
    ) {

        WavyLines(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.TopCenter)
                .padding(top = 36.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(2.2f))

            Text(
                text = stringResource(id = R.string.discovery_intro_small_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.discovery_intro_main_bold))
                    }
                    append(" ")
                    append(stringResource(id = R.string.discovery_intro_main_desc))
                },
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.weight(0.8f))

            DiscoveryIntroBullet(text = stringResource(id = R.string.discovery_intro_bullet))

            Spacer(modifier = Modifier.height(20.dp))

            DiscoveryIntroBullet(text = stringResource(id = R.string.discovery_intro_bullet_exercise))

            Spacer(modifier = Modifier.weight(1.5f))

            SleepPrimaryButton(
                text = stringResource(id = R.string.discovery_intro_button),
                onClick = onNavigateNext,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onLearnMore) {
                Text(
                    text = stringResource(id = R.string.discovery_intro_learn_more),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = SleepPrimary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun DiscoveryIntroBullet(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            border = androidx.compose.foundation.BorderStroke(2.dp, SleepPrimary),
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = SleepPrimary,
                modifier = Modifier.padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                lineHeight = 20.sp,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun WavyLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(
                    0f   to Color.Black,
                    0.5f to Color.Black,
                    1f   to Color.Transparent
                ),
                blendMode = BlendMode.DstIn
            )
        }) {
        val width = size.width
        val height = size.height
        
        val colors = listOf(
            Color(0xFFF68514),
            Color(0xFFEA580C),
            Color(0xFFD946EF),
            Color(0xFFA855F7),
            Color(0xFF721BFD)
        )

        colors.forEachIndexed { index, color ->
            val path = Path()
            val yStart = height * 0.3f + (index * 20.dp.toPx())
            path.moveTo(0f, yStart)
            
            val midX = width * 0.5f
            val midY = yStart + (index * 10.dp.toPx())
            
            path.cubicTo(
                width * 0.2f, yStart - (40.dp.toPx() + index * 12.dp.toPx()),
                width * 0.4f, yStart + (60.dp.toPx() + index * 6.dp.toPx()),
                midX, midY
            )
            
            path.cubicTo(
                width * 0.7f, midY - (70.dp.toPx() - index * 8.dp.toPx()),
                width * 0.9f, midY + (30.dp.toPx() + index * 10.dp.toPx()),
                width, yStart + (20.dp.toPx() * (index - 2))
            )

            drawPath(
                path = path,
                color = color.copy(alpha = 0.8f),
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun OnboardingDiscoveryIntroScreenPreview() {
    SleeptrackerTheme {
        OnboardingDiscoveryIntroScreen(onNavigateNext = {}, onLearnMore = {})
    }
}
