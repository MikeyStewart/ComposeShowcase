package com.mikeystewart.ipod

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.atan2

@Composable
fun IPodScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var previousSelectedIndex by remember { mutableIntStateOf(0) }
    var lastAngle by remember { mutableStateOf<Float?>(null) }
    val rotationThreshold = 15f // degrees needed before moving to next/previous item
    val listHeight = 360.dp
    val itemsPerPage = 10
    val listState = rememberLazyListState()

    var firstVisibleItemIndex by remember { mutableIntStateOf(0) }
    val lastVisibleItemIndex by remember { derivedStateOf { firstVisibleItemIndex + itemsPerPage - 1 } }
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                firstVisibleItemIndex = index
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .safeContentPadding()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(listHeight)
                .background(MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(trackList) { index, item ->
                ListItem(
                    index = index,
                    title = item.first,
                    artist = item.second,
                    selected = selectedIndex == index,
                    modifier = Modifier.height(listHeight / itemsPerPage)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // Initialize lastAngle when user touches the screen
                            lastAngle = offset.angleFromCenter(size)
                        },
                        onDragEnd = {
                            // Reset lastAngle when user lifts their finger
                            lastAngle = null
                        },
                        onDragCancel = {
                            lastAngle = null
                        },
                        onDrag = { change, _ ->
                            val currentAngle = change.position.angleFromCenter(size)
                            lastAngle?.let { previous ->
                                val delta = (currentAngle - previous + 540) % 360 - 180

                                if (delta > rotationThreshold) {
                                    selectedIndex = (selectedIndex + 1).coerceIn(0, trackList.size - 1)
                                    lastAngle = currentAngle // reset after move
                                } else if (delta < -rotationThreshold) {
                                    selectedIndex = (selectedIndex - 1).coerceIn(0, trackList.size - 1)
                                    lastAngle = currentAngle // reset after move
                                }
                            }
                        }
                    )
                },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(96.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape),
            )

            // For debugging
            Column {
                Text("Selected index: $selectedIndex")
                Text("First visible index: $firstVisibleItemIndex")
                Text("Last visible index: $lastVisibleItemIndex")
            }
        }
    }
}

@Composable
fun ListItem(
    index: Int,
    title: String,
    artist: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (selected) Color(0xFF007AFF) else Color.Unspecified)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "${index + 1}. $title - $artist",
            maxLines = 1,
            overflow = TextOverflow.Visible,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground,
        )
    }
}


// Helper to calculate angle from the center of the wheel
private fun Offset.angleFromCenter(size: androidx.compose.ui.unit.IntSize): Float {
    val center = Offset(size.width / 2f, size.height / 2f)
    val angle = atan2(
        y - center.y,
        x - center.x
    ) * (180f / Math.PI)
    return ((angle + 360) % 360).toFloat()
}

val trackList = listOf(
    "Blinding Lights" to "The Weeknd",
    "Levitating" to "Dua Lipa",
    "Watermelon Sugar" to "Harry Styles",
    "Dance Monkey" to "Tones and I",
    "drivers license" to "Olivia Rodrigo",
    "Bad Guy" to "Billie Eilish",
    "Circles" to "Post Malone",
    "Don't Start Now" to "Dua Lipa",
    "Sunflower" to "Post Malone & Swae Lee",
    "Señorita" to "Shawn Mendes & Camila Cabello",
    "Good 4 U" to "Olivia Rodrigo",
    "Someone You Loved" to "Lewis Capaldi",
    "As It Was" to "Harry Styles",
    "Montero (Call Me By Your Name)" to "Lil Nas X",
    "Anti-Hero" to "Taylor Swift",
    "Believer" to "Imagine Dragons",
    "Thunder" to "Imagine Dragons",
    "Happier Than Ever" to "Billie Eilish",
    "bad guy" to "Billie Eilish",
    "Lovely" to "Billie Eilish & Khalid",
    "Without Me" to "Halsey",
    "Memories" to "Maroon 5",
    "Attention" to "Charlie Puth",
    "Light Switch" to "Charlie Puth",
    "abcdefu" to "GAYLE",
    "Riptide" to "Vance Joy",
    "Take Me To Church" to "Hozier",
    "Circles" to "Post Malone",
    "Uptown Funk" to "Mark Ronson ft. Bruno Mars",
    "24K Magic" to "Bruno Mars",
    "Locked Out of Heaven" to "Bruno Mars",
    "Treasure" to "Bruno Mars",
    "Happy" to "Pharrell Williams",
    "Get Lucky" to "Daft Punk ft. Pharrell Williams"
)

@Preview(showBackground = true)
@Composable
private fun IpodPreview() {
    IPodScreen()
}