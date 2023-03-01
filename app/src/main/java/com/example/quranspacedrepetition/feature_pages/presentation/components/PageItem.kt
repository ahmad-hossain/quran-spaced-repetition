package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quranspacedrepetition.feature_pages.domain.model.Page

@Composable
fun PageItem(
    modifier: Modifier = Modifier,
    page: Page
) {
    Column {
        Row(modifier) {
            TableCell(text = page.pageNumber.toString(), weight = 1f)
            TableCell(text = page.interval.toString(), weight = 1f)
            TableCell(text = page.repetitions.toString(), weight = 1f)
            TableCell(text = page.relativeDueDate, weight = 1f)
        }
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}