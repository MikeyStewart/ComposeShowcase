package com.mikeystewart.composeshowcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikeystewart.composeshowcase.ui.theme.ComposeShowcaseTheme
import com.mikeystewart.cornerfold.CornerFoldScreen
import com.mikeystewart.customtopbar.CustomTopBarScreen
import com.mikeystewart.expressive.ExpressiveScreen
import com.mikeystewart.ipod.IPodScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeShowcaseTheme {
                ComposeShowcase()
            }
        }
    }
}

@Composable
fun ComposeShowcase() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ShowcaseList,
    ) {
        composable<ShowcaseList> {
            ShowcaseList { destination -> navController.navigate(destination) }
        }
        composable<IPod> { IPodScreen() }
        composable<CornerFold> { CornerFoldScreen() }
        composable<CustomTopBar> { CustomTopBarScreen() }
        composable<Expressive> { ExpressiveScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeShowcasePreview() {
    ComposeShowcaseTheme {
        ComposeShowcase()
    }
}
