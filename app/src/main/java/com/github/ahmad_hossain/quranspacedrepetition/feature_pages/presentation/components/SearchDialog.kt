package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import com.github.ahmad_hossain.quranspacedrepetition.R
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.UiText

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    searchQuery: String,
    searchQueryError: UiText?,
    onSearchQueryChanged: (String) -> Unit,
    onSearchDialogConfirmed: () -> Unit,
    onSearchDialogDismissed: () -> Unit,
) {
    if (!isVisible) return
    AlertDialog(
        modifier = modifier,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onSearchDialogDismissed,
        confirmButton = {
            TextButton(
                onClick = onSearchDialogConfirmed,
                enabled = searchQueryError == null
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
        title = { Text(text = stringResource(R.string.jump_to_page)) },
        text = {
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            val context = LocalContext.current

            OutlinedTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text(text = stringResource(R.string.page_number)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (searchQueryError == null)
                            onSearchDialogConfirmed()
                    }
                ),
                singleLine = true,
                isError = searchQueryError != null,
                supportingText = if (searchQueryError != null) {{ Text(searchQueryError.asString(context)) }} else null,
                trailingIcon = if (searchQueryError != null) {{ Icon(imageVector = Icons.Outlined.Error, contentDescription = null) }} else null,
            )
        }
    )
}