package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.screens.home.MaterialCheckIcon
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

data class ReminderChoiceItem(
    val label: String,
    val supportingLabel: String? = null,
)

private val ReminderScreenColor = Color(0xFF121212)
private val ReminderSectionColor = Color(0xFF17151D)
private val ReminderStrokeColor = Color(0xFF1A1A1D)
private val ReminderMutedTextColor = Color(0xFFBABABA)
private val ReminderRowHeight = 68.dp

@Composable
fun ReminderTimeScreen(
    modifier: Modifier = Modifier,
    headerTitle: String,
    screenTitle: String,
    sectionLabel: String,
    options: List<ReminderChoiceItem>,
    initialSelectedIndex: Int,
    type: Int = 0,
    onCloseClick: () -> Unit = {},
) {
    ReminderChoiceTemplate(
        modifier = modifier,
        headerTitle = headerTitle,
        screenTitle = screenTitle,
        sectionLabel = sectionLabel,
        options = options,
        initialSelectedIndex = initialSelectedIndex,
        type = type,
        onCloseClick = onCloseClick
    )
}

@Composable
fun ReminderChoiceTemplate(
    modifier: Modifier = Modifier,
    headerTitle: String,
    screenTitle: String,
    sectionLabel: String,
    options: List<ReminderChoiceItem>,
    initialSelectedIndex: Int,
    type: Int = 0,
    onCloseClick: () -> Unit = {},
) {
    val safeInitialSelectedIndex = initialSelectedIndex.coerceIn(0, (options.lastIndex).coerceAtLeast(0))
    var currentSelectedIndex by rememberSaveable(sectionLabel, initialSelectedIndex, options.size) {
        mutableStateOf(safeInitialSelectedIndex)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ReminderScreenColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ReminderScreenColor)
        ) {
            ToolDetailTopBar(
                title = headerTitle,
                onBackClick = onCloseClick,
                type = type
            )

            Text(
                text = screenTitle,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .background(ReminderSectionColor),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = sectionLabel,
                    color = ReminderMutedTextColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 21.dp)
                )
            }

            options.forEachIndexed { index, option ->
                ReminderTimeOptionRow(
                    option = option,
                    selected = index == currentSelectedIndex,
                    onClick = { currentSelectedIndex = index }
                )
            }
        }
    }
}

@Composable
private fun ReminderTimeOptionRow(
    option: ReminderChoiceItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = ReminderStrokeColor, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ReminderRowHeight)
                .clickable(onClick = onClick)
                .padding(start = 21.dp, end = 21.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = if (option.supportingLabel != null) Alignment.Top else Alignment.CenterVertically
        ) {
            if (option.supportingLabel != null) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = option.label,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = option.supportingLabel,
                        color = ReminderMutedTextColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            } else {
                Text(
                    text = option.label,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            if (selected) {
                Icon(
                    imageVector = MaterialCheckIcon,
                    contentDescription = "Selected reminder time",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
            } else {
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}

fun buildReminderChoiceItems(
    labels: List<String>,
    highlightedLabel: String? = null,
    highlightedSupportingLabel: String = "recommended",
): List<ReminderChoiceItem> {
    val normalizedHighlightedLabel = highlightedLabel?.trim()?.lowercase()

    return labels.map { label ->
        ReminderChoiceItem(
            label = label,
            supportingLabel = if (label.lowercase() == normalizedHighlightedLabel) highlightedSupportingLabel else null
        )
    }
}

fun buildDefaultReminderChoiceItems(
    highlightedLabel: String? = null,
): List<ReminderChoiceItem> {
    return buildReminderChoiceItems(
        labels = listOf("At wake up", "15 minutes", "30 minutes", "1 hour", "2 hours"),
        highlightedLabel = highlightedLabel
    )
}

fun findInitialReminderChoiceIndex(
    options: List<ReminderChoiceItem>,
    selectedLabel: String,
): Int {
    val normalizedSelectedLabel = selectedLabel.trim().lowercase()
    return options.indexOfFirst { it.label.lowercase() == normalizedSelectedLabel }
        .takeIf { it >= 0 }
        ?: 0
}

@Preview(showBackground = true, backgroundColor = 0xFF222328, widthDp = 402, heightDp = 874)
@Composable
private fun ReminderTimeScreenPreview() {
    SleeptrackerTheme {
        ReminderTimeScreen(
            headerTitle = "Your Peaks And Dips",
            screenTitle = "Reminder time",
            sectionLabel = "AFTER WAKE",
            options = buildDefaultReminderChoiceItems(highlightedLabel = "1 hour"),
            initialSelectedIndex = 3,
        )
    }
}
