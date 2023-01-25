package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quranspacedrepetition.feature_pages.domain.model.Page

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageItem(
    modifier: Modifier = Modifier,
    page: Page
) {
    ListItem(
        modifier = modifier,
        leadingContent = { Text(page.pageNumber.toString()) },
        headlineText = {},
        trailingContent = { Text(page.repetitions.toString()) },
    )
}