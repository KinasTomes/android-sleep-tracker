package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SharedTopBarColor = Color(0xFF222328)
private val SharedTopBarStrokeColor = Color(0xFF55565C)

@Composable
internal fun PeaksTopBar(
    title: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SharedTopBarColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 21.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            if (trailingContent != null) {
                trailingContent()
            }
        }

        HorizontalDivider(
            color = SharedTopBarStrokeColor,
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF222328)
@Composable
private fun PeaksTopBarPreview() {
    SleeptrackerTheme {
        PeaksTopBar(title = "Evening Routine")
    }
}
