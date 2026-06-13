package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthConnectPermissionScreen(
    onTurnOnClick: () -> Unit,
    onWhyClick: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var allowAll by remember { mutableStateOf(false) }
    var allowSleep by remember { mutableStateOf(false) }
    var allowSteps by remember { mutableStateOf(false) }
    var allowActivity by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Sync Allow All state to sub-options
    LaunchedEffect(allowAll) {
        if (allowAll) {
            allowSleep = true
            allowSteps = true
            allowActivity = true
        }
    }

    // Sync sub-options back to Allow All state
    LaunchedEffect(allowSleep, allowSteps, allowActivity) {
        if (!allowSleep || !allowSteps || !allowActivity) {
            if (allowAll) {
                allowAll = false
            }
        } else if (allowSleep && allowSteps && allowActivity) {
            allowAll = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleepBackground)
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.8f))

        Text(
            text = stringResource(id = R.string.perm_health_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = SleepPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.perm_health_desc),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        HealthConnectLogo()

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.perm_health_instruction_bold),
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        )
        
        Text(
            text = stringResource(id = R.string.perm_health_instruction_regular),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.weight(1.5f))

        SleepPrimaryOutlinedButton(
            text = stringResource(id = R.string.perm_health_button),
            onClick = { showSheet = true },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onWhyClick) {
            Text(
                text = stringResource(id = R.string.perm_health_why),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = SleepPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF131314),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(width = 36.dp, height = 4.dp)
                        .background(Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                )
            },
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.health_connect),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Health Connect",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Quyền truy cập vào Health Connect",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Chọn dữ liệu bạn muốn chia sẻ với SleepTracker",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E20), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cho phép tất cả",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Switch(
                            checked = allowAll,
                            onCheckedChange = { allowAll = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFA8C7FA),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF323234)
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )

                    Text(
                        text = "CHO PHÉP SLEEPTRACKER ĐỌC",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌙", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Giấc ngủ",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                Text(
                                    text = "Đọc dữ liệu giấc ngủ của bạn",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Switch(
                            checked = allowSleep,
                            onCheckedChange = { allowSleep = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFA8C7FA),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF323234)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👣", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Bước đi",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                Text(
                                    text = "Đọc số bước hàng ngày của bạn",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Switch(
                            checked = allowSteps,
                            onCheckedChange = { allowSteps = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFA8C7FA),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF323234)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🏃", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Hoạt động thể chất",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                Text(
                                    text = "Đọc các bài tập và vận động",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Switch(
                            checked = allowActivity,
                            onCheckedChange = { allowActivity = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFA8C7FA),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF323234)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { showSheet = false }
                    ) {
                        Text(
                            text = "Từ chối",
                            color = Color(0xFFA8C7FA),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            showSheet = false
                            if (allowSleep || allowSteps || allowActivity) {
                                onTurnOnClick()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA8C7FA),
                            contentColor = Color(0xFF131314)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Cho phép",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthConnectLogo() {
    Surface(
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Image(
            painter = painterResource(id = R.drawable.health_connect),
            contentDescription = "Health Connect",
            modifier = Modifier.fillMaxSize().padding(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HealthConnectPermissionScreenPreview() {
    SleeptrackerTheme {
        HealthConnectPermissionScreen(onTurnOnClick = {}, onWhyClick = {})
    }
}
