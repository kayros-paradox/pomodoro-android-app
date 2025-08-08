package com.example.pomodoro.ui.menu

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pomodoro.R
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.data.options.OptionValue
import com.example.pomodoro.service.PomodoroService
import com.example.pomodoro.service.pomodoroServiceIntent
import com.example.pomodoro.ui.PomodoroTopBar
import com.example.pomodoro.ui.navigation.NavigationDestination
import com.example.pomodoro.ui.theme.BlueDarkWoodsmoke
import com.example.pomodoro.ui.theme.OrangePeel
import com.example.pomodoro.ui.theme.Theme

@Immutable
object MenuDestination: NavigationDestination {
    override val route = "Menu"
    val titleRes = R.string.options
    val iconRes = R.drawable.close
}

@Composable
fun MenuScreen(
    updateThemeType: (Theme.ThemeType) -> Unit,
    navigateToWelcome: () -> Unit,
    navigateToTimer: () -> Unit
) {
    Scaffold(
        topBar = {
            PomodoroTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
                titleRes = MenuDestination.titleRes,
                iconRes = MenuDestination.iconRes,
                onMenuButtonClick = navigateToTimer,
                showMenuButton = true
            )
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                Color(color = 0x00FFFFFF)
                            )
                        )
                    )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        MenuPanel(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
            updateThemeType = updateThemeType,
            navigateToWelcome = navigateToWelcome,
        )
    }
}

@Composable
fun MenuPanel(
    modifier: Modifier = Modifier,
    navigateToWelcome: () -> Unit,
    updateThemeType: (Theme.ThemeType) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhaseEditorPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        )
        OptionsPanel(
            modifier = Modifier.padding(bottom = 25.dp),
            navigateToWelcome = navigateToWelcome,
            updateThemeType = updateThemeType,
        )
    }
}

@Composable
fun OptionsPanel(
    modifier: Modifier = Modifier,
    navigateToWelcome: () -> Unit,
    updateThemeType: (Theme.ThemeType) -> Unit
) {
    val menuTextButtonModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = dimensionResource(id = R.dimen.padding_pre_medium))

    val menuOptionModifier = remember { Modifier.fillMaxWidth() }

    val menuPremiumOptionModifier = menuOptionModifier
        .padding(top = dimensionResource(id = R.dimen.padding_small))

    val standardOptionSwitchColors = SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.background,
        checkedTrackColor = MaterialTheme.colorScheme.onBackground,
        uncheckedThumbColor = MaterialTheme.colorScheme.onBackground,
        uncheckedTrackColor = MaterialTheme.colorScheme.background
    )

    val premiumOptionSwitchColors = SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.background,
        checkedTrackColor = OrangePeel,
        uncheckedThumbColor = OrangePeel,
        uncheckedTrackColor = MaterialTheme.colorScheme.background
    )

    val menuTextButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    Column(modifier = modifier) {
        BackgroundColorEditorPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_pre_medium)),
            updateThemeType = updateThemeType
        )
        MenuOption(
            modifier = menuOptionModifier,
            option = Option.NotificationSound,
            switchColors = standardOptionSwitchColors
        )
        MenuOption(
            modifier = menuOptionModifier,
            option = Option.Vibration,
            switchColors = standardOptionSwitchColors
        )
        MenuOption(
            modifier = menuOptionModifier,
            option = Option.KeepTheScreenOn,
            switchColors = standardOptionSwitchColors
        )
        MenuOption(
            modifier = menuOptionModifier,
            option = Option.ShowTaskbar,
            switchColors = standardOptionSwitchColors,
        )
        MenuOption(
            modifier = menuOptionModifier,
            option = Option.FullScreenMode,
            switchColors = standardOptionSwitchColors
        )
        MenuOption(
            modifier = menuPremiumOptionModifier,
            option = Option.AutoBreakStart,
            switchColors = standardOptionSwitchColors
        )
        MenuOption(
            modifier = menuPremiumOptionModifier,
            option = Option.AutoPomodoroStart,
            switchColors = standardOptionSwitchColors
        )
        MenuOption(
            modifier = menuPremiumOptionModifier,
            option = Option.NightMode,
            switchColors = premiumOptionSwitchColors,
        )
        RestIntervalEditorOption(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.padding_pre_medium))
        )

        MenuTextButton(
            modifier = menuTextButtonModifier,
            colors = menuTextButtonColors,
            textRes = R.string.show_the_introduction,
            onClick = navigateToWelcome
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestIntervalEditorOption(modifier: Modifier = Modifier) {
    val context: Context = LocalContext.current
    var value by remember {
        mutableFloatStateOf(
            value = OptionValue.RestInterval.data.toFloat()
        )
    }

    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(top = dimensionResource(id = R.dimen.padding_small))

    Column(modifier = modifier) {
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.option_rest_interval),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
                    .alpha(0.8F),
                text = value.toInt().toString(),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp),
                    value = value,
                    onValueChange = {
                        value = it
                        context.pomodoroServiceIntent(
                            PomodoroService.Action.UpdateRestInterval,
                            it.toInt().toString()
                        )
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.onBackground,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(ButtonDefaults.IconSize)
                                .background(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    shape = CircleShape
                                )
                        )
                    },
                    track = {
                        SliderDefaults.Track(
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0.7F),
                            sliderState = it,
                            drawStopIndicator = { _ -> },
                            drawTick = { _, _ -> },
                            thumbTrackGapSize = 0.dp,
                            trackInsideCornerSize = 0.dp
                        )
                    },
                    steps = 8,
                    valueRange = 1F..10F
                )
            }
        }
    }
}

