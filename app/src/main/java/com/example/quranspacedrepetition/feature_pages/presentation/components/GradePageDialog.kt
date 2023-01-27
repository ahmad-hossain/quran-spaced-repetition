package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.quranspacedrepetition.R

data class GradeOption(
    val grade: Int,
    val text: String,
    val onSelectGrade: (Int) -> Unit,
)

@Composable
fun GradePageDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    selectedGrade: Int,
    onSelectGrade: (Int) -> Unit,
) {
    val gradeOptions = listOf(
        GradeOption(
            grade = 5,
            text = "5 - perfect response",
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 4,
            text = "4 - correct response after a hesitation",
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 3,
            text = "3 - correct response recalled with serious difficulty",
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 2,
            text = "2 - incorrect response; where the correct one seemed easy to recall",
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 1,
            text = "1 - incorrect response; the correct one remembered",
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 0,
            text = "0 - complete blackout",
            onSelectGrade = onSelectGrade,
        ),
    )

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text("Assign Grade") },
        text = {
            val dividerColor = LocalContentColor.current.copy(alpha = 0.7f)

            Column {
                Divider(color = dividerColor)
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    gradeOptions.forEach {
                        TextRadioButton(
                            selected = selectedGrade == it.grade,
                            onSelect = { it.onSelectGrade(it.grade) },
                            text = it.text,
                        )
                    }
                }
                Divider(modifier = Modifier.requiredHeight(1.dp), color = dividerColor)
            }
        },
        icon = { Icon(Icons.Outlined.StarRate, null) }
    )
}