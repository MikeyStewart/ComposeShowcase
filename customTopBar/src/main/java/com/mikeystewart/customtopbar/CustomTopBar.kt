package com.mikeystewart.customtopbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.util.fastFirst
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBarScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                titleText = "Custom top bar!",
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            (0..100).forEach {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(42.dp)
                    ) {
                        Text("$it")
                    }
                }
            }
        }
    }
}

// region mess

// Two interpolation functions used for some calculations below
fun lerp(start: Int, end: Int, progress: Float?): Int {
    val clampedProgress = (progress ?: 0f).coerceIn(0f, 1f)
    return (start + (end - start) * clampedProgress).roundToInt()
}
fun lerp(start: Float, end: Float, progress: Float?): Float {
    val clampedProgress = (progress ?: 0f).coerceIn(0f, 1f)
    return start + (end - start) * clampedProgress
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    titleText: String,
    collapsedHeight: Dp = 64.dp, // standard top bar height
    expandedHeight: Dp = 160.dp, // random number I used just for building/testing this thing
    scrollBehavior: TopAppBarScrollBehavior?
) {
    require(collapsedHeight.isSpecified && collapsedHeight.isFinite) {
        "The collapsedHeight is expected to be specified and finite"
    }
    require(expandedHeight.isSpecified && expandedHeight.isFinite) {
        "The expandedHeight is expected to be specified and finite"
    }
    require(expandedHeight >= collapsedHeight) {
        "The expandedHeight is expected to be greater or equal to the collapsedHeight"
    }
    val expandedHeightPx: Float
    val collapsedHeightPx: Float
    LocalDensity.current.run {
        expandedHeightPx = expandedHeight.toPx()
        collapsedHeightPx = collapsedHeight.toPx()
    }

    // Sets the app bar's height offset limit to hide just the bottom title area and keep top title
    // visible when collapsed.
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != collapsedHeightPx - expandedHeightPx) {
            scrollBehavior?.state?.heightOffsetLimit = collapsedHeightPx - expandedHeightPx
        }
    }

    // Set up support for resizing the top app bar when vertically dragging the bar itself.
    val appBarDragModifier =
        if (scrollBehavior != null && !scrollBehavior.isPinned) {
            Modifier.draggable(
                orientation = Orientation.Vertical,
                state =
                rememberDraggableState { delta -> scrollBehavior.state.heightOffset += delta },
                onDragStopped = { velocity ->
                    settleAppBar(
                        scrollBehavior.state,
                        velocity,
                        scrollBehavior.flingAnimationSpec,
                        scrollBehavior.snapAnimationSpec
                    )
                }
            )
        } else {
            Modifier
        }

    // This block is used to animate the 'scroll' when focussing/clicking the search bar
    val progress = remember { Animatable(0f) }
    LaunchedEffect(progress.value) {
        // we can set the offset of the scroll state programatically
        scrollBehavior?.state?.heightOffset = progress.value
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(scrollBehavior?.state?.collapsedFraction) {
        if ((scrollBehavior?.state?.collapsedFraction ?: 0f) <= 0f) {
            progress.snapTo(0f)
        }
    }

    Surface(modifier = modifier.then(appBarDragModifier)) {
        Column {
            Layout(
                {
                    Box(
                        Modifier
                            .layoutId("navigationIcon")
                            .padding(start = TopAppBarHorizontalPadding)
                            .size(48.dp)
                            .background(color = Color.Red.copy(alpha = 0.2f))
                            .border(color = Color.Red, width = 1.dp)
                    )
                    Box(
                        Modifier
                            .layoutId("title")
                            .alpha(lerp(1f, 0f, (scrollBehavior?.state?.collapsedFraction ?: 0f) * 2)) // times 2 so it hides quicker
                            .padding(horizontal = TopAppBarHorizontalPadding)
                            .background(color = Color.Cyan.copy(alpha = 0.2f))
                            .border(color = Color.Cyan, width = 1.dp)
                    ) {
                        Text(
                            text = titleText,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }

                    Box(
                        Modifier
                            .layoutId("searchBar")
                            .padding(horizontal = TopAppBarHorizontalPadding, vertical = TopAppBarVerticalContentPadding)
                            .height(42.dp)
                            .fillMaxWidth()
                            .background(color = Color.Magenta.copy(alpha = 0.2f))
                            .border(color = Color.Magenta, width = 1.dp)
                            .clickable {
                                scope.launch {
                                    // triggers the scroll animation
                                    progress.animateTo(targetValue = scrollBehavior?.state?.heightOffsetLimit ?: 0f)
                                }
                            }
                    ) {
                        Text(
                            text = "Search... Collapsed fraction: ${scrollBehavior?.state?.collapsedFraction}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                modifier = Modifier
                    .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                    // clip after padding so we don't show the title over the inset area
                    .clipToBounds()
                    .heightIn(max = expandedHeight)
                    .border(color = Color.Green, width = 1.dp)
            ) { measurables, constraints ->
                val navigationIconPlaceable =
                    measurables
                        .fastFirst { it.layoutId == "navigationIcon" }
                        .measure(constraints.copy(minWidth = 0))

                val titlePlaceable =
                    measurables
                        .fastFirst { it.layoutId == "title" }
                        .measure(constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth))

                // Search bar width calculation
                val maxSearchBarWidthExpanded = constraints.maxWidth
                val maxSearchBarWidthCollapsed = constraints.maxWidth - navigationIconPlaceable.width
                val maxSearchBarWidth = lerp(maxSearchBarWidthCollapsed, maxSearchBarWidthExpanded, 1 - (scrollBehavior?.state?.collapsedFraction ?: 0f))

                // Starting X position of search bar calculation
                val searchBarStartExpanded = constraints.minWidth
                val searchBarStartCollapsed = constraints.minWidth + navigationIconPlaceable.width
                val searchBarStart = lerp(searchBarStartCollapsed, searchBarStartExpanded, 1 - (scrollBehavior?.state?.collapsedFraction ?: 0f))

                val searchBarPlaceable =
                    measurables
                        .fastFirst { it.layoutId == "searchBar" }
                        .measure(constraints.copy(minWidth = 0, maxWidth = maxSearchBarWidth))

                // Subtract the scrolledOffset from the maxHeight. The scrolledOffset is expected to be
                // equal or smaller than zero.
                val scrolledOffsetValue = scrollBehavior?.state?.heightOffset ?: 0f
                val heightOffset = if (scrolledOffsetValue.isNaN()) 0 else scrolledOffsetValue.roundToInt()

                val layoutHeight =
                    if (constraints.maxHeight == Constraints.Infinity) {
                        constraints.maxHeight
                    } else {
                        constraints.maxHeight + heightOffset
                    }

                layout(constraints.maxWidth, layoutHeight) {
                    // Navigation icon
                    navigationIconPlaceable.placeRelative(
                        x = 0,
                        y = (collapsedHeightPx.roundToInt() - navigationIconPlaceable.height) / 2,
                    )

                    // SearchBar
                    searchBarPlaceable.placeRelative(
                        x = searchBarStart,
                        y = layoutHeight - searchBarPlaceable.height,
                    )

                    // Title
                    titlePlaceable.placeRelative(
                        x = constraints.minWidth,
                        y = layoutHeight - searchBarPlaceable.height - titlePlaceable.height,
                    )
                }
            }
        }
    }
}

// this is straight copied from the M3 impl
// it's used to animate the top bar to either collapsed or expanded state if user stops scroll while it's inbetween states
@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
    // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
    // and just return Zero Velocity.
    // Note that we don't check for 0f due to float precision with the collapsedFraction
    // calculation.
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 && state.heightOffset > state.heightOffsetLimit) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) {
                state.heightOffset = value
            }
        }
    }

    return Velocity(0f, remainingVelocity)
}

// Couple of semi-random values added for spacing
private val TopAppBarHorizontalPadding = 4.dp
private val TopAppBarVerticalContentPadding = 4.dp