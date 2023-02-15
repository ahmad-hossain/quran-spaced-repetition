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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.presentation.components.GradePageDialog
import com.example.quranspacedrepetition.feature_pages.presentation.components.PageItem
import com.example.quranspacedrepetition.feature_pages.presentation.components.SearchDialog
import com.example.quranspacedrepetition.feature_pages.presentation.components.TableCell
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private val FabHeight = 56.dp
private val ScaffoldFabSpacing = 16.dp

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
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.revision)) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (state.displayedPages.isEmpty()) return@Scaffold
            FloatingActionButton(
                onClick = { viewModel.onEvent(PagesEvent.SearchFabClicked) },
                content = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) }
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = if (state.isTodayChipSelected) 0 else 1,
            ) {
                Tab(
                    selected = state.isTodayChipSelected,
                    onClick = { viewModel.onEvent(PagesEvent.TodayChipClicked) },
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
                    selected = state.isAllChipSelected,
                    onClick = { viewModel.onEvent(PagesEvent.AllChipClicked) },
                    text = { Text(text = stringResource(R.string.all)) },
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = FabHeight + ScaffoldFabSpacing * 2),
                state = lazyListState
            ) {
                stickyHeader {
                    if (state.displayedPages.isNotEmpty())
                        TableHeader()
                }
                items(state.displayedPages) { page ->
                    PageItem(
                        modifier = Modifier.clickable { viewModel.onEvent(PagesEvent.PageClicked(page)) },
                        page = page
                    )
                }
            }
        }
    }
}

@Composable
private fun TableHeader() {
    val bold = FontWeight.Bold
    Row(Modifier.background(MaterialTheme.colorScheme.background)) {
        TableCell(fontWeight = bold, text = stringResource(R.string.page_number_abbrev), weight = 1f)
        TableCell(fontWeight = bold, text = stringResource(R.string.interval), weight = 1f)
        TableCell(fontWeight = bold, text = stringResource(R.string.repetitions), weight = 1f)
        TableCell(fontWeight = bold, text = stringResource(R.string.due_date), weight = 1f)
    }
}