package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Shared top bar for main tab screens (Home, Progress, Tools, etc.).
 *
 * @param mainTitle Primary title text (e.g. "My Sleep", "My Progress").
 * @param subTitle Optional subtitle below the title (e.g. date range). When null,
 *   [mainTitle] is aligned to the bottom of the bar.
 * @param actions Right-side action icons slot.
 */
@Composable
fun ScreenTopBar(
    mainTitle: String,
    subTitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = if (subTitle != null) Arrangement.spacedBy(4.dp) else Arrangement.Bottom,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = mainTitle,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            if (subTitle != null) {
                Text(
                    text = subTitle,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}
