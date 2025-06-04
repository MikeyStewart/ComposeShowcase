package com.mikeystewart.composeshowcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseList(navigateTo: (NavigationDestination) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Inspired by https://dribbble.com/shots/25645360-Music
            ShowcaseItem(
                title = "iPod (WIP)",
                description = "A touchscreen version of the iPod classic's scroll wheel built as a quick UI challenge.",
                onClick = { navigateTo(IPod) }
            )

            ShowcaseItem(
                title = "Corner fold (WIP)",
                description = "A corner fold animation imitating the physical action of folding the corner of a card or page. This feature is 100% compose so it's more flexible and robust than other solutions.",
                onClick = { navigateTo(CornerFold) }
            )

            ShowcaseItem(
                title = "CustomTopBar (WIP)",
                description = "A custom collapsing/animated TopBar. Features fluid view manipulation/animation and an interactive search bar. Loosely based on M3's LargeTopBar.",
                onClick = { navigateTo(CustomTopBar) }
            )

            ShowcaseItem(
                title = "Expressive (WIP)",
                description = "Trying out the new M3 Expressive components. Wahooo!",
                onClick = { navigateTo(Expressive) }
            )
        }
    }
}

@Composable
private fun ShowcaseItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}