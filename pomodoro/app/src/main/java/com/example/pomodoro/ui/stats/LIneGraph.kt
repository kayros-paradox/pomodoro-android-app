package com.example.pomodoro.ui.stats

import android.graphics.Typeface
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pomodoro.ui.theme.PomodoroTheme
import com.example.pomodoro.ui.theme.Theme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.github.mikephil.charting.components.YAxis
import java.time.LocalDate

//@Preview(name = "Line Graph Preview")
//@Composable
//fun LineGraphPreview() {
//    PomodoroTheme(
//        darkTheme = true,
//        themeType = Theme.ThemeType.STANDARD
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            LineGraph(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(220.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .alpha(0.8F),
//                xData = listOf(0F, 1F, 2F, 3F, 4F, 5F, 6F),
//                yData = listOf(8F, 14F, 17F, 6F, 20F, 8F, 12F),
//                dataLabel = ""
//            )
//        }
//    }
//}


@Composable
fun LineGraph(
    xData: List<Float>,
    yData: List<Float>,
    dataLabel: String,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.onTertiary,
    fillColor: Color = MaterialTheme.colorScheme.onTertiary,
    fillAlpha: Int = 255,
    axisTextColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    xValueFormatter: ValueFormatter = MyXAxisFormatter(),
    drawXAxisLine: Boolean = true,
    drawXAxisGridLines: Boolean = false,
    drawAxisLeftLine: Boolean = false,
    drawAxisRightLine: Boolean = false,
    drawAxisLeftGridLines: Boolean = false,
    drawAxisRightGridLines: Boolean = false,
    drawGridBackground: Boolean = false,
    drawValues: Boolean = false,
    drawMarkers: Boolean = true,
    drawFilled: Boolean = false,
    drawHighlight: Boolean = false,
    descriptionEnabled: Boolean = false,
    dragEnabled: Boolean = false,
    touchEnabled: Boolean = false,
    legendEnabled: Boolean = false,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val chart = LineChart(context).apply {
                xAxis.position = xAxisPosition
                xAxis.textColor = axisTextColor.toArgb()
                xAxis.valueFormatter = xValueFormatter
                xAxis.setDrawAxisLine(drawXAxisLine)
                xAxis.setDrawGridLines(drawXAxisGridLines)
                xAxis.setYOffset(14F)
                xAxis.setAxisLineWidth(2F)
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                axisLeft.setDrawGridLines(drawAxisLeftGridLines)
                axisLeft.setDrawAxisLine(drawAxisLeftLine)
                axisRight.setDrawAxisLine(drawAxisRightLine)
                axisRight.setDrawGridLines(drawAxisRightGridLines)
                axisLeft.setXOffset(16F)
                axisRight.setXOffset(16F)
                setExtraOffsets(6F, 18F, 18F, 16F)
                setDragEnabled(dragEnabled)
                setTouchEnabled(touchEnabled)
                setBackgroundColor(backgroundColor.toArgb())
                setDrawGridBackground(drawGridBackground)
                xAxis.axisMinimum = -0.4F
                xAxis.axisMaximum = xData.size.toFloat() - 0.4F
                axisLeft.axisMinimum = 0F
                axisLeft.axisMaximum = 24F
                isHighlightPerDragEnabled = drawHighlight
                isHighlightPerTapEnabled = drawHighlight
            }
            val entries: List<Entry> = xData.zip(yData) { x, y -> Entry(x, y) }

            // Here we apply styling to the dataset
            val dataSet = LineDataSet(entries, dataLabel).apply {
                valueTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                color = lineColor.toArgb()
                setLineWidth(2F)
                setDrawValues(drawValues)
                setDrawCircles(drawMarkers)
                setDrawFilled(drawFilled)
                setFillColor(fillColor.toArgb())
                setFillAlpha(fillAlpha)
            }
            chart.data = LineData(dataSet).apply {}

            /* Extra chart styling */
            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled
            chart.axisLeft.textColor = axisTextColor.toArgb()
            chart.axisRight.isEnabled = yAxisRightEnabled

            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}