package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val TodoCardBackground = Color(0xFF16151A)
private val TodoAddCardBackground = Color(0xFF202328)
private val TodoDeleteBackground = Color(0xFF1F212E)
private val TodoAccent = Color(0xFF00EDB9)
private val TodoBorder = Color.White.copy(alpha = 0.92f)
private val TodoPlaceholder = Color.White.copy(alpha = 0.8f)

@Composable
fun TodoItemCard(
    text: String,
    completed: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(TodoCardBackground)
            .padding(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TodoCheckButton(
                checked = completed,
                onClick = { onCheckedChange(!completed) }
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        if (completed) {
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TodoDeleteBackground)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDeleteClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bin),
                    contentDescription = "Delete todo",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun TodoAddItemCard(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Write a to-do to calm your mind",
    focusRequester: FocusRequester? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(TodoAddCardBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(start = 12.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TodoCheckButton(
            checked = false,
            onClick = onClick
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
                .then(
                    if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier
                )
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                        onSubmit()
                        true
                    } else {
                        false
                    }
                },
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TodoPlaceholder,
                        fontSize = 16.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun TodoCheckButton(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .border(
                    width = 1.4.dp,
                    color = if (checked) TodoAccent else TodoBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = TodoAccent,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 500, heightDp = 120)
@Composable
private fun TodoItemCardIncompletePreview() {
    SleeptrackerTheme {
        TodoItemCard(
            text = "Play games",
            completed = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 500, heightDp = 120)
@Composable
private fun TodoItemCardCompletePreview() {
    SleeptrackerTheme {
        TodoItemCard(
            text = "Play games",
            completed = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 500, heightDp = 120)
@Composable
private fun TodoAddItemCardPreview() {
    SleeptrackerTheme {
        TodoAddItemCard(
            value = "",
            onValueChange = {},
            onSubmit = {}
        )
    }
}
