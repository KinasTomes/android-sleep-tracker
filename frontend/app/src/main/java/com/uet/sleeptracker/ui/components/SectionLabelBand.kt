package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DefaultSectionLabelBandColor = Color(0xFF17151D)
private val DefaultSectionLabelTextColor = Color(0xFFBABABA)

@Composable
fun SectionLabelBand(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DefaultSectionLabelBandColor,
    textColor: Color = DefaultSectionLabelTextColor,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 21.dp)
        )
    }
}
