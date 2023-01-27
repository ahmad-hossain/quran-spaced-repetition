package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TextRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: () -> Unit,
    text: String,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(text)
    }
}