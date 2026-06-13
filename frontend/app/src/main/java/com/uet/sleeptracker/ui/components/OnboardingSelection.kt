package com.uet.sleeptracker.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.*

@Composable
fun SelectableItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) SleepSurfaceSelected else Color.Transparent)
            .padding(vertical = 20.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) SleepPrimary else SleepOutlineLight.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isSelected) Color.White else SleepSurfaceSelected,
                            CircleShape
                        )
                )
            }
        }

        AnimatedVisibility(
            visible = isSelected && description != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            description?.let {
                Column {
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

@Composable
fun <T> SelectionList(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    itemDescription: (T) -> String? = { null }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, SleepOutlineLight.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        items.forEachIndexed { index, item ->
            SelectableItem(
                text = itemLabel(item),
                isSelected = item == selectedItem,
                onClick = { onItemSelected(item) },
                description = itemDescription(item)
            )
            if (index < items.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    color = Color.Black,
                    thickness = 1.dp
                )
            }
        }
    }
}
