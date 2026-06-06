package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryButton
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val EnvironmentCheckBackground = Color(0xFF010101)
private val EnvironmentCheckDivider = Color(0xFF141414)
private val EnvironmentCheckMuted = Color(0xFF989898)
private val EnvironmentCheckReminderMuted = Color(0xFFBABABA)
private val EnvironmentCheckToggleThumb = Color(0xFF8AD2CE)
private val EnvironmentCheckToggleTrackOff = Color(0xFF363636)
private val EnvironmentCheckToggleThumbOff = Color(0xFFBABABA)

@Composable
fun EnvironmentCheckScreen(
    modifier: Modifier = Modifier,
    reminderTime: String = "30m",
    initialRemindersEnabled: Boolean = true,
    onRemindersEnabledChange: (Boolean) -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
    onRemindMeClick: () -> Unit = {},
    onNotNowClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    var remindersEnabled by remember { mutableStateOf(initialRemindersEnabled) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = EnvironmentCheckBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(EnvironmentCheckBackground)
        ) {
            ToolDetailTopBar(
                title = "Environment Check",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                EnvironmentCheckHero()

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Benefits",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EnvironmentCheckBenefit("Increase sleep quality")
                    EnvironmentCheckBenefit("Get to sleep faster")
                    EnvironmentCheckBenefit("Wake up less often")
                }

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "How does this work?",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Do a quick environment check before going to bed to ensure you fall, and stay, asleep. The three key factor are temperature, light, and bed associations. We’ll send a reminder to do a check right before bed.",
                    color = EnvironmentCheckMuted,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    letterSpacing = (-0.54).sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "The fix",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    EnvironmentCheckFixItem(
                        number = "1",
                        text = "Reduce your bedroom temperature. Ideally, 62 to 67 degrees F."
                    )
                    EnvironmentCheckFixItem(
                        number = "2",
                        text = "Do what you can to create total darkness in your room - such as removing electronics, blackout curtains, or using a sleep mask."
                    )
                    EnvironmentCheckFixItem(
                        number = "3",
                        text = "Only sleep in your bed. Do everything else somewhere else."
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "How does this work?",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EnvironmentCheckExplanation(
                        title = "Temperature.",
                        body = " Your core body temperature needs to drop 1-3 degrees during the night to fall and stay asleep. For most people the ideal temperature is colder than you think, 62-67 degrees F environment. If your body gets too hot during the night it can lead to wake ups."
                    )
                    EnvironmentCheckExplanation(
                        title = "Light.",
                        body = " Your eyes can detect light even when they are closed. Our bodies are hard-wired to use changes in light as an indicator to wake up. Sources as small as a digital clock, power lights on TVs and power boxes and light coming in from outside can cause waking. Do what you can to remove sources of light from your room. Try blackout curtains or a sleep mask. It may be odd at first, but you'll find blocking light will help your sleep."
                    )
                    EnvironmentCheckExplanation(
                        title = "Bed associations.",
                        body = " Your brain has an amazing power to associate things- places, smells, sounds, lights, etc. These days people do many things that aren't sleep in the bedroom and in bed- work, TV, games, phone, etc. So, when you're in bed trying to sleep... your brain is expecting something else. Keep your bed and bedroom for sleep and sleep only. Do other things in other rooms."
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(
                    color = EnvironmentCheckDivider,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                EnvironmentCheckToggleRow(
                    checked = remindersEnabled,
                    onCheckedChange = {
                        remindersEnabled = it
                        onRemindersEnabledChange(it)
                    }
                )

                HorizontalDivider(
                    color = EnvironmentCheckDivider,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                EnvironmentCheckReminderRow(
                    value = reminderTime,
                    onClick = onReminderTimeClick
                )

                HorizontalDivider(
                    color = EnvironmentCheckDivider,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                SleepPrimaryButton(
                    text = "REMIND ME",
                    onClick = onRemindMeClick,
                    height = 80.dp,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SleepPrimaryOutlinedButton(
                    text = "NOT NOW",
                    onClick = onNotNowClick,
                    height = 80.dp,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun EnvironmentCheckHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.prep_the_bedroom),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Black,
                            Color(0xFF2A0E57),
                            Color(0xFF371470)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 107.dp, end = 24.dp)
        ) {
            Text(
                text = "Prep your bedroom",
                color = Color.White,
                fontSize = 28.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(29.dp))

            Text(
                text = "Make sure you have the right room environment to fall asleep.",
                color = EnvironmentCheckMuted,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@Composable
private fun EnvironmentCheckBenefit(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(SleepPrimary)
        )

        Spacer(modifier = Modifier.width(17.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            letterSpacing = (-0.54).sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun EnvironmentCheckFixItem(
    number: String,
    text: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(SleepPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontSize = 28.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = text,
            color = EnvironmentCheckMuted,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            letterSpacing = (-0.54).sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun EnvironmentCheckExplanation(
    title: String,
    body: String,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = EnvironmentCheckMuted,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-0.54).sp
                )
            ) {
                append(title)
            }
            withStyle(
                SpanStyle(
                    color = EnvironmentCheckMuted,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.54).sp
                )
            ) {
                append(body)
            }
        },
        lineHeight = 26.sp
    )
}

@Composable
private fun EnvironmentCheckToggleRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .padding(start = 24.dp, end = 27.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Remind me",
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "to check environment",
                color = EnvironmentCheckMuted,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        EnvironmentCheckSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun EnvironmentCheckReminderRow(
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(start = 25.dp, end = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(17.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Reminder Time",
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = "Before melatonin window",
                color = EnvironmentCheckReminderMuted,
                fontSize = 16.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Text(
            text = value,
            color = EnvironmentCheckReminderMuted,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.width(6.dp))

        Image(
            painter = painterResource(id = R.drawable.right),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun EnvironmentCheckSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "EnvironmentCheckSwitchOffset"
    )

    Box(
        modifier = modifier
            .width(36.dp)
            .height(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(16.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(8.dp))
                .background(if (checked) SleepPrimary else EnvironmentCheckToggleTrackOff)
        )

        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .size(20.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(if (checked) EnvironmentCheckToggleThumb else EnvironmentCheckToggleThumbOff)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101, widthDp = 476, heightDp = 956)
@Composable
private fun EnvironmentCheckScreenPreview() {
    SleeptrackerTheme {
        EnvironmentCheckScreen(
            initialRemindersEnabled = true
        )
    }
}
