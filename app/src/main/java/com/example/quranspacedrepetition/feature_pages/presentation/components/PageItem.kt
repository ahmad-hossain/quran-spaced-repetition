package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quranspacedrepetition.feature_pages.domain.model.Page

@Composable
fun PageItem(
    modifier: Modifier = Modifier,
    page: Page
) {
    Row(modifier) {
        TableCell(text = page.pageNumber.toString(), weight = 1f)
        TableCell(text = page.interval.toString(), weight = 1f)
        TableCell(text = page.repetitions.toString(), weight = 1f)
        TableCell(text = page.formattedDueDate, weight = 1f)
    }
}