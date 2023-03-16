package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.ahmad_hossain.quranspacedrepetition.R

@Composable
fun PermissionDialog(
    icon: Painter,
    permission: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmClicked: () -> Unit,
    onDismissed: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(R.string.allow) + " ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.app_name))
                    }
                    append(" ${stringResource(id = R.string.to)} $permission?")
                },
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center
            )
        },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirmClicked) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissed) {
                Text(dismissButtonText)
            }
        },
    )
}

@Preview
@Composable
fun PreviewPermissionDialog() {
    PermissionDialog(
        icon = painterResource(id = R.drawable.ic_alarm_filled),
        permission = "Schedule Alarms",
        text = "Quran Hifz Revision needs the Alarm permission to schedule notifications.",
        confirmButtonText = "Go to Settings",
        dismissButtonText = "Cancel",
        onConfirmClicked = {},
        onDismissed = {}
    )
}