@Composable
fun MenuTextButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    @StringRes textRes: Int,
    fontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = colors,
        onClick = onClick
    ) {
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun BackgroundColorEditorPanel(
    modifier: Modifier = Modifier,
    updateThemeType: (Theme.ThemeType) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_pre_medium))
                .padding(bottom = dimensionResource(id = R.dimen.padding_small)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.theme_color),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }

        val buttonModifier = Modifier
            .weight(1F)
            .aspectRatio(ratio = 1F)

        val spacerModifier = Modifier
            .size(dimensionResource(id = R.dimen.padding_pre_medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.padding_pre_medium))
        ) {
            var chosenThemeType by remember { mutableStateOf(Theme.currentThemeType) }

            ChangeBackgroundButton(
                modifier = buttonModifier,
                themeType = Theme.ThemeType.STANDARD,
                isChosen = chosenThemeType == Theme.ThemeType.STANDARD,
                onClick = { themeType ->
                    updateThemeType(themeType)
                    chosenThemeType = themeType
                }
            )
            Spacer(modifier = spacerModifier)
            ChangeBackgroundButton(
                modifier = buttonModifier,
                themeType = Theme.ThemeType.BLUE,
                isChosen = chosenThemeType == Theme.ThemeType.BLUE,
                onClick = { themeType ->
                    updateThemeType(themeType)
                    chosenThemeType = themeType
                }
            )
            Spacer(modifier = spacerModifier)
            ChangeBackgroundButton(
                modifier = buttonModifier,
                themeType = Theme.ThemeType.GREEN,
                isChosen = chosenThemeType == Theme.ThemeType.GREEN,
                onClick = { themeType ->
                    updateThemeType(themeType)
                    chosenThemeType = themeType
                }
            )
            Spacer(modifier = spacerModifier)
            ChangeBackgroundButton(
                modifier = buttonModifier,
                themeType = Theme.ThemeType.PINK,
                isChosen = chosenThemeType == Theme.ThemeType.PINK,
                onClick = { themeType ->
                    updateThemeType(themeType)
                    chosenThemeType = themeType
                }
            )
            Spacer(modifier = spacerModifier)
            ChangeBackgroundButton(
                modifier = buttonModifier,
                themeType = Theme.ThemeType.BROWN,
                isChosen = chosenThemeType == Theme.ThemeType.BROWN,
                onClick = { themeType ->
                    updateThemeType(themeType)
                    chosenThemeType = themeType
                }
            )
            Spacer(modifier = spacerModifier)
            ChangeBackgroundButton(
                modifier = buttonModifier,
                themeType = Theme.ThemeType.VIOLET,
                isChosen = chosenThemeType == Theme.ThemeType.VIOLET,
                onClick = { themeType ->
                    updateThemeType(themeType)
                    chosenThemeType = themeType
                }
            )
        }
    }
}

@Composable
fun ChangeBackgroundButton(
    modifier: Modifier = Modifier,
    isChosen: Boolean,
    themeType: Theme.ThemeType,
    onClick: (Theme.ThemeType) -> Unit
) {
    IconButton(
        onClick = { onClick(themeType) },
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = themeType.backgroundColor,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        if (isChosen) {
            Icon(
                painter = painterResource(id = R.drawable.check_circle),
                contentDescription = null,
                tint = BlueDarkWoodsmoke,
                modifier = Modifier.fillMaxSize(fraction = 0.8F)
            )
        }
    }
}

/**
 * Опция меню настроек.
 * @param switchColors Цвета переключателя.
 * @param option Настройка (enum элемент).
 */
@Composable
fun MenuOption(
    modifier: Modifier = Modifier,
    switchColors: SwitchColors,
    option: Option,
) {
    var checked by remember { mutableStateOf(option.checked) }

    Column {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = option.nameId),
                style = MaterialTheme.typography.labelMedium
            )
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = !checked
                    option.checked = !option.checked
                    option.onSwitch()
                },
                colors = switchColors
            )
        }
        if (option.descriptionId != null) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
            ) {
                Text(
                    text = stringResource(id = option.descriptionId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.fillMaxWidth(fraction = 0.75F)
                )
            }
        }
    }
}