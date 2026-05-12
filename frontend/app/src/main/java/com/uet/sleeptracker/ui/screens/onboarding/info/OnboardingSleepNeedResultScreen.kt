package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingSleepNeedResultScreen(
    nightsCount: Int = 0,
    onLooksRight: () -> Unit,
    onHowCalculated: () -> Unit,
    onAdjustSleepNeed: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SleepBackground)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp)
                .padding(top = 96.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingSleepNeedResultGauge(
                currentNeed = stringResource(id = R.string.sleep_need_result_value),
                progress = 0.375f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.sleep_need_result_prefix))
                    append("\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.sleep_need_result_bold))
                    }
                },
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontSize = 22.sp,
                    lineHeight = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = { showSheet = true }) {
                Text(
                    text = stringResource(id = R.string.sleep_need_result_how_calculated),
                    color = SleepPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            TextButton(onClick = onAdjustSleepNeed) {
                Text(
                    text = stringResource(id = R.string.sleep_need_result_adjust),
                    color = SleepPrimary,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SleepPrimaryOutlinedButton(
                text = stringResource(id = R.string.sleep_need_result_confirm),
                onClick = onLooksRight,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFF1C1C1E),
                dragHandle = null,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                SleepNeedCalculationBottomSheetContent(
                    nightsCount = nightsCount,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            showSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SleepNeedCalculationBottomSheetContent(
    nightsCount: Int,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = "What's sleep need?",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Based on ",
                color = Color.Gray,
                fontSize = 18.sp
            )
            Surface(
                shape = CircleShape,
                color = SleepPrimary,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = nightsCount.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = " nights of sleep",
                color = Color.Gray,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "We estimate your sleep need by comparing how much sleep you usually get versus when you sleep longer than usual—something we call a sleep rebound. This is because your body tries to make up the sleep it needs when you sleep longer than usual.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.LightGray,
                fontSize = 17.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun OnboardingSleepNeedResultGauge(
    currentNeed: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SleepPrimary)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.58f)
                        .height(32.dp)
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF4E1A9E))
                ) {
                    Text(
                        text = stringResource(id = R.string.sleep_need_most_people),
                        color = Color.White.copy(alpha = 0.35f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                GaugeTimeLabel(
                    text = stringResource(id = R.string.sleep_need_result_min),
                    modifier = Modifier.align(Alignment.TopStart),
                    isPrimary = false
                )
                GaugeTimeLabel(
                    text = stringResource(id = R.string.sleep_need_result_most_people_start),
                    modifier = Modifier.align(BiasAlignment(horizontalBias = -0.5f, verticalBias = -1f)),
                    isPrimary = true
                )
                GaugeTimeLabel(
                    text = stringResource(id = R.string.sleep_need_result_most_people_end),
                    modifier = Modifier.align(BiasAlignment(horizontalBias = 0.66f, verticalBias = -1f)),
                    isPrimary = true
                )
                GaugeTimeLabel(
                    text = stringResource(id = R.string.sleep_need_result_max),
                    modifier = Modifier.align(Alignment.TopEnd),
                    isPrimary = false
                )
            }
        }

        // The indicator now uses a larger offset and zIndex to ensure it overlaps the gauge bar correctly
        Column(
            modifier = Modifier
                .align(BiasAlignment(horizontalBias = progress * 2 - 1, verticalBias = -1f))
                .offset(y = 28.dp)
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentNeed,
                color = SleepPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(50.dp)
                    .background(Color.White.copy(alpha = 0.85f))
            )
        }
    }
}

@Composable
private fun GaugeTimeLabel(
    text: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean
) {
    Text(
        text = text,
        color = Color.White.copy(alpha = if (isPrimary) 0.82f else 0.26f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier
    )
}

@Preview(showSystemUi = true, backgroundColor = 0xFF000000)
@Composable
fun OnboardingSleepNeedResultScreenPreview() {
    SleeptrackerTheme {
        OnboardingSleepNeedResultScreen(
            onLooksRight = {},
            onHowCalculated = {},
            onAdjustSleepNeed = {}
        )
    }
}
