package com.example.quranspacedrepetition.feature_settings.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_settings.domain.model.UiText

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditPageRangeDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    startPage: String,
    endPage: String,
    startPageError: UiText?,
    endPageError: UiText?,
    onStartPageChanged: (String) -> Unit,
    onEndPageChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    if (!isVisible) return
    val noTextErrors = startPageError == null && endPageError == null
    AlertDialog(
        modifier = modifier,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmClicked,
                enabled = noTextErrors
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        icon = { Icon(imageVector = Icons.Outlined.Edit, contentDescription = null) },
        title = { Text(stringResource(R.string.edit_start_and_end_page)) },
        text = {
            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Column {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = startPage,
                    onValueChange = onStartPageChanged,
                    label = { Text(stringResource(R.string.start_page)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    isError = startPageError != null,
                    supportingText = if (startPageError != null) { { Text(startPageError.asString()) } } else null,
                    trailingIcon = if (startPageError != null) { { Icon(imageVector = Icons.Outlined.Error, contentDescription = null) } } else null,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = endPage,
                    onValueChange = onEndPageChanged,
                    label = { Text(stringResource(R.string.end_page)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (noTextErrors)
                                onConfirmClicked()
                        }
                    ),
                    singleLine = true,
                    isError = endPageError != null,
                    supportingText = if (endPageError != null) { { Text(endPageError.asString()) } } else null,
                    trailingIcon = if (endPageError != null) { { Icon(imageVector = Icons.Outlined.Error, contentDescription = null) } } else null,
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewEditPageRangeDialog() {
    EditPageRangeDialog(
        isVisible = true,
        startPage = "1",
        endPage = "2",
        startPageError = null,
        endPageError = null,
        onStartPageChanged = { },
        onEndPageChanged = { },
        onDismissRequest = { },
        onConfirmClicked = { },
    )
}