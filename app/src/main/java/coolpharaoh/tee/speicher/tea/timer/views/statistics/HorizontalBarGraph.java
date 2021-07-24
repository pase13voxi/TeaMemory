package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import android.app.Activity;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO;

public class HorizontalBarGraph {
    public static final int VISIBLE_RANGE = 10;
    public static final float BAR_WIDTH = 0.8f;
    public static final float TEXT_SIZE = 16f;
    public static final int ANIMATION_DURATION = 2000;

    final HorizontalBarChart chart;
    final Activity activity;

    public HorizontalBarGraph(final HorizontalBarChart chart, final Activity activity) {
        this.chart = chart;
        this.activity = activity;

        configureGraph();
    }

    private void configureGraph() {
        configureChart();
        configureXAxis();
        configureYAxisRight();
        configureYAxisLeft();
    }

    private void configureChart() {
        final Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.getLegend().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDrawValueAboveBar(false);
    }

    private void configureXAxis() {
        final XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(ContextCompat.getColor(activity, R.color.text_black));
        xAxis.setTextSize(TEXT_SIZE);
    }

    private void configureYAxisRight() {
        final YAxis yRight = chart.getAxisRight();
        yRight.setDrawAxisLine(true);
        yRight.setDrawGridLines(false);
        yRight.setEnabled(false);
    }

    private void configureYAxisLeft() {
        final YAxis yLeft = chart.getAxisLeft();
        yLeft.setEnabled(false);
    }

    public void display(final List<StatisticsPOJO> statistics) {
        setMaximum(statistics);

        setLabels(statistics);

        setGraphData(statistics);

        setVisibleRange(statistics);

        chart.invalidate();
        chart.animateY(ANIMATION_DURATION);
    }

    private void setMaximum(final List<StatisticsPOJO> statistics) {
        final YAxis yLeft = chart.getAxisLeft();

        long max = 0;
        for (StatisticsPOJO statistic : statistics) {
            if (max <= statistic.counter) {
                max = statistic.counter;
            }
        }

        yLeft.setAxisMaximum(max);
        yLeft.setAxisMinimum(0f);
    }

    private void setLabels(final List<StatisticsPOJO> statistics) {
        final XAxis xAxis = chart.getXAxis();

        final ArrayList<String> values = new ArrayList<>();
        for (final StatisticsPOJO statistic : statistics) {
            final String teaName = truncateLongText(statistic.teaname, 5);
            values.add(teaName);
        }

        final int labelCount = Math.min(statistics.size(), VISIBLE_RANGE);
        xAxis.setLabelCount(labelCount);
        xAxis.setValueFormatter(new XAxisFormatter(values));
    }

    public String truncateLongText(final String text, final int maxLength) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.substring(0, Math.min(text.length(), maxLength));
    }

    private void setGraphData(final List<StatisticsPOJO> statistics) {
        final BarData data = new BarData(createBarDataSetFrom(statistics));

        data.setBarWidth(BAR_WIDTH);

        chart.setData(data);
    }

    private BarDataSet createBarDataSetFrom(final List<StatisticsPOJO> statistics) {
        final ArrayList<BarEntry> entries = createBarEntries(statistics);

        final BarDataSet barDataSet = new BarDataSet(entries, "Bar Data Set");
        setColors(statistics, barDataSet);

        barDataSet.setValueFormatter(new XAxisFormatter(null));
        barDataSet.setValueTextSize(TEXT_SIZE);

        return barDataSet;
    }

    private ArrayList<BarEntry> createBarEntries(final List<StatisticsPOJO> statistics) {
        final ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < statistics.size(); i++) {
            entries.add(new BarEntry(i, statistics.get(i).counter));
        }
        return entries;
    }

    private void setColors(final List<StatisticsPOJO> statistics, final BarDataSet barDataSet) {
        final List<Integer> colors = setBarColors(statistics, barDataSet);

        setBarTextColors(statistics, barDataSet, colors);
    }

    private List<Integer> setBarColors(final List<StatisticsPOJO> statistics, final BarDataSet barDataSet) {
        final List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < statistics.size(); i++) {
            colors.add(statistics.get(i).teacolor);
        }
        barDataSet.setColors(colors);
        return colors;
    }

    private void setBarTextColors(final List<StatisticsPOJO> statistics, final BarDataSet barDataSet, final List<Integer> colors) {
        final List<Integer> textColors = new ArrayList<>();
        for (int i = 0; i < statistics.size(); i++) {
            // because of a weird behavior I need to add the foreground color twice
            final int foregroundColor = ColorConversation.discoverForegroundColor(colors.get(i));
            textColors.add(foregroundColor);
            textColors.add(foregroundColor);
        }

        barDataSet.setValueTextColors(textColors);
    }

    private void setVisibleRange(List<StatisticsPOJO> statistics) {
        chart.setVisibleXRangeMaximum(VISIBLE_RANGE);
        chart.moveViewTo(0, statistics.size(), YAxis.AxisDependency.LEFT);
    }

    public void reset() {
        chart.fitScreen();
        chart.getData().clearValues();
        chart.getXAxis().setValueFormatter(null);
        chart.notifyDataSetChanged();
        chart.clear();
        chart.invalidate();
    }

    public static class XAxisFormatter extends ValueFormatter {

        private final List<String> values;

        public XAxisFormatter(final List<String> values) {
            this.values = values;
        }

        @Override
        public String getAxisLabel(final float value, final AxisBase axis) {
            if (value % 1 == 0) {
                return this.values.get((int) value);
            }
            return "";
        }

        @Override
        public String getFormattedValue(final float value) {
            return String.valueOf((int) Math.floor(value));
        }
    }
}
