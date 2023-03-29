package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat

@Composable
fun GradePageDialogOption(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: () -> Unit,
    grade: Int,
    descriptionRes: Int,
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
        Text(htmlStringResource(descriptionRes))
    }
}

/**
 * Converts a [Spanned] into an [AnnotatedString] trying to keep as much formatting as possible.
 *
 * Currently supports `bold`, `italic`, `underline` and `color`.
 */
fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic), start, end)
            }
            is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
            is ForegroundColorSpan -> addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
        }
    }
}

@Composable
fun htmlStringResource(@StringRes resourceId: Int, vararg formatArgs: Any): AnnotatedString {
    val htmlString = stringResource(id = resourceId, *formatArgs)
    return HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT).toAnnotatedString()
}