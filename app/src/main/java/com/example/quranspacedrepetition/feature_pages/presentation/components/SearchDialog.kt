package com.example.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import com.example.quranspacedrepetition.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
    searchQuery: String,
    searchQueryHasError: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearchDialogConfirmed: () -> Unit,
    onSearchDialogDismissed: () -> Unit,
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onSearchDialogDismissed,
        confirmButton = {
            TextButton(
                onClick = onSearchDialogConfirmed,
                enabled = !searchQueryHasError
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onSearchDialogDismissed) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        icon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
        title = { Text(text = stringResource(R.string.search)) },
        text = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = searchQueryHasError,
                supportingText = if (searchQueryHasError) {{ Text(stringResource(R.string.invalid_number)) }} else null,
                trailingIcon = if (searchQueryHasError) {{ Icon(imageVector = Icons.Outlined.Error, contentDescription = null) }} else null,
            )
        }
    )
}