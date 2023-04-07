package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GradePageDialogOption(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: () -> Unit,
    emoji: String,
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
            text = emoji,
            fontSize = 18.sp,
        )
        Spacer(Modifier.width(8.dp))
        Text(description)
    }
}