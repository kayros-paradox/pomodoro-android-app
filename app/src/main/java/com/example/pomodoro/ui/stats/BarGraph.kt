package com.example.pomodoro.ui.stats

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun BarGraph(
    xData: List<Float>,
    yData: List<Float>,
    dataLabel: String,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.onTertiary,
    axisTextColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    xValueFormatter: ValueFormatter? = MyXAxisFormatter(),
    drawXAxisLine: Boolean = true,
    drawXAxisGridLines: Boolean = false,
    drawAxisLeftLine: Boolean = false,
    drawAxisRightLine: Boolean = false,
    drawAxisLeftGridLines: Boolean = false,
    drawAxisRightGridLines: Boolean = false,
    drawGridBackground: Boolean = false,
    drawValues: Boolean = false,
    drawHighlight: Boolean = false,
    descriptionEnabled: Boolean = false,
    dragEnabled: Boolean = true,
    scaleEnabled: Boolean = false,
    touchEnabled: Boolean = true,
    legendEnabled: Boolean = false,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val chart = BarChart(context).apply {
                xValueFormatter.let { value -> xAxis.valueFormatter = value }
                xAxis.apply {
                    isDragEnabled = dragEnabled
                    position = xAxisPosition
                    textColor = axisTextColor.toArgb()
                    granularity = 1F
                    isGranularityEnabled = true
                    labelCount = xData.size
                    yOffset = 14F
                    axisLineWidth = 2F
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawAxisLine(drawXAxisLine)
                    setVisibleXRange(0F, 5F)
                    setDrawGridLines(drawXAxisGridLines)
                }
                axisLeft.apply {
                    axisMinimum = 0F
                    xOffset = 16F
                    setDrawGridLines(drawAxisLeftGridLines)
                    setDrawAxisLine(drawAxisLeftLine)
                }
                axisRight.apply {
                    xOffset = 16F
                    setDrawAxisLine(drawAxisRightLine)
                    setDrawGridLines(drawAxisRightGridLines)
                }
                isHighlightPerDragEnabled = drawHighlight
                isHighlightPerTapEnabled = drawHighlight
                setExtraOffsets(6F, 18F, 18F, 16F)
//                fitScreen()
//                setPinchZoom(true)
//                setScaleEnabled(scaleEnabled)
//                setTouchEnabled(touchEnabled)
                setBackgroundColor(backgroundColor.toArgb())
                setDrawGridBackground(drawGridBackground)
            }
            val entries: List<BarEntry> = xData.zip(yData) { x, y -> BarEntry(x, y) }

            val dataSet = BarDataSet(entries, dataLabel).apply {
                valueTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                color = lineColor.toArgb()
                setDrawValues(drawValues)
            }
            chart.data = BarData(dataSet).apply {
                barWidth = 0.5F
            }

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