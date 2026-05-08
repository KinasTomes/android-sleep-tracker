package com.uet.sleeptracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.AppBottomNavigation
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun HomeScreen(
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToTools: () -> Unit = {}
) {
    val currentTime = remember {
        SimpleDateFormat("HH:mm").format(Date())
    }
    
    val dateRange = remember {
        val calendar = Calendar.getInstance()
        val endDate = SimpleDateFormat("MMM d").format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val startDate = SimpleDateFormat("MMM d").format(calendar.time)
        "$startDate - $endDate"
    }

    Scaffold(
        containerColor = SleepBackground,
        bottomBar = {
            AppBottomNavigation(
                selectedTab = "home",
                onNavigateToTools = onNavigateToTools
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Fixed Top Bar
            HomeTopBar(
                currentTime = currentTime,
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToProfile = onNavigateToProfile
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Scrollable Content
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Title and Date Range
                    Text(
                        text = "My Sleep",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                    
                    Text(
                        text = dateRange,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 16.sp
                        )
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Sleep Debt Visualization
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder for sleep debt visualization
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .background(Color(0xFF2C2C2E), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "3.6",
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 56.sp
                                    )
                                )
                                Text(
                                    text = "hrs",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 18.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Sleep debt",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
                
                item {
                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Sleep Debt Since Yesterday
                        Column {
                            Text(
                                text = "1.8hrs debt",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            Text(
                                text = "Since yesterday",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            )
                        }
                        
                        // Energy Potential
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF2C2C2E)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Energy Potential",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "⚡",
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "75%",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color(0xFF4ADE80),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                item {
                    // Total Sleep Time Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2C2C2E)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "8h 1m",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                                Text(
                                    text = "Total Sleep Time",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 14.sp
                                    )
                                )
                            }
                            
                            Text(
                                text = "23:35 - 7:36",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
                
                item {
                    // Naps Today Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2C2C2E)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Naps Today",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            
                            Text(
                                text = "45m",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontSize = 24.sp
                                )
                            )
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(
    currentTime: String,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentTime,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontSize = 20.sp
            )
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = onNavigateToNotifications) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = SleepPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = onNavigateToProfile) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = SleepPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HomeScreenPreview() {
    SleeptrackerTheme {
        HomeScreen()
    }
}
