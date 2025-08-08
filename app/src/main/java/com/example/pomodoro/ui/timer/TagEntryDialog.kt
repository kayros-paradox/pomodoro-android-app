package com.example.pomodoro.ui.timer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodoro.R
import com.example.pomodoro.data.tags.Tag
import com.example.pomodoro.ui.AppViewModelProvider
import com.example.pomodoro.ui.theme.BlueDarkWoodsmoke
import com.example.pomodoro.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun TagEntryDialog(
    modifier: Modifier = Modifier,
    checkTagInTags: (Tag) -> Boolean,
    onDismissRequest: () -> Unit,
    viewModel: TagEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismissRequest) {
        TagEntryCard(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onSaveTagRequest = {
                if (checkTagInTags(it)) {
                    viewModel.updateUiState(it)
                    scope.launch {
                        viewModel.saveTag()
                        onDismissRequest()
                    }
                }
            }
        )
    }
}

@Composable
fun TagEntryCard(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSaveTagRequest: (Tag) -> Unit
) {
    val maxTagNameLength = 24
    val cardSpacer = dimensionResource(id = R.dimen.card_spacer_medium)
    var tagName by remember { mutableStateOf(value = "") }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(cardSpacer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardTitle(
                modifier = Modifier.fillMaxWidth(),
                titleRes = R.string.new_tag
            )
            CardInputField(
                modifier = Modifier.fillMaxWidth(),
                value = tagName,
                onValueChange = {
                    if (it.length <= maxTagNameLength) {
                        tagName = it
                    }
                }
            )
            CardButtons(
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = onDismissRequest,
                onSaveTagRequest = { onSaveTagRequest(Tag(name = tagName)) }
            )
        }
    }
}

@Composable
fun CardTitle(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = titleRes),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CardInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.enter_tag_name)) },
        textStyle = MaterialTheme.typography.labelMedium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun CardButtons(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSaveTagRequest: () -> Unit
) {
    val buttonModifier = Modifier
        .padding(horizontal = dimensionResource(id = R.dimen.padding_post_medium))

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CancelTagButton(
            modifier = buttonModifier,
            onClick = onDismissRequest
        )
        SaveTagButton(
            modifier = buttonModifier,
            onClick = onSaveTagRequest
        )
    }
}

@Composable
fun CancelTagButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        containerColor = Theme.specialColor,
        contentColor = BlueDarkWoodsmoke,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.task_entry_cancel),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SaveTagButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.task_entry_save),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}