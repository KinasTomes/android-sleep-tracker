package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    subTitleContent: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 8.dp,
    contentHeight: Dp = 64.dp,
) {
    val textSize = 20.sp
    val hasSubTitle = subTitle != null || subTitleContent != null

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .padding(top = 6.dp, start = horizontalPadding, end = horizontalPadding, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = if (hasSubTitle) Arrangement.spacedBy(2.dp) else Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mainTitle,
                    color = Color.White,
                    fontSize = textSize,
                    lineHeight = textSize,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subTitleContent != null) {
                    subTitleContent()
                } else if (subTitle != null) {
                    Text(
                        text = subTitle,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = textSize,
                        lineHeight = textSize,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
        TopDivider()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ScreenTopbarPreview() {
    ScreenTopBar(mainTitle = "My Sleep", subTitle = "May 17-30")
}
