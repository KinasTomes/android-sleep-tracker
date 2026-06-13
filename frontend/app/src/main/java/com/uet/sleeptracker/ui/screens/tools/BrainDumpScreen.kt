package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.TodoAddItemCard
import com.uet.sleeptracker.ui.components.TodoItemCard
import com.uet.sleeptracker.ui.components.ToolDetailTopBar
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val BrainDumpBackground = Color(0xFF16151A)
private val BrainDumpHeroStart = Color(0xFF0C0C0A)
private val BrainDumpHeroEnd = Color(0xFF175ECF)
private val BrainDumpFooterText = Color.White.copy(alpha = 0.6f)
private val BrainDumpFooterDivider = Color(0xFF777777)

@Composable
fun BrainDumpScreen(
    modifier: Modifier = Modifier,
    todos: List<com.uet.sleeptracker.data.BrainDumpTodoItem> = emptyList(),
    onTodoAdd: (String) -> Unit = {},
    onTodoCheckedChange: (index: Int, checked: Boolean) -> Unit = { _, _ -> },
    onTodoDelete: (id: Int) -> Unit = {},
    onCloseClick: () -> Unit = {},
    onSetReminderClick: () -> Unit = {},
) {
    var inputValue by remember { mutableStateOf("") }
    val addInputFocusRequester = remember { FocusRequester() }

    fun submitTodo() {
        val newText = inputValue.trim()
        if (newText.isEmpty()) return
        onTodoAdd(newText)
        inputValue = ""
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BrainDumpBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BrainDumpBackground)
        ) {
            ToolDetailTopBar(
                title = "Brain Dump",
                onBackClick = onCloseClick,
                type = 1
            )

            BrainDumpHero()

            todos.forEachIndexed { index, todo ->
                TodoItemCard(
                    text = todo.text,
                    completed = todo.completed,
                    onCheckedChange = { checked ->
                        onTodoCheckedChange(index, checked)
                    },
                    onDeleteClick = {
                        onTodoDelete(todo.id)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(color = Color(0xFF1A1A1D), thickness = 1.dp)
            }

            TodoAddItemCard(
                value = inputValue,
                onValueChange = { inputValue = it },
                onSubmit = ::submitTodo,
                focusRequester = addInputFocusRequester,
                onClick = { addInputFocusRequester.requestFocus() },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = BrainDumpFooterDivider, thickness = 1.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSetReminderClick
                        )
                        .padding(bottom = 112.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bell),
                        contentDescription = null,
                        tint = SleepPrimary,
                        modifier = Modifier.size(32.dp)
                    )

                    Text(
                        text = "Set a reminder",
                        color = SleepPrimary,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 14.dp)
                    )

                    Text(
                        text = "to review your list in the morning",
                        color = BrainDumpFooterText,
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 23.dp)
                    )
                }
            }
        }
    }
}



@Composable
private fun BrainDumpHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BrainDumpHeroStart,
                        BrainDumpHeroEnd
                    )
                )
            )
            .padding(horizontal = 21.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.write_logo),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(42.dp)
        )

        Text(
            text = "Write a brain dump",
            color = Color.White,
            fontSize = 20.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 64.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF16151A, widthDp = 476, heightDp = 956)
@Composable
private fun BrainDumpScreenPreview() {
    SleeptrackerTheme {
        BrainDumpScreen()
    }
}
