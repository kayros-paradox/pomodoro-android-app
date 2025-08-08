package com.example.pomodoro.ui.menu

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pomodoro.R
import com.example.pomodoro.data.pomodoro.PomodoroPhase
import com.example.pomodoro.service.PomodoroService
import com.example.pomodoro.service.pomodoroServiceIntent

@Composable
fun PhaseEditorPanel(modifier: Modifier = Modifier) {
    var editablePhase by remember { mutableStateOf(PomodoroPhase.POMODORO) }
    var showEditorPanel by remember { mutableStateOf(value = false) }

    val spacerModifier: Modifier = Modifier
        .width(dimensionResource(id = R.dimen.phase_editor_panel_spacer_width))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = showEditorPanel,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(durationMillis = 100)
                ) togetherWith fadeOut(
                    animationSpec = tween(durationMillis = 100)
                )
            },
            label = "animated editor panel"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val buttonModifier = remember {
                    Modifier.weight(1F).aspectRatio(ratio = 1F)
                }
                when {
                    it -> {
                        EditPhasePanel(
                            modifier = Modifier.fillMaxWidth(),
                            buttonModifier = buttonModifier,
                            spacerModifier = spacerModifier,
                            editablePhase = editablePhase,
                            onSaveClick = { showEditorPanel = !showEditorPanel }
                        )
                    }
                    else -> {
                        ChangePhasePanel(
                            modifier = Modifier.fillMaxWidth(),
                            buttonModifier = buttonModifier,
                            spacerModifier = spacerModifier,
                            onChangePhaseClick = { phase ->
                                editablePhase = phase
                                showEditorPanel = !showEditorPanel
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditPhasePanel(
    modifier: Modifier,
    buttonModifier: Modifier,
    spacerModifier: Modifier,
    editablePhase: PomodoroPhase,
    onSaveClick: () -> Unit,
) {
    val context: Context = LocalContext.current
    var minutes by remember { mutableIntStateOf(editablePhase.minutes) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        EditPhaseButton(
            modifier = buttonModifier,
            iconId = R.drawable.remove
        ) {
            if (minutes > 1) {
                minutes--
                editablePhase.minutes = minutes
                context.pomodoroServiceIntent(
                    PomodoroService.Action.UpdatePhase,
                    editablePhase.name
                )
            }
        }
        Spacer(modifier = spacerModifier)
        ChangePhaseButton(
            modifier = buttonModifier,
            phaseMinutes = minutes,
            phaseNameId = editablePhase.nameId,
            onClick = onSaveClick
        )
        Spacer(modifier = spacerModifier)
        EditPhaseButton(
            modifier = buttonModifier,
            iconId = R.drawable.add
        ) {
            if (minutes <= 999) {
                minutes++
                editablePhase.minutes = minutes
                context.pomodoroServiceIntent(
                    PomodoroService.Action.UpdatePhase,
                    editablePhase.name
                )
            }
        }
    }
}

@Composable
fun EditPhaseButton(
    modifier: Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    @DrawableRes iconId: Int,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "edit minutes of phase"
            )
        }
    }
}

@Composable
fun ChangePhasePanel(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier,
    spacerModifier: Modifier,
    onChangePhaseClick: (PomodoroPhase) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ChangePhaseButton(
            modifier = buttonModifier,
            phaseMinutes = PomodoroPhase.BREAK.minutes,
            phaseNameId = PomodoroPhase.BREAK.nameId,
            onClick = { onChangePhaseClick(PomodoroPhase.BREAK) }
        )
        Spacer(modifier = spacerModifier)
        ChangePhaseButton(
            modifier = buttonModifier,
            phaseMinutes = PomodoroPhase.POMODORO.minutes,
            phaseNameId = PomodoroPhase.POMODORO.nameId,
            onClick = { onChangePhaseClick(PomodoroPhase.POMODORO) }
        )
        Spacer(modifier = spacerModifier)
        ChangePhaseButton(
            modifier = buttonModifier,
            phaseMinutes = PomodoroPhase.REST.minutes,
            phaseNameId = PomodoroPhase.REST.nameId,
            onClick = { onChangePhaseClick(PomodoroPhase.REST) }
        )
    }
}

@Composable
fun ChangePhaseButton(
    modifier: Modifier = Modifier,
    phaseMinutes: Int,
    @StringRes phaseNameId: Int,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = phaseMinutes.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
            Text(
                text = stringResource(id = phaseNameId).uppercase(),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}