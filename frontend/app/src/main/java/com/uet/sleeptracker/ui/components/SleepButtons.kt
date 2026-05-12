package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepPrimary
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SleepPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 64.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        color = Color.White
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SleepPrimary,
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(text = text, style = textStyle)
    }
}

@Composable
fun SleepPrimaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 64.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        color = Color.White
    )
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, SleepPrimary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(text = text, style = textStyle)
    }
}
