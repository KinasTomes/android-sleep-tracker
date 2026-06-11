package com.uet.sleeptracker.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepPrimary

private val HabitBackground = Color(0xFF111111)
private val HabitDivider = Color(0xFF1A1A1D)
private val HabitMuted = Color(0xFFBABABA)

data class HabitIntroductionContent(
    val topBarTitle: String,
    val heroTitle: String,
    val heroDescription: String,
    @param:DrawableRes val heroImageRes: Int,
    val sectionTitle: String,
    val bullets: List<String>,
    val explanationTitle: String,
    val explanationParagraphs: List<String>,
    @param:DrawableRes val explanationImageRes: Int? = null,
)

@Composable
fun HabitIntroductionScreen(
    content: HabitIntroductionContent,
    onCloseClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = HabitBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HabitBackground)
        ) {
            ToolDetailTopBar(
                title = content.topBarTitle,
                onBackClick = onCloseClick,
                type = 1,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(440.dp)
                ) {
                    Image(
                        painter = painterResource(content.heroImageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )

                    Column(
                        modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 106.dp)
                    ) {
                        Text(
                            text = content.heroTitle,
                            color = Color.White,
                            fontSize = 28.sp,
                            letterSpacing = 0.sp,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            text = content.heroDescription,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth(0.82f)
                                .padding(top = 16.dp)
                        )
                    }

                    HabitPrimaryButton(
                        text = "GET STARTED",
                        onClick = onGetStartedClick,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }

                Text(
                    text = content.sectionTitle,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 21.dp, top = 76.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    content.bullets.forEach { bullet ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(SleepPrimary)
                            )
                            Text(
                                text = bullet,
                                color = Color.White,
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                letterSpacing = 0.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(start = 17.dp)
                            )
                        }
                    }
                }

                content.explanationImageRes?.let { imageRes ->
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(start = 18.dp, end = 18.dp, top = 76.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Text(
                    text = content.explanationTitle,
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(
                        start = 21.dp,
                        end = 21.dp,
                        top = if (content.explanationImageRes == null) 54.dp else 48.dp,
                    )
                )

                Column(
                    modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 22.dp, bottom = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    content.explanationParagraphs.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}

data class HabitSettingsContent(
    val topBarTitle: String,
    val heroTitle: String? = null,
    @param:DrawableRes val heroIconRes: Int? = null,
    val heroGradientEnd: Color? = null,
    val toggleTitle: String,
    val toggleSubtitle: String,
    val reminderValue: String,
    val reminderSubtitle: String = "Before melatonin window",
    val initialEnabled: Boolean = true,
    val showActions: Boolean = true,
)

@Composable
fun HabitReminderSettingsScreen(
    content: HabitSettingsContent,
    onCloseClick: () -> Unit,
    onReminderTimeClick: (() -> Unit)? = null,
    onRemindMeClick: () -> Unit = {},
    onNotNowClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var enabled by remember(content.topBarTitle) { mutableStateOf(content.initialEnabled) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = HabitBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HabitBackground)
        ) {
            ToolDetailTopBar(
                title = content.topBarTitle,
                onBackClick = onCloseClick,
                type = 1,
            )

            if (content.heroTitle != null && content.heroIconRes != null && content.heroGradientEnd != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0F110E), content.heroGradientEnd)
                            )
                        )
                        .padding(horizontal = 21.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(content.heroIconRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = content.heroTitle,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start = 18.dp)
                    )
                }
            }

            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(
                    start = 21.dp,
                    end = 21.dp,
                    top = if (content.heroTitle == null) 72.dp else 62.dp,
                    bottom = 34.dp,
                )
            )

            HorizontalDivider(color = HabitDivider, thickness = 1.dp)
            HabitToggleRow(
                title = content.toggleTitle,
                subtitle = content.toggleSubtitle,
                checked = enabled,
                onCheckedChange = { enabled = it },
            )
            HorizontalDivider(color = HabitDivider, thickness = 1.dp)
            HabitReminderRow(
                value = content.reminderValue,
                subtitle = content.reminderSubtitle,
                onClick = onReminderTimeClick,
            )
            HorizontalDivider(color = HabitDivider, thickness = 1.dp)

            if (content.showActions) {
                Spacer(modifier = Modifier.weight(1f))
                HabitPrimaryButton(
                    text = "REMIND ME",
                    onClick = onRemindMeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                HabitOutlinedButton(
                    text = "NOT NOW",
                    onClick = onNotNowClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun HabitToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(start = 21.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 18.sp)
            Text(
                text = subtitle,
                color = HabitMuted,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        HabitSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun HabitReminderRow(
    value: String,
    subtitle: String,
    onClick: (() -> Unit)?,
) {
    val clickModifier = if (onClick == null) Modifier else Modifier.clickable(onClick = onClick)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickModifier)
            .height(73.dp)
            .padding(start = 21.dp, end = 21.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.clock),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = "Reminder Time", color = Color.White, fontSize = 16.sp)
                Text(text = subtitle, color = HabitMuted, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, color = HabitMuted, fontSize = 14.sp)
            if (onClick != null) {
                Icon(
                    painter = painterResource(R.drawable.right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun HabitSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 0.dp,
        animationSpec = tween(200),
        label = "habitSwitch",
    )
    Box(
        modifier = Modifier
            .width(36.dp)
            .height(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) },
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(32.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(50))
                .background(if (checked) Color(0xFF01EEB8) else Color(0xFF808080))
        )
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF7EC7C0) else Color(0xFFBABABA))
        )
    }
}

@Composable
private fun HabitPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SleepPrimary),
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
            )
        )
    }
}

@Composable
private fun HabitOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(3.dp, SleepPrimary),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
    ) {
        Text(text = text, color = Color.White, fontSize = 20.sp, letterSpacing = 0.sp)
    }
}
