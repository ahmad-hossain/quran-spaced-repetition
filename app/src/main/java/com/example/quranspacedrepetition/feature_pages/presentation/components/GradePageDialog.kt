package com.example.quranspacedrepetition.feature_pages.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
            text = stringResource(R.string.grade_5_desc),
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 4,
            text = stringResource(R.string.grade_4_desc),
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 3,
            text = stringResource(R.string.grade_3_desc),
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 2,
            text = stringResource(R.string.grade_2_desc),
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 1,
            text = stringResource(R.string.grade_1_desc),
            onSelectGrade = onSelectGrade,
        ),
        GradeOption(
            grade = 0,
            text = stringResource(R.string.grade_0_desc),
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
        title = { Text(stringResource(R.string.assign_grade)) },
        text = {
            val dividerColor = LocalContentColor.current.copy(alpha = 0.7f)

            Column {
                Divider(color = dividerColor)
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    gradeOptions.forEachIndexed { index, gradeOption ->
                        if (index != 0)
                            Spacer(Modifier.height(6.dp))
                        GradePageDialogOption(
                            selected = selectedGrade == gradeOption.grade,
                            onSelect = { gradeOption.onSelectGrade(gradeOption.grade) },
                            grade = gradeOption.grade,
                            description = gradeOption.text,
                        )
                    }
                }
                Divider(modifier = Modifier.requiredHeight(1.dp), color = dividerColor)
            }
        },
        icon = { Icon(Icons.Outlined.StarRate, null) }
    )
}

@Preview
@Composable
fun PreviewGradingDialog() {
    GradePageDialog(
        onDismissRequest = {},
        onConfirm = {},
        onDismiss = {},
        selectedGrade = 0,
        onSelectGrade = {},
    )
}