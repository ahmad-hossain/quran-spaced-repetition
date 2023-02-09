package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    if (state.isGradeDialogVisible) {
        GradePageDialog(
            onDismissRequest = { viewModel.onEvent(PagesEvent.GradeDialogDismissed) },
            onConfirm = { viewModel.onEvent(PagesEvent.GradeDialogConfirmed) },
            onDismiss = { viewModel.onEvent(PagesEvent.GradeDialogDismissed) },
            selectedGrade = state.selectedGrade,
            onSelectGrade = { viewModel.onEvent(PagesEvent.GradeSelected(it)) },
        )
    }
    if (state.isSearchDialogVisible) {
        SearchDialog(
            searchQuery = state.searchQuery,
            searchQueryHasError = state.searchQueryHasError,
            onSearchQueryChanged = { viewModel.onEvent(PagesEvent.SearchQueryChanged(it)) },
            onSearchDialogConfirmed = { viewModel.onEvent(PagesEvent.SearchDialogConfirmed) },
            onSearchDialogDismissed = { viewModel.onEvent(PagesEvent.SearchDialogDismissed) },
        )
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.pages)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(PagesEvent.SearchFabClicked) },
                content = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding),
            contentPadding = PaddingValues(bottom = FabHeight + ScaffoldFabSpacing * 2)
        ) {
            item {
                FilterChipsSection()
            }
            item { Spacer(Modifier.height(8.dp)) }
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterChipsSection(
    modifier: Modifier = Modifier,
    viewModel: PagesViewModel = hiltViewModel(),
) {
    val state = viewModel.state

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        FilterChip(
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            selected = state.isTodayChipSelected,
            onClick = { viewModel.onEvent(PagesEvent.TodayChipClicked) },
            label = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                        imageVector = Icons.Outlined.Today,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.today))
                }
            },
        )
        Spacer(modifier = Modifier.padding(8.dp))
        FilterChip(
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            selected = state.isAllChipSelected,
            onClick = { viewModel.onEvent(PagesEvent.AllChipClicked) },
            label = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    text = stringResource(R.string.all),
                    textAlign = TextAlign.Center
                )
            },
        )
    }
}

@Composable
private fun TableHeader() {
    val bold = FontWeight.Bold
    Row(Modifier.background(MaterialTheme.colorScheme.background)) {
        TableCell(fontWeight = bold, text = stringResource(R.string.page_number), weight = 1f)
        TableCell(fontWeight = bold, text = stringResource(R.string.interval), weight = 1f)
        TableCell(fontWeight = bold, text = stringResource(R.string.repetitions), weight = 1f)
        TableCell(fontWeight = bold, text = stringResource(R.string.due_date), weight = 1f)
    }
}
