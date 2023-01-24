package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun PagesScreen(
    navigator: DestinationsNavigator,
    viewModel : PagesViewModel = hiltViewModel(),
) {

}
