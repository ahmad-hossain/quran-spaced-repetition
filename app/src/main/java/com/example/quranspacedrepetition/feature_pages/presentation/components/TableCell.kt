package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TableCell(
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    text: String,
    weight: Float
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .border(1.dp, Color.Gray)
            .weight(weight)
            .padding(8.dp)
    )
}