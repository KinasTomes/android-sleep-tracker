package com.uet.sleeptracker.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val SuggestionCardShape = RoundedCornerShape(12.dp)
private val SuggestionCardBorderColor = Color(0xFF363636)
private val SuggestionCardBottomColor = Color(0xFF16151A)
private val SuggestionCardSubtitleColor = Color(0xFF989898)

enum class SleepSuggestionCardTextStyle {
    Subtitle,
    Details,
}

data class SleepSuggestionCardDetailLine(
    val annotatedText: androidx.compose.ui.text.AnnotatedString,
    val color: Color,
) {
    constructor(text: String, color: Color) : this(
        annotatedText = androidx.compose.ui.text.AnnotatedString(text),
        color = color
    )

    val text: String get() = annotatedText.text
}

@Composable
fun SleepSuggestionCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes backgroundImageResId: Int,
    subtitle: String? = null,
    textStyle: SleepSuggestionCardTextStyle = SleepSuggestionCardTextStyle.Subtitle,
    detailLines: List<SleepSuggestionCardDetailLine> = emptyList(),
) {
    Box(
        modifier = modifier
            .size(width = 175.dp, height = 120.dp)
            .clip(SuggestionCardShape)
            .border(width = 1.dp, color = SuggestionCardBorderColor, shape = SuggestionCardShape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = backgroundImageResId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(SuggestionCardBottomColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 11.dp, top = 11.dp, end = 11.dp, bottom = 10.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                when (textStyle) {
                    SleepSuggestionCardTextStyle.Subtitle -> {
                        subtitle?.let {
                            Text(
                                text = it,
                                color = SuggestionCardSubtitleColor,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 3.dp)
                            )
                        }
                    }

                    SleepSuggestionCardTextStyle.Details -> {
                        Spacer(modifier = Modifier.height(3.dp))

                        detailLines.forEach { line ->
                            Text(
                                text = line.annotatedText,
                                color = line.color,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SleepSuggestionCardPreview() {
    SleeptrackerTheme {
        SleepSuggestionCard(
            title = "Write a brain dump",
            subtitle = "Ease your mind before bed",
            backgroundImageResId = R.drawable.write_a_brain_dump,
            onClick = {}
        )
    }
}
