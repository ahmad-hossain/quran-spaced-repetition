package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.destinations.SettingsScreenDestination
import com.example.quranspacedrepetition.feature_pages.presentation.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate
import kotlin.math.roundToInt

private val FabHeight = 56.dp
private val ScaffoldFabSpacing = 16.dp
val BottomBarHeight = 80.dp

enum class UiTabs {
    TODAY, ALL
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@RootNavGraph(start = true)
@Destination
@Composable
fun PagesScreen(
    navigator: DestinationsNavigator,
    viewModel: PagesViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val lazyListState = rememberLazyListState()
    val isListScrolled by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(Unit) {
        viewModel.scrollToIndex.collect {
            lazyListState.scrollToItem(it)
        }
    }

    GradePageDialog(
        isVisible = state.isGradeDialogVisible,
        onDismissRequest = { viewModel.onEvent(PagesEvent.GradeDialogDismissed) },
        onConfirm = { viewModel.onEvent(PagesEvent.GradeDialogConfirmed) },
        onDismiss = { viewModel.onEvent(PagesEvent.GradeDialogDismissed) },
        selectedGrade = state.selectedGrade,
        onSelectGrade = { viewModel.onEvent(PagesEvent.GradeSelected(it)) },
    )
    SearchDialog(
        isVisible = state.isSearchDialogVisible,
        searchQuery = state.searchQuery,
        searchQueryHasError = state.searchQueryHasError,
        onSearchQueryChanged = { viewModel.onEvent(PagesEvent.SearchQueryChanged(it)) },
        onSearchDialogConfirmed = { viewModel.onEvent(PagesEvent.SearchDialogConfirmed) },
        onSearchDialogDismissed = { viewModel.onEvent(PagesEvent.SearchDialogDismissed) },
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomBarHeightPx = with(LocalDensity.current) { BottomBarHeight.roundToPx().toFloat() }
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val deltaY = available.y
                val newOffset = bottomBarOffsetHeightPx.value + deltaY
                bottomBarOffsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.revision)) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = if (isListScrolled) MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) else MaterialTheme.colorScheme.surface,
                )
            )
        },
        bottomBar = {
            CustomBottomBar(
                modifier = Modifier.offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) },
                currentScreen = Screen.Home,
                onSettingsClicked = { navigator.navigate(SettingsScreenDestination) },
            )
        },
        floatingActionButton = {
            if (state.displayedPages.isEmpty()) return@Scaffold
            FloatingActionButton(
                modifier = Modifier.offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) },
                onClick = { viewModel.onEvent(PagesEvent.SearchFabClicked) },
                content = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) }
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                    )
                )
        ) {
            TabsSection(
                containerColor = if (isListScrolled) MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) else TabRowDefaults.containerColor,
                selectedTab = state.selectedTab,
                onTabClicked = { viewModel.onEvent(PagesEvent.TabClicked(it)) }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = BottomBarHeight + FabHeight + ScaffoldFabSpacing * 2),
                state = lazyListState
            ) {
                stickyHeader {
                    if (state.displayedPages.isNotEmpty())
                        TableHeader()
                }
                items(state.displayedPages) { page ->
                    val shouldGradePage = page.dueDate == null || page.dueDate.toEpochDay() <= LocalDate.now().toEpochDay()
                    PageItem(
                        modifier =
                        if (shouldGradePage) Modifier.clickable { viewModel.onEvent(PagesEvent.PageClicked(page)) } else Modifier.alpha(0.38f),
                        page = page
                    )
                }
            }
        }
    }
}

@Composable
private fun TabsSection(
    selectedTab: UiTabs,
    onTabClicked: (UiTabs) -> Unit,
    containerColor: Color,
) {
    TabRow(
        containerColor = containerColor,
        selectedTabIndex = selectedTab.ordinal,
    ) {
        Tab(
            selected = selectedTab == UiTabs.TODAY,
            onClick = { onTabClicked(UiTabs.TODAY) },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Outlined.Today, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.today))
                }
            },
        )
        Tab(
            selected = selectedTab == UiTabs.ALL,
            onClick = { onTabClicked(UiTabs.ALL) },
            text = { Text(text = stringResource(R.string.all)) },
        )
    }
}

@Composable
private fun TableHeader() {
    val bold = FontWeight.Bold
    Column {
        Row(Modifier.background(MaterialTheme.colorScheme.background)) {
            TableCell(fontWeight = bold, text = stringResource(R.string.page_number_abbrev), weight = 1f)
            TableCell(fontWeight = bold, text = stringResource(R.string.interval), weight = 1f)
            TableCell(fontWeight = bold, text = stringResource(R.string.repetitions), weight = 1f)
            TableCell(fontWeight = bold, text = stringResource(R.string.due_date), weight = 1f)
        }
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}