package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R

private val ToolDetailTopBarColor = Color(0xFF111111)
private val ToolDetailTopBarColorType1 = Color(0xFF222328)
private val ToolDetailTopBarDividerColor = Color(0xFF363636)

@Composable
fun ToolDetailTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: Int = 0,
) {
    val backgroundColor = if (type == 1) ToolDetailTopBarColorType1 else ToolDetailTopBarColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (type == 1) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp)
                )

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C2C2E))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(36.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onBackClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(36.dp)
                    )
                }

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 42.dp)
                )
            }
        }

        HorizontalDivider(
            color = ToolDetailTopBarDividerColor,
            thickness = 1.dp
        )
    }
}
