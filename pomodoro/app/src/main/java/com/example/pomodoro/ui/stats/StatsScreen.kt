package com.example.pomodoro.ui.stats

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodoro.R
import com.example.pomodoro.data.tags.Tag
import com.example.pomodoro.ui.AppViewModelProvider
import com.example.pomodoro.ui.PomodoroTopBar
import com.example.pomodoro.ui.navigation.NavigationDestination
import com.example.pomodoro.ui.theme.PomodoroTheme
import com.example.pomodoro.ui.theme.Theme
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate

@Immutable
object StatsDestination: NavigationDestination {
    override val route = "Stats"
    val titleRes = R.string.statistics
    val iconRes = R.drawable.close
}

class MyXAxisFormatter : ValueFormatter() {
    private val week = arrayOf("пн", "вт", "ср", "чт", "пт", "сб", "вс")
    private val days = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dayOfWeek = LocalDate.now().dayOfWeek.value
        week.mapIndexed { index, value ->
            if (index + 1 == dayOfWeek) "сегодня" else value
        }.toTypedArray()
    } else week

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}

class XAxisFormatter(private val tagNames: Array<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return tagNames.getOrNull(value.toInt()) ?: value.toString()
    }
}

@Composable
fun StatsScreen(
    navigateToMenu: () -> Unit,
    viewModel: StatsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.tagsUiStateFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            PomodoroTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
                titleRes = StatsDestination.titleRes,
                iconRes = StatsDestination.iconRes,
                onMenuButtonClick = navigateToMenu,
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
        FocusStatsBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_post_medium)),
//            uiState = uiState
        )
    }
}

@Preview(name = "Focus Stats Box Preview")
@Composable
fun FocusStatsBoxPreview() {
    PomodoroTheme(
        darkTheme = true,
        themeType = Theme.ThemeType.STANDARD
    ) {
        Scaffold { paddingValues ->
            FocusStatsBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
fun FocusStatsBox(
    modifier: Modifier = Modifier,
    uiState: TagsUiState = TagsUiState(
        tags = listOf(
            Tag(id = 1, name = "Work", time = 122),
            Tag(id = 212, name = "Study", time = 35),
            Tag(id = 333, name = "Music", time = 102),
            Tag(id = 3234, name = "Sport", time = 24),
            Tag(id = 1123, name = "Running", time = 45),
            Tag(id = 212, name = "Study2", time = 35),
            Tag(id = 333, name = "Music3", time = 102),
            Tag(id = 3234, name = "Sport4", time = 24),
            Tag(id = 1123, name = "Running5", time = 45)
        )
    )
) {
    val boxModifier = Modifier
        .height(220.dp)
        .fillMaxWidth()

    val graphModifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(8.dp))
        .alpha(0.8F)

    val tagNames = remember(uiState.tags) {
        uiState.tags.map { tag -> tag.name }.toTypedArray()
    }
    val ids = remember(uiState.tags) {
        List(uiState.tags.size) { index -> index.toFloat() }
    }
    val times = remember(uiState.tags) {
        uiState.tags.map { tag -> tag.time.toFloat()}
    }

    Column(modifier = modifier) {
        SummaryStatsBox(modifier = boxModifier)
        TodayStatsBox(modifier = boxModifier)
        FocusTimeStatsBox(
            modifier = boxModifier,
            graphModifier = graphModifier
        )
        FocusTagsStatsBox(
            modifier = boxModifier,
            graphModifier = graphModifier,
            tagNames = tagNames,
            xData = ids,
            yData = times
        )
    }
}

@Composable
fun SummaryStatsBox(modifier: Modifier = Modifier) {

}

@Composable
fun TodayStatsBox(modifier: Modifier = Modifier) {

}

@Composable
fun FocusTimeStatsBox(
    modifier: Modifier = Modifier,
    graphModifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
         LineGraph(
            modifier = graphModifier,
            xData = listOf(0F, 1F, 2F, 3F, 4F, 5F, 6F),
            yData = listOf(8F, 14F, 17F, 6F, 20F, 8F, 12F),
            dataLabel = ""
        )
    }
}

@Composable
fun FocusTagsStatsBox(
    modifier: Modifier = Modifier,
    graphModifier: Modifier = Modifier,
    tagNames: Array<String>,
    xData: List<Float>,
    yData: List<Float>
) {
    Box(modifier = modifier) {
        BarGraph(
            modifier = graphModifier,
            xData = xData,
            yData = yData,
            dataLabel = "",
            xValueFormatter = XAxisFormatter(tagNames = tagNames)
        )
    }
}