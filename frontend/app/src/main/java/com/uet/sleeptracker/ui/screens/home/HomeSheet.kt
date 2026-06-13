package com.uet.sleeptracker.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlin.math.absoluteValue
import java.util.Calendar
import java.util.Locale
import com.uet.sleeptracker.ui.screens.tools.AlarmItem


private val SheetPanelColor = Color(0xFF232429)
private val SheetBorderColor = Color(0xFF55565C)
private val SheetItemsShape = RoundedCornerShape(16.dp)
private val SheetItemMinHeight = 72.dp

@Composable
internal fun HomeSlideUpPanel(
    modifier: Modifier = Modifier,
    alarms: List<AlarmItem> = emptyList(),
    onPeaksAndDipsClick: () -> Unit = {},
    onCaffeineCutoffClick: () -> Unit = {},
    onEveningRoutineClick: () -> Unit = {},
    onMelatoninWindowClick: () -> Unit = {},
    onBrowseAllToolsClick: () -> Unit = {},
    onAlarmEditClick: (AlarmItem) -> Unit = {}
) {
    var isSheetExpanded by rememberSaveable { mutableStateOf(false) }

    val now = Calendar.getInstance()
    val nextAlarmInfo = alarms
        .mapNotNull { alarm ->
            val nextTime = getNextOccurrence(alarm, now)
            if (nextTime != null) alarm to nextTime else null
        }
        .minByOrNull { it.second.timeInMillis }

    val alarmLabel = if (nextAlarmInfo != null) {
        val (alarm, _) = nextAlarmInfo
        alarm.time
    } else {
        "No alarm"
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val collapsedSheetHeight = 148.dp // 76.dp bottom bar height + 72.dp visible header height
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
            color = SheetPanelColor,
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
                        alarmLabel = alarmLabel,
                        onToggle = { isSheetExpanded = !isSheetExpanded },
                        onAlarmClick = {
                            val alarmToEdit = nextAlarmInfo?.first ?: alarms.firstOrNull()
                            alarmToEdit?.let { onAlarmEditClick(it) }
                        }
                    )

                    if (shouldShowSheetContent) {
                        SheetExpandedContent(
                            onPeaksAndDipsClick = onPeaksAndDipsClick,
                            onCaffeineCutoffClick = onCaffeineCutoffClick,
                            onEveningRoutineClick = onEveningRoutineClick,
                            onMelatoninWindowClick = onMelatoninWindowClick,
                            onBrowseAllToolsClick = {
                                isSheetExpanded = false
                                onBrowseAllToolsClick()
                            }
                        )
                    }
                }

                androidx.compose.foundation.Canvas(
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
                            color = SheetBorderColor,
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

@Composable
internal fun SheetToolItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    showBell: Boolean = true,
    titleColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = SheetItemMinHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            icon()
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (showBell) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Reminder",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            if (title != "Browse all tools") {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
internal fun SheetHeader(
    isExpanded: Boolean,
    alarmLabel: String,
    onToggle: () -> Unit,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.double_up),
            contentDescription = if (isExpanded) "Collapse panel" else "Expand panel",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onToggle
                )
                .rotate(if (isExpanded) 180f else 0f)
        )

        // Right actions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Clock Pill button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF721BFD))
                    .clickable { onAlarmClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.alarm),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(text = alarmLabel, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.W400)
            }
        }
    }
}

@Composable
internal fun ColumnScope.SheetExpandedContent(
    modifier: Modifier = Modifier,
    onPeaksAndDipsClick: () -> Unit = {},
    onCaffeineCutoffClick: () -> Unit = {},
    onEveningRoutineClick: () -> Unit = {},
    onMelatoninWindowClick: () -> Unit = {},
    onBrowseAllToolsClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 92.dp), // 16.dp default + 76.dp bottom bar height
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SheetItemsShape)
                    .background(SheetPanelColor)
                    .border(width = 1.dp, color = SheetBorderColor, shape = SheetItemsShape)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SheetToolItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.paeks_n_dips),
                                contentDescription = "Peaks & dips icon",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        },
                        title = "Peaks & dips",
                        subtitle = "09:00",
                        showBell = true,
                        onClick = onPeaksAndDipsClick
                    )
                    Divider()

                    SheetToolItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ceffeine),
                                contentDescription = "Avoid caffeine icon",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        },
                        title = "Avoid caffeine",
                        subtitle = "12:45",
                        showBell = true,
                        onClick = onCaffeineCutoffClick
                    )
                    Divider()

                    SheetToolItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.evening),
                                contentDescription = "Evening routine icon",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        },
                        title = "Evening routine",
                        subtitle = "20:35–22:45",
                        showBell = true,
                        onClick = onEveningRoutineClick
                    )
                    Divider()

                    SheetToolItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.melatonin_window),
                                contentDescription = "Melatonin window icon",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        },
                        title = "Melatonin window",
                        subtitle = "22:45–00:15",
                        showBell = true,
                        onClick = onMelatoninWindowClick
                    )
                    Divider()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1C1C1E))
                    ) {
                        SheetToolItem(
                            icon = {
                                Text(
                                    text = "+",
                                    color = Color(0xFF8C5CFF),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            },
                            title = "Browse all tools",
                            subtitle = null,
                            showBell = false,
                            titleColor = Color(0xFF8C5CFF),
                            onClick = onBrowseAllToolsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SheetBorderColor)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 402, heightDp = 600)
@Composable
private fun HomeSheetPreview() {
    SleeptrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121212))
                .padding(16.dp)
        ) {
            SheetHeader(
                isExpanded = true,
                alarmLabel = "08:00",
                onToggle = {},
                onAlarmClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            SheetExpandedContent()
        }
    }
}

private fun getNextOccurrence(alarm: AlarmItem, now: Calendar): Calendar? {
    if (!alarm.isEnabled || alarm.activeDays.none { it }) return null
    val parts = alarm.time.split(":")
    if (parts.size != 2) return null
    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null

    for (offset in 0..7) {
        val candidate = (now.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, offset)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dayOfWeekIndex = candidate.get(Calendar.DAY_OF_WEEK) - 1
        if (alarm.activeDays.getOrElse(dayOfWeekIndex) { false }) {
            if (candidate.after(now)) {
                return candidate
            }
        }
    }
    return null
}

private fun getDayLabel(nextTime: Calendar, now: Calendar): String {
    val nextYear = nextTime.get(Calendar.YEAR)
    val nextDayOfYear = nextTime.get(Calendar.DAY_OF_YEAR)

    val todayYear = now.get(Calendar.YEAR)
    val todayDayOfYear = now.get(Calendar.DAY_OF_YEAR)

    val tomorrow = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
    val tomorrowYear = tomorrow.get(Calendar.YEAR)
    val tomorrowDayOfYear = tomorrow.get(Calendar.DAY_OF_YEAR)

    return when {
        nextYear == todayYear && nextDayOfYear == todayDayOfYear -> "Today"
        nextYear == tomorrowYear && nextDayOfYear == tomorrowDayOfYear -> "Tomorrow"
        else -> {
            nextTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US) ?: ""
        }
    }
}
