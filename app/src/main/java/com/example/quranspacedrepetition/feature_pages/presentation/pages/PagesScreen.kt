package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.presentation.components.PageItem
import com.example.quranspacedrepetition.feature_pages.presentation.components.TableCell
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun PagesScreen(
    navigator: DestinationsNavigator,
    viewModel: PagesViewModel = hiltViewModel(),
) {
    val state = viewModel.state

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.pages)) }) }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {

            Spacer(modifier = Modifier.padding(8.dp))

            LazyColumn {
                item {
                    FilterChipsSection()
                }
                stickyHeader {
                    TableHeader()
                }
                items(state.displayedPages) { page ->
                    PageItem(page = page)
                }
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
            modifier = Modifier.weight(1f),
            selected = state.isTodayChipSelected,
            onClick = { viewModel.onEvent(PagesEvent.TodayChipClicked) },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Today, contentDescription = null) },
            label = { Text(text = stringResource(R.string.today)) },
        )
        Spacer(modifier = Modifier.padding(8.dp))
        FilterChip(
            modifier = Modifier.weight(1f),
            selected = state.isAllChipSelected,
            onClick = { viewModel.onEvent(PagesEvent.AllChipClicked) },
            label = { Text(text = stringResource(R.string.all)) },
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
