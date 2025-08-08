package com.example.pomodoro.ui.timer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodoro.R
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.data.pomodoro.PomodoroPhase
import com.example.pomodoro.data.pomodoro.PomodoroUiState
import com.example.pomodoro.ui.AppViewModelProvider
import com.example.pomodoro.ui.PomodoroTopBar
import com.example.pomodoro.ui.navigation.NavigationDestination

@Immutable
object TimerDestination: NavigationDestination {
    override val route = "Timer"
    val titleRes = R.string.pomodoro
    val iconRes = R.drawable.menu
}

@Composable
fun TimerScreen(
    navigateToMenu: () -> Unit,
    onSwitchOperatingModeClick: () -> Unit,
    onResetTimerClick: () -> Unit,
    updateChosenPhaseOnClick: (PomodoroPhase) -> Unit,
    viewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val showTopBarMenuButton = remember (uiState.operatingMode) {
        uiState.operatingMode.isStopped()
    }

    Scaffold(
        topBar = {
            PomodoroTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
                titleRes = TimerDestination.titleRes,
                iconRes = TimerDestination.iconRes,
                onMenuButtonClick = navigateToMenu,
                showMenuButton = showTopBarMenuButton
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        TimerPanel(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onSwitchOperatingModeClick = onSwitchOperatingModeClick,
            onResetTimerClick = onResetTimerClick,
            updateChosenPhaseOnClick = updateChosenPhaseOnClick,
            uiState = uiState
        )
    }
}

@Composable
fun TimerPanel(
    modifier: Modifier = Modifier,
    onSwitchOperatingModeClick: () -> Unit,
    onResetTimerClick: () -> Unit,
    updateChosenPhaseOnClick: (PomodoroPhase) -> Unit,
    uiState: PomodoroUiState = PomodoroUiState()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        val spacerModifier = remember { Modifier.weight(1F) }
        val maxWidthModifier = remember { Modifier.fillMaxWidth() }
        val enabledResetButton = remember (uiState.operatingMode) {
            uiState.operatingMode.isPaused()
        }
        val isStopped = remember (uiState.operatingMode) {
            uiState.operatingMode.isStopped()
        }
        val textTime = remember (uiState.currentSeconds) {
            uiState.toString
        }

        Spacer(modifier = spacerModifier)
        TaskPanel(modifier = maxWidthModifier)
        Spacer(modifier = spacerModifier)
        TimerCircle(
            darkTheme = Option.NightMode.checked,
            operatingMode = uiState.operatingMode,
            currentSeconds = if (isStopped) 0F else uiState.currentSeconds.toFloat(),
            maxSeconds = uiState.currentPhase.minutes * 60,
            onClick = onSwitchOperatingModeClick
        )
        Spacer(modifier = spacerModifier)
        TimerTimePanel(
            phaseNameRes = uiState.currentPhase.nameId,
            textTime = textTime
        )
        Spacer(modifier = spacerModifier)
        TimerResetButton(
            modifier = maxWidthModifier,
            enabled = enabledResetButton,
            onClick = onResetTimerClick
        )
        Spacer(modifier = spacerModifier)
        PhaseSelectionPanel(
            modifier = maxWidthModifier,
            phases = PomodoroPhase.entries,
            chosenPhase = uiState.chosenPhase,
            enabled = isStopped,
            onClick = updateChosenPhaseOnClick
        )
        Spacer(modifier = spacerModifier)
    }
}

@Composable
fun TaskPanel(
    modifier: Modifier = Modifier,
    viewModel: FocusTaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val tagsUiState by viewModel.tagsUiStateFlow.collectAsStateWithLifecycle()
    val focusTaskUiState by viewModel.focusTaskUiStateFlow.collectAsStateWithLifecycle()
    var showModal by rememberSaveable { mutableStateOf(value = false) }

    AnimatedVisibility(
        visible = Option.ShowTaskbar.checked,
        enter = fadeIn(),
        exit = fadeOut(),
        label = "анимация видимости настройки панели добавления задачи"
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                onClick = { showModal = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.padding_post_medium)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(
                            text = "#${focusTaskUiState.tagName}",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                    Row {
                        Text(
                            text = focusTaskUiState.taskName,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(visible = showModal) {
        FocusTaskModal(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { showModal = false },
            tagsUiState = tagsUiState,
            focusTaskUiState = focusTaskUiState,
            saveFocusTaskUiState = { viewModel.updateFocusTaskUiState(it) },
            deleteTag = { viewModel.deleteTag(it) }
        )
    }
}

@Composable
fun TimerTimePanel(
    modifier: Modifier = Modifier,
    @StringRes phaseNameRes: Int,
    textTime: String
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(phaseNameRes),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = textTime,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.7F)
            )
        }
    }
}

@Composable
fun TimerResetButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    AnimatedContent(
        targetState = enabled,
        transitionSpec = {
            fadeIn(animationSpec = tween()) togetherWith fadeOut(animationSpec = tween())
        },
        label = "анимация кнопки сброса таймера"
    ) {
        when {
            it -> {
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onClick,
                        enabled = enabled,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = R.string.reset),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
            else -> { Spacer(modifier = Modifier.size(48.dp)) }
        }
    }
}

@Composable
fun PhaseSelectionPanel(
    modifier: Modifier = Modifier,
    phases: List<PomodoroPhase>,
    chosenPhase: PomodoroPhase,
    enabled: Boolean,
    onClick: (PomodoroPhase) -> Unit
) {
    val buttonSize = remember { 56.dp }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (phase in phases) {
            val isChosen = remember (chosenPhase) {
                phase == chosenPhase
            }

            PhaseButton(
                modifier = Modifier.size(buttonSize),
                iconRes = if (isChosen) phase.chosenIconId else phase.unChosenIconId,
                contentDescriptionRes = phase.nameId,
                isChosen = isChosen,
                iconTint = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                if (enabled) onClick(phase)
            }
        }
    }
}

@Composable
fun PhaseButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    @StringRes contentDescriptionRes: Int,
    isChosen: Boolean,
    iconTint: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = if (isChosen) iconTint else containerColor,
        animationSpec = tween(),
        label = "анимация фона кнопок выбора режима таймера"
    )

    val animatedIconTintColor by animateColorAsState(
        targetValue = if (isChosen) containerColor else iconTint,
        animationSpec = tween(),
        label = "анимация цвета кнопок выбора режима таймера"
    )

    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = animatedContainerColor,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(id = contentDescriptionRes),
            tint = animatedIconTintColor
        )
    }
}