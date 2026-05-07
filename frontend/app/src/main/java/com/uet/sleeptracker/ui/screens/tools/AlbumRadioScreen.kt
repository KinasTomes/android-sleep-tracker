package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

data class Track(
    val title: String,
    val artist: String
)

@Composable
fun AlbumRadioScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val tracks = listOf(
        Track("Elenore", "The Turtle"),
        Track("Hey Moon", "John Maus"),
        Track("Home With You", "FKA twigs"),
        Track("Hound Dog", "Elvis Presley"),
        Track("Lost Cause", "Billie Eilish"),
        Track("Cardigan", "Taylor Swift"),
        Track("WILDFLOWER", "Billie Eilish")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                AlbumRadioTopBar(onBackClick = onBackClick)

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.coming_next_header),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    items(tracks) { track ->
                        TrackListItem(track = track)
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(200.dp))
                    }
                }
            }

            NowPlayingCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
private fun AlbumRadioTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "‹",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.album_radio_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TrackListItem(
    track: Track,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = track.artist,
                    color = SleepOnSurfaceVariant,
                    fontSize = 15.sp
                )
            }
            DragHandleIcon()
        }
        HorizontalDivider(color = Color(0xFF1C1C1E), thickness = 0.5.dp)
    }
}

@Composable
private fun DragHandleIcon() {
    Column(
        modifier = Modifier.size(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(1.5.dp)
                    .background(Color(0xFF3A3A3C))
            )
            if (it < 2) Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
private fun NowPlayingCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF1C1C1E),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.current_song_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.current_album_title),
                color = SleepOnSurfaceVariant,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Bar
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFF3A3A3C), RoundedCornerShape(1.5.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight()
                            .background(Color.White, RoundedCornerShape(1.5.dp))
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = 132.dp) // Manual positioning for 40%
                            .background(Color.White, CircleShape)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "0:38", color = SleepOnSurfaceVariant, fontSize = 12.sp)
                    Text(text = "3:16", color = SleepOnSurfaceVariant, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PlaybackControls()
        }
    }
}

@Composable
private fun PlaybackControls(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle
        PlaybackIcon(color = SleepPrimary, type = "shuffle")
        
        // Previous
        PlaybackIcon(color = Color.White, type = "prev")
        
        // Play
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Black, CircleShape) // Simplified Play icon
            )
        }
        
        // Next
        PlaybackIcon(color = Color.White, type = "next")
        
        // Repeat
        PlaybackIcon(color = Color.White, type = "repeat")
    }
}

@Composable
private fun PlaybackIcon(
    color: Color,
    type: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            "shuffle" -> {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color.copy(alpha = 0.2f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(16.dp).border(1.5.dp, color, RoundedCornerShape(2.dp)))
                }
            }
            "prev" -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(2.dp, 12.dp).background(color))
                    Box(modifier = Modifier.size(10.dp).border(1.5.dp, color, CircleShape))
                }
            }
            "next" -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).border(1.5.dp, color, CircleShape))
                    Box(modifier = Modifier.size(2.dp, 12.dp).background(color))
                }
            }
            "repeat" -> {
                Box(modifier = Modifier.size(18.dp).border(1.5.dp, color, CircleShape))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AlbumRadioScreenPreview() {
    SleeptrackerTheme {
        AlbumRadioScreen()
    }
}
