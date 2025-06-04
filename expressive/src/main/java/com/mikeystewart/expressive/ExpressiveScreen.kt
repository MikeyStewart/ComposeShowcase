package com.mikeystewart.expressive

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.MergeType
import androidx.compose.material.icons.automirrored.filled.MultilineChart
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.AppBarColumn
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("M3 Expressive")
                }
            )
        }
    ) { insets ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HorizontalDivider() }
            item { ButtonGroupExample() }

            item { HorizontalDivider() }
            item { LoadingIndicatorExample() }

            item { HorizontalDivider() }
            item { FloatingToolbarExample() }

            item { HorizontalDivider() }
            item { SplitButtonExample() }

            item { HorizontalDivider() }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SplitButtonExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Split Button",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleLargeEmphasized
        )

        Text(text = "SplitButtonLayout() + DropdownMenu()", modifier = Modifier.padding(top = 16.dp))
        SplitButtonWithMenu()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SplitButtonWithMenu() {
    var checked by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()) {
        SplitButtonLayout(
            leadingButton = {
                SplitButtonDefaults.LeadingButton(
                    onClick = { /* Do Nothing */ },
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        modifier = Modifier.size(SplitButtonDefaults.LeadingIconSize),
                        contentDescription = "Localized description",
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("My Button")
                }
            },
            trailingButton = {
                SplitButtonDefaults.TrailingButton(
                    checked = checked,
                    onCheckedChange = { checked = it },
                ) {
                    val rotation: Float by
                    animateFloatAsState(
                        targetValue = if (checked) 180f else 0f,
                        label = "Trailing Icon Rotation"
                    )
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        modifier =
                            Modifier
                                .size(SplitButtonDefaults.TrailingIconSize)
                                .graphicsLayer {
                                    this.rotationZ = rotation
                                },
                        contentDescription = "Localized description"
                    )
                }
            }
        )

        DropdownMenu(expanded = checked, onDismissRequest = { checked = false }) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { /* Handle edit! */ },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = { /* Handle settings! */ },
                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                onClick = { /* Handle send feedback! */ },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingToolbarExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Floating Toolbar",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleLargeEmphasized
        )

        Text(text = "HorizontalFloatingToolbar()", modifier = Modifier.padding(top = 16.dp))
        HorizontalFloatingToolbarExample()

        Text(text = "HorizontalFloatingToolbar() with FAB", modifier = Modifier.padding(top = 16.dp))
        HorizontalFloatingToolbarWithFabExample()

        Text(text = "VerticalFloatingToolbar()", modifier = Modifier.padding(top = 16.dp))
        VerticalFloatingToolbarExample()

        Text(text = "VerticalFloatingToolbar() with FAB", modifier = Modifier.padding(top = 16.dp))
        VerticalFloatingToolbarWithFabExample()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VerticalFloatingToolbarWithFabExample() {
    var expanded by rememberSaveable { mutableStateOf(true) }
    VerticalFloatingToolbar(
        modifier = Modifier,
        expanded = expanded,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        },
        content = {
            AppBarColumn(
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Default.QueueMusic,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Queue"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.MergeType,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Merge"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ManageSearch,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "manage search"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.MultilineChart,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "chart"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HorizontalFloatingToolbarWithFabExample() {
    var expanded by rememberSaveable { mutableStateOf(true) }
    HorizontalFloatingToolbar(
        modifier = Modifier,
        expanded = expanded,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        },
        content = {
            AppBarRow(
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Default.QueueMusic,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Queue"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.MergeType,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Merge"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ManageSearch,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "manage search"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.MultilineChart,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "chart"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VerticalFloatingToolbarExample() {
    var expanded by rememberSaveable { mutableStateOf(true) }
    VerticalFloatingToolbar(
        modifier = Modifier,
        expanded = expanded,
        leadingContent = {
            AppBarColumn(
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Back"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Forward"
                )
            }
        },
        trailingContent = {
            AppBarColumn(
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Download,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Download"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Favorite"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Person"
                )
            }
        },
        content = {
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HorizontalFloatingToolbarExample() {
    var expanded by rememberSaveable { mutableStateOf(true) }
    HorizontalFloatingToolbar(
        modifier = Modifier,
        expanded = expanded,
        leadingContent = {
            AppBarRow(
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Back"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Forward"
                )
            }
        },
        trailingContent = {
            AppBarRow(
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                }
            ) {
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Download,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Download"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Favorite"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Add"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "Person"
                )
                clickableItem(
                    onClick = { /* doSomething() */ },
                    icon = {
                        Icon(
                            Icons.Filled.ArrowUpward,
                            contentDescription = "Localized description"
                        )
                    },
                    label = "ArrowUpward"
                )
            }
        },
        content = {
            FilledIconButton(
                modifier = Modifier.width(64.dp),
                onClick = { expanded = !expanded }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonGroupExample() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Button Group",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleLargeEmphasized
        )

        Text(text = "ButtonRow() + clickableItem()", modifier = Modifier.padding(top = 16.dp))
        ClickableButtonGroup()

        Text(text = "ButtonRow() + toggleableItem()", modifier = Modifier.padding(top = 16.dp))
        ToggleableButtonGroup()

        Text(text = "Row() + ToggleButton()", modifier = Modifier.padding(top = 16.dp))
        SingleSelectButtonGroup()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingIndicatorExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Loading Indicator",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleLargeEmphasized
        )

        Text(text = "LoadingIndicator(), indeterminate", modifier = Modifier.padding(top = 16.dp))
        LoadingIndicator()

        val progress = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(2000)
                progress.animateTo(1f, animationSpec = tween(5000))
                delay(2000)
                progress.snapTo(0f)
            }
        }
        Text(text = "LoadingIndicator(), determinate\n(progress: ${progress.value})", modifier = Modifier.padding(top = 16.dp))
        LoadingIndicator(
            progress = { progress.value },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SingleSelectButtonGroup() {
    val options = listOf("One", "Two", "Cafe")
    val unCheckedIcons =
        listOf(Icons.Outlined.Work, Icons.Outlined.Restaurant, Icons.Outlined.Coffee)
    val checkedIcons = listOf(Icons.Filled.Work, Icons.Filled.Restaurant, Icons.Filled.Coffee)
    var selectedIndex by remember { mutableIntStateOf(0) }

    Row(
        Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        val modifiers = listOf(Modifier.weight(1f), Modifier.weight(1.5f), Modifier.weight(1f))

        options.forEachIndexed { index, label ->
            ToggleButton(
                checked = selectedIndex == index,
                onCheckedChange = { selectedIndex = index },
                modifier = modifiers[index],
                shapes =
                    when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    }
            ) {
                Icon(
                    if (selectedIndex == index) checkedIcons[index] else unCheckedIcons[index],
                    contentDescription = "Localized description"
                )
                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                Text(label)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ClickableButtonGroup() {
    val numButtons = 10
    ButtonGroup(
        overflowIndicator = { menuState ->
            FilledIconButton(
                onClick = {
                    if (menuState.isExpanded) {
                        menuState.dismiss()
                    } else {
                        menuState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
        }
    ) {
        for (i in 0 until numButtons) {
            clickableItem(onClick = {}, label = "$i")
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToggleableButtonGroup() {
    val numButtons = 4
    var checked by remember { mutableIntStateOf(0) }
    ButtonGroup(
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        overflowIndicator = { menuState ->
            FilledIconButton(
                onClick = {
                    if (menuState.isExpanded) {
                        menuState.dismiss()
                    } else {
                        menuState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
        }
    ) {
        (0..numButtons).forEach { button ->
            toggleableItem(
                checked = checked == button,
                onCheckedChange = { checked = button },
                label = "$button",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpressivePreview() {
    ExpressiveScreen()
}