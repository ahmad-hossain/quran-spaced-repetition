package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GradePageDialogOption(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: () -> Unit,
    grade: Int,
    description: String,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = grade.toString(),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Text(modifier = Modifier.padding(horizontal = 4.dp), text = "-")
        Text(description)
    }
}