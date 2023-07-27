package coolpharaoh.tee.speicher.tea.timer.views.statistics

import android.app.Activity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation.discoverForegroundColor
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO
import kotlin.math.floor

class HorizontalBarGraph(val chart: HorizontalBarChart, val activity: Activity) {
    init {
        configureGraph()
    }

    private fun configureGraph() {
        configureChart()
        configureXAxis()
        configureYAxisRight()
        configureYAxisLeft()
    }

    private fun configureChart() {
        val description = Description()
        description.text = ""
        chart.description = description
        chart.legend.isEnabled = false
        chart.setPinchZoom(false)
        chart.setScaleEnabled(false)
        chart.setDrawValueAboveBar(false)
    }

    private fun configureXAxis() {
        val xAxis = chart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.setDrawAxisLine(false)
        xAxis.textColor = ContextCompat.getColor(activity, R.color.text_black)
        xAxis.textSize = TEXT_SIZE
    }

    private fun configureYAxisRight() {
        val yRight = chart.axisRight
        yRight.setDrawAxisLine(true)
        yRight.setDrawGridLines(false)
        yRight.isEnabled = false
    }

    private fun configureYAxisLeft() {
        val yLeft = chart.axisLeft
        yLeft.isEnabled = false
    }

    fun display(statistics: List<StatisticsPOJO>) {
        setMaximum(statistics)

        setLabels(statistics)

        setGraphData(statistics)

        setVisibleRange(statistics)

        chart.invalidate()
        chart.animateY(ANIMATION_DURATION)
    }

    private fun setMaximum(statistics: List<StatisticsPOJO>) {
        val yLeft = chart.axisLeft

        var max: Long = 0
        for (statistic in statistics) {
            if (max <= statistic.counter) {
                max = statistic.counter
            }
        }

        yLeft.axisMaximum = max.toFloat()
        yLeft.axisMinimum = 0f
    }

    private fun setLabels(statistics: List<StatisticsPOJO>) {
        val xAxis = chart.xAxis

        val values = ArrayList<String>()
        for (statistic in statistics) {
            val teaName = truncateLongText(statistic.teaName, 5)
            values.add(teaName)
        }

        val labelCount = Math.min(statistics.size, VISIBLE_RANGE)
        xAxis.labelCount = labelCount
        xAxis.valueFormatter = XAxisFormatter(values)
    }

    fun truncateLongText(text: String?, maxLength: Int): String {
        return if (text.isNullOrEmpty()) {
            ""
        } else text.substring(0, Math.min(text.length, maxLength))
    }

    private fun setGraphData(statistics: List<StatisticsPOJO>) {
        val data = BarData(createBarDataSetFrom(statistics))

        data.barWidth = BAR_WIDTH

        chart.data = data
    }

    private fun createBarDataSetFrom(statistics: List<StatisticsPOJO>): BarDataSet {
        val entries = createBarEntries(statistics)

        val barDataSet = BarDataSet(entries, "Bar Data Set")
        setColors(statistics, barDataSet)

        barDataSet.valueFormatter = XAxisFormatter(null)
        barDataSet.valueTextSize = TEXT_SIZE

        return barDataSet
    }

    private fun createBarEntries(statistics: List<StatisticsPOJO>): ArrayList<BarEntry> {
        val entries = ArrayList<BarEntry>()
        for (i in statistics.indices) {
            entries.add(BarEntry(i.toFloat(), statistics[i].counter.toFloat()))
        }
        return entries
    }

    private fun setColors(statistics: List<StatisticsPOJO>, barDataSet: BarDataSet) {
        val colors = setBarColors(statistics, barDataSet)

        setBarTextColors(statistics, barDataSet, colors)
    }

    private fun setBarColors(statistics: List<StatisticsPOJO>, barDataSet: BarDataSet): List<Int> {
        val colors: MutableList<Int> = ArrayList()
        for (i in statistics.indices) {
            colors.add(statistics[i].teaColor)
        }
        barDataSet.colors = colors
        return colors
    }

    private fun setBarTextColors(statistics: List<StatisticsPOJO>, barDataSet: BarDataSet,
                                 colors: List<Int>) {
        val textColors: MutableList<Int> = ArrayList()
        for (i in statistics.indices) {
            // because of a weird behavior I need to add the foreground color twice
            val foregroundColor = discoverForegroundColor(colors[i])
            textColors.add(foregroundColor)
            textColors.add(foregroundColor)
        }

        barDataSet.setValueTextColors(textColors)
    }

    private fun setVisibleRange(statistics: List<StatisticsPOJO>) {
        chart.setVisibleXRangeMaximum(VISIBLE_RANGE.toFloat())
        chart.moveViewTo(0f, statistics.size.toFloat(), YAxis.AxisDependency.LEFT)
    }

    fun reset() {
        chart.fitScreen()
        chart.data.clearValues()
        chart.xAxis.valueFormatter = null
        chart.notifyDataSetChanged()
        chart.clear()
        chart.invalidate()
    }

    class XAxisFormatter(private val values: List<String>?) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            return if (value % 1 == 0f) {
                values!![value.toInt()]
            } else ""
        }

        override fun getFormattedValue(value: Float): String {
            return floor(value.toDouble()).toInt().toString()
        }
    }

    companion object {
        const val VISIBLE_RANGE = 10
        const val BAR_WIDTH = 0.8f
        const val TEXT_SIZE = 16f
        const val ANIMATION_DURATION = 2000
    }
}