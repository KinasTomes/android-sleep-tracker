package com.uet.sleeptracker.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.*

@Composable
fun GoalSelectableItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: String, // Emoji or icon placeholder
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) SleepPrimary else SleepOutlineLight.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = SleepSurface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(
                        text = icon,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = if (isSelected) SleepPrimary else Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = isSelected && description != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                description?.let {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun <T> MultiSelectionGoalList(
    items: List<T>,
    selectedItems: Set<T>,
    onItemToggle: (T) -> Unit,
    itemLabel: (T) -> String,
    itemIcon: (T) -> String,
    modifier: Modifier = Modifier,
    itemDescription: (T) -> String? = { null }
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { item ->
            GoalSelectableItem(
                text = itemLabel(item),
                isSelected = selectedItems.contains(item),
                onClick = { onItemToggle(item) },
                icon = itemIcon(item),
                description = itemDescription(item)
            )
        }
    }
}
