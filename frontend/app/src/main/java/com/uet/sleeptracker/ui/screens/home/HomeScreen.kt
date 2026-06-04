package com.uet.sleeptracker.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.ScreenTopBar
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    dateRangeText: String = "May 17-30",
    sleepDebtHours: Float = 3.2f,
    debtSinceYesterdayText: String = "-8hrs",
    energyPotentialText: String = "84%",
    totalSleepTimeText: String = "8h 30m",
    sleepTimeRangeText: String = "23:30 - 08:00",
    napsTodayText: String = "",
    onProfileClick: () -> Unit = {},
    onPeaksAndDipsClick: () -> Unit = {},
    onCaffeineCutoffClick: () -> Unit = {},
    onEveningRoutineClick: () -> Unit = {},
    onBottomNavSelected: (SleepBottomNavTab) -> Unit = {}
) {
    var isSheetExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        bottomBar = {
            SleepBottomNavigation(
                selectedTab = SleepBottomNavTab.MySleep,
                onTabSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                ScreenTopBar(
                    mainTitle = "My Sleep",
                    subTitle = dateRangeText,
                    actions = {
                        SleepTopActionIcon(
                            type = SleepIconType.Profile,
                            onClick = onProfileClick,
                            tint = Color(0xFF7C5CFF),
                            size = 30.dp
                        )
                    },
                    modifier = Modifier.padding(top = 18.dp)
                )

                // Main content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 12.dp,
                        bottom = 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SleepDebtHero(sleepDebtHours = sleepDebtHours)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DebtSummary(
                                value = debtSinceYesterdayText,
                                modifier = Modifier.weight(1f)
                            )
                            EnergyPotentialCard(
                                value = energyPotentialText,
                                modifier = Modifier.weight(1.1f)
                            )
                        }
                    }

                    item {
                        LastNightCard(
                            totalSleepTime = totalSleepTimeText,
                            sleepTimeRange = sleepTimeRangeText
                        )
                    }

                    item {
                        NapsTodayCard(napsTodayText = napsTodayText)
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Sleep tracking unreliable?",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Help improve RISE",
                                color = Color(0xFF7C5CFF),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Draggable slide-up sheet overlay
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val density = LocalDensity.current
                val collapsedSheetHeight = 76.dp
                val expandedSheetHeight = maxHeight - 48.dp
                val sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                var isSheetDragging by remember { mutableStateOf(false) }
                var targetSheetHeightVal by rememberSaveable { mutableStateOf(collapsedSheetHeight.value) }
                val targetSheetHeight = targetSheetHeightVal.dp
                var isArrowExpanded by rememberSaveable { mutableStateOf(false) }
                val animatedSheetHeight by animateDpAsState(
                    targetValue = targetSheetHeight,
                    animationSpec = tween(durationMillis = if (isSheetDragging) 0 else 520),
                    label = "sheetHeightAnim"
                )
                val shouldShowSheetContent = animatedSheetHeight > collapsedSheetHeight + 8.dp

                LaunchedEffect(isSheetExpanded, expandedSheetHeight, isSheetDragging) {
                    if (!isSheetDragging) {
                        targetSheetHeightVal = if (isSheetExpanded) expandedSheetHeight.value else collapsedSheetHeight.value
                    }
                }

                LaunchedEffect(animatedSheetHeight, collapsedSheetHeight, expandedSheetHeight, isSheetDragging) {
                    if (isSheetDragging) return@LaunchedEffect

                    when {
                        (animatedSheetHeight.value - expandedSheetHeight.value).absoluteValue < 0.5f -> {
                            isArrowExpanded = true
                        }

                        (animatedSheetHeight.value - collapsedSheetHeight.value).absoluteValue < 0.5f -> {
                            isArrowExpanded = false
                        }
                    }
                }

                Surface(
                    color = Color(0xFF232429),
                    shape = sheetShape,
                    modifier = Modifier
                        .requiredWidth(maxWidth + 2.dp)
                        .height(animatedSheetHeight)
                        .align(Alignment.BottomCenter)
                        .pointerInput(maxHeight) {
                            detectVerticalDragGestures(
                                onDragStart = {
                                    isSheetDragging = true
                                    targetSheetHeightVal = animatedSheetHeight.value
                                },
                                onDragEnd = {
                                    isSheetDragging = false
                                    val sheetTopVal = maxHeight.value - targetSheetHeightVal
                                    val shouldExpand = sheetTopVal < maxHeight.value * 0.5f
                                    isSheetExpanded = shouldExpand
                                    targetSheetHeightVal = if (shouldExpand) expandedSheetHeight.value else collapsedSheetHeight.value
                                },
                                onDragCancel = {
                                    isSheetDragging = false
                                    targetSheetHeightVal = if (isSheetExpanded) expandedSheetHeight.value else collapsedSheetHeight.value
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                val dragAmountDp = with(density) { dragAmount.toDp() }
                                targetSheetHeightVal = (targetSheetHeightVal - dragAmountDp.value)
                                    .coerceIn(collapsedSheetHeight.value, expandedSheetHeight.value)
                            }
                        }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Drag Handle
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f))
                                )
                            }

                            SheetHeader(
                                isExpanded = isArrowExpanded,
                                onToggle = { isSheetExpanded = !isSheetExpanded }
                            )

                            if (shouldShowSheetContent) {
                                SheetExpandedContent(
                                    onPeaksAndDipsClick = onPeaksAndDipsClick,
                                    onCaffeineCutoffClick = onCaffeineCutoffClick,
                                    onEveningRoutineClick = onEveningRoutineClick
                                )
                            }
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxSize()
                                .align(Alignment.TopCenter)
                        ) {
                            val radius = 20.dp.toPx()
                            val strokeWidth = 1.dp.toPx()
                            val inset = strokeWidth / 2f
                            clipRect(bottom = size.height * 0.5f) {
                                drawRoundRect(
                                    color = Color(0xFF55565C),
                                    topLeft = Offset(inset, inset),
                                    size = Size(
                                        width = size.width - strokeWidth,
                                        height = size.height - strokeWidth
                                    ),
                                    cornerRadius = CornerRadius(radius, radius),
                                    style = Stroke(width = strokeWidth)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun HomeScreenPreview() {
    SleeptrackerTheme {
        HomeScreen()
    }
}
