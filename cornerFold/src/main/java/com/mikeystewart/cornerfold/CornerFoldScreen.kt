package com.mikeystewart.cornerfold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CornerFoldScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Hello World") }
    var textContainerSizePercentage: Float by remember { mutableFloatStateOf(1f) }

    Scaffold { insets ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
        ) {
            BasicText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(textContainerSizePercentage)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                text = text,
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = 999999.sp
                ),
            )

//            Text(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .fillMaxSize(textContainerSizePercentage)
//                    .background(MaterialTheme.colorScheme.primaryContainer),
//                text = text,
//                fontSize = 32.sp
//            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
            ) {
                Slider(
                    value = textContainerSizePercentage,
                    onValueChange = { textContainerSizePercentage = it },
                    valueRange = 0.1f..1f
                )
                Text(textContainerSizePercentage.toString())
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                )
            }
        }
    }
}