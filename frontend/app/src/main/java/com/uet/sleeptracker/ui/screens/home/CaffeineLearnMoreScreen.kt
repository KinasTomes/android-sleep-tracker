package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val CaffeineLearnScreenColor = Color(0xFF121212)
private val CaffeineHeroColor = Color(0xFF41290F)
private val CaffeineBodyMutedColor = Color(0xFF898883)
private val CaffeinePurpleBulletColor = Color(0xFF7A2FFF)

@Composable
fun CaffeineLearnMoreScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CaffeineLearnScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CaffeineLearnScreenColor)
        ) {
            ToolDetailTopBar(
                title = "Caffeine Cutoff",
                onBackClick = onCloseClick,
                type = 1
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                CaffeineLearnHero()

                Spacer(modifier = Modifier.height(108.dp))

                Text(
                    text = "Tips",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 21.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                BulletParagraph(
                    text = "You know caffeine keeps you up, but not everyone knows its effects can last up to 10 hours in your system - avoid coffee, tea, and soda in the afternoon",
                    textColor = Color.White,
                    modifier = Modifier.padding(start = 21.dp, end = 15.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                BulletParagraph(
                    text = "Try switching to decaf",
                    textColor = Color.White,
                    singleLine = true,
                    modifier = Modifier.padding(start = 21.dp, end = 21.dp)
                )

                Spacer(modifier = Modifier.height(34.dp))

                Text(
                    text = "The Science Behind It",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 21.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Caffeine blocks adenosine - a chemical that makes you feel sleepy - from binding to receptors in the brain. This makes you feel less tired and the effect continues as long as you have caffeine in your system",
                    color = Color(0xFF818181),
                    style = caffeineBodyTextStyle(),
                    modifier = Modifier.padding(start = 21.dp, end = 19.dp)
                )

                Spacer(modifier = Modifier.height(37.dp))

                Text(
                    text = "It takes your body about 5-6 hours to breakdown half of the caffeine in your system. This means if you have a coffee at noon, at around 5pm you still have half of it in your system and by 10pm you have a quarter of it. So try to reserve caffeine for your morning routine.",
                    color = Color(0xFF818181),
                    style = caffeineBodyTextStyle(),
                    modifier = Modifier.padding(start = 20.dp, end = 8.dp, bottom = 54.dp)
                )
            }
        }
    }
}

@Composable
private fun CaffeineLearnHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(284.dp)
            .background(CaffeineHeroColor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.coffee_cup),
            contentDescription = "Coffee cup",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(width = 162.dp, height = 228.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 21.dp, top = 87.dp)
        ) {
            Text(
                text = "Limit Caffeine",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Limiting your caffeine intake in the\nafternoon is a great way to ensure\nyou get the best quality sleep at\nnight.",
                color = CaffeineBodyMutedColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun BulletParagraph(
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(top = if (singleLine) 6.dp else 7.dp)
                .size(12.dp)
                .background(CaffeinePurpleBulletColor, CircleShape)
        )

        Text(
            text = text,
            color = textColor,
            style = caffeineBodyTextStyle(),
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            modifier = Modifier.padding(start = 22.dp)
        )
    }
}

private fun caffeineBodyTextStyle(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = (-0.8).sp
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 402, heightDp = 874)
@Composable
private fun CaffeineLearnMoreScreenPreview() {
    SleeptrackerTheme {
        CaffeineLearnMoreScreen()
    }
}
