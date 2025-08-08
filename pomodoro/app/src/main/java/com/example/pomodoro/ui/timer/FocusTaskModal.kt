package com.example.pomodoro.ui.timer

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pomodoro.R
import com.example.pomodoro.data.pomodoro.FocusTaskUiState
import com.example.pomodoro.data.tags.Tag
import com.example.pomodoro.ui.theme.BlueDarkWoodsmoke
import com.example.pomodoro.ui.theme.LightRed
import com.example.pomodoro.ui.theme.Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusTaskModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    tagsUiState: TagsUiState,
    focusTaskUiState: FocusTaskUiState,
    saveFocusTaskUiState: (FocusTaskUiState) -> Unit,
    deleteTag: (Tag) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var currentTagId by remember { mutableIntStateOf(focusTaskUiState.tagId) }
    var currentTaskName by remember { mutableStateOf(focusTaskUiState.taskName) }
    val spacePadding = dimensionResource(id = R.dimen.padding_pre_medium)

    ModalBottomSheet(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.6F)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(spacePadding)
        ) {
            FocusTaskModalTopBar(
                modifier = Modifier.fillMaxWidth(),
                titleRes = R.string.focus_task,
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible)
                            onDismissRequest()
                    }
                }
            )
            FocusTaskModalTagsPanel(
                modifier = Modifier.fillMaxWidth(),
                tags = tagsUiState.tags,
                currentTagId = currentTagId,
                changeCurrentTagId = { currentTagId = it },
                onDeleteTagClick = {
                    deleteTag(
                        tagsUiState.tags.find { tag ->
                            tag.id == currentTagId
                        } ?: Tag(name = "Focus")
                    )
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.padding_pre_medium)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(id = R.string.task),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
            TextFieldInput(
                modifier = Modifier.fillMaxWidth(),
                value = currentTaskName,
                onValueChange = { currentTaskName = it }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    saveFocusTaskUiState(
                        FocusTaskUiState(
                            tagId = currentTagId,
                            tagName = (tagsUiState.tags.find {
                                tag -> tag.id == currentTagId
                            } ?: Tag(name = "Focus")).name,
                            taskName = currentTaskName
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun FocusTaskModalTopBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    onClick: () -> Unit = {}
) {
    val iconSize: Dp = dimensionResource(id = R.dimen.min_touch_size)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = titleRes).uppercase(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        IconButton(
            modifier = Modifier.size(iconSize),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun FocusTaskModalTagsPanel(
    modifier: Modifier = Modifier,
    tags: List<Tag>,
    currentTagId: Int,
    changeCurrentTagId: (Int) -> Unit,
    onDeleteTagClick: () -> Unit
) {
    var showTagEntryDialog by remember { mutableStateOf(value = false) }

    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.tags),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val iconSize: Dp = dimensionResource(id = R.dimen.min_touch_size)
                    val iconModifier: Modifier = remember { Modifier.size(iconSize) }

                    IconButton(
                        modifier = iconModifier,
                        onClick = { showTagEntryDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_box),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        modifier = iconModifier,
                        onClick = onDeleteTagClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = null,
                            tint = LightRed
                        )
                    }
                }
            }
        }
        TaskTagsList(
            modifier = Modifier.fillMaxWidth(),
            tags = tags,
            currentTagId = currentTagId,
            changeCurrentTagId = { changeCurrentTagId(it) }
        )
    }

    AnimatedVisibility(
        visible = showTagEntryDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TagEntryDialog(
            checkTagInTags = { tag -> tags.none { tag.name == it.name } },
            onDismissRequest = { showTagEntryDialog = false }
        )
    }
}

@Composable
fun TextFieldInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    val maxValueLength = remember { 24 }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { if (it.length <= maxValueLength) onValueChange(it) },
        singleLine = false,
        maxLines = 1,
        textStyle = MaterialTheme.typography.labelMedium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(10.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small)),
            text = "${value.length}/24",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskTagsList(
    modifier: Modifier = Modifier,
    tags: List<Tag>,
    currentTagId: Int,
    changeCurrentTagId: (Int) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        for (tag in tags) {
            TaskTag(
                modifier = Modifier.padding(horizontal = 2.dp),
                text = tag.name,
                active = currentTagId == tag.id,
                onButtonClick = { changeCurrentTagId(tag.id) }
            )
        }
    }
}

@Composable
fun TaskTag(
    modifier: Modifier = Modifier,
    text: String = "",
    active: Boolean,
    onButtonClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (active) Theme.specialColor else MaterialTheme.colorScheme.surface,
        label = "анимация фона кнопки добавления задачи"
    )
    val borderColor by animateColorAsState(
        targetValue = if (active) Color.Transparent else Theme.specialColor,
        label = "анимация обводки кнопки добавления задачи"
    )
    val contentColor by animateColorAsState(
        targetValue = if (active) BlueDarkWoodsmoke else MaterialTheme.colorScheme.onBackground,
        label = "анимация цвета кнопки добавления задачи"
    )

    OutlinedButton(
        modifier = modifier,
        onClick = onButtonClick,
        shape = RoundedCornerShape(10.dp),
        border =  BorderStroke(1.dp, color = borderColor),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (active) {
                Icon(
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.padding_pre_medium)),
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = null,
                    tint = contentColor
                )
            }

            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        }
    }
}