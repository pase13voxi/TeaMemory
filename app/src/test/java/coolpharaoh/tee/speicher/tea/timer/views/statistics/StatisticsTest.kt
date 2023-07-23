package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

import androidx.test.core.app.ActivityScenario;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO;

@RunWith(RobolectricTestRunner.class)
public class StatisticsTest {
    private static final int WEEK_ITEM = 0;
    private static final int MONTH_ITEM = 1;
    private static final int YEAR_ITEM = 2;
    private static final int OVERALL_ITEM = 3;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    CounterDao counterDao;

    @Before
    public void setUp() {
        mockDB();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
    }

    @Test
    public void launchActivityAndExpectListWeek() {
        final int countSize = 2;
        final List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("LAUNCH", countSize);
        when(counterDao.getTeaCounterWeek()).thenReturn(statisticsPOJOs);

        final ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> checkExpectedItems(statistics, countSize));
    }

    @Test
    public void setPeriodWeekAndExpectListWeek() {
        final int countSize = 5;
        final List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("WEEK", countSize);
        when(counterDao.getTeaCounterWeek()).thenReturn(statisticsPOJOs);

        final ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(WEEK_ITEM);

            checkExpectedItems(statistics, countSize);
        });
    }

    @Test
    public void setPeriodMonthAndExpectListMonth() {
        final int countSize = 4;
        final List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("MONTH", countSize);
        when(counterDao.getTeaCounterMonth()).thenReturn(statisticsPOJOs);

        final ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(MONTH_ITEM);

            checkExpectedItems(statistics, countSize);
        });
    }

    @Test
    public void setPeriodDayAndExpectListYear() {
        final int countSize = 6;
        final List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("YEAR", countSize);
        when(counterDao.getTeaCounterYear()).thenReturn(statisticsPOJOs);

        final ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(YEAR_ITEM);

            checkExpectedItems(statistics, countSize);
        });
    }

    @Test
    public void setPeriodOverallAndExpectListOverall() {
        final int countSize = 3;
        final List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("OVERALL", countSize);
        when(counterDao.getTeaCounterOverall()).thenReturn(statisticsPOJOs);

        final ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OVERALL_ITEM);

            checkExpectedItems(statistics, countSize);
        });
    }

    private void checkExpectedItems(final Statistics statistics, final int itemCount) {
        final HorizontalBarChart horizontalBar = statistics.findViewById(R.id.horizontal_graph_statistics);

        final IBarDataSet iBarDataSet = horizontalBar.getData().getDataSets().get(0);
        assertThat(iBarDataSet.getEntryCount()).isEqualTo(itemCount);
        for (int i = 0; i < itemCount; i++) {
            assertThat(iBarDataSet.getEntryForIndex(i).getY()).isEqualTo(i + 1);
        }
    }

    private List<StatisticsPOJO> getStatisticsPOJOs(final String name, final int itemCount) {
        final List<StatisticsPOJO> statisticsPOJOS = new ArrayList<>();

        for (int count = 1; count <= itemCount; count++) {
            final StatisticsPOJO statistic = new StatisticsPOJO();
            statistic.teaName = name + count;
            statistic.teaColor = count;
            statistic.counter = count;
            statisticsPOJOS.add(statistic);
        }

        return statisticsPOJOS;
    }
}
