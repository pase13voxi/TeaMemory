package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import android.os.Build;

import androidx.test.core.app.ActivityScenario;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.felix.horizontalbargraph.HorizontalBar;
import br.com.felix.horizontalbargraph.model.BarItem;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.StatisticsPOJO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;


//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class StatisticsTest {
    private static final int OVERALL_ITEM = 0;
    private static final int MONTH_ITEM = 1;
    private static final int WEEK_ITEM = 2;
    private static final int DAY_ITEM = 3;
    
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
    public void launchActivityAndExpectListOverall() {
        List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("LAUNCH");
        when(counterDao.getTeaCounterOverall()).thenReturn(statisticsPOJOs);

        ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> checkExpectedItems(statisticsPOJOs, statistics));
    }

    @Test
    public void setPeriodOverallAndExpectListOverall() {
        List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("OVERALL");
        when(counterDao.getTeaCounterOverall()).thenReturn(statisticsPOJOs);

        ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OVERALL_ITEM);

            checkExpectedItems(statisticsPOJOs, statistics);
        });
    }

    @Test
    public void setPeriodMonthAndExpectListMonth() {
        List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("MONTH");
        when(counterDao.getTeaCounterMonth()).thenReturn(statisticsPOJOs);

        ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(MONTH_ITEM);

            checkExpectedItems(statisticsPOJOs, statistics);
        });
    }

    @Test
    public void setPeriodWeekAndExpectListWeek() {
        List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("WEEK");
        when(counterDao.getTeaCounterWeek()).thenReturn(statisticsPOJOs);

        ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(WEEK_ITEM);

            checkExpectedItems(statisticsPOJOs, statistics);
        });
    }

    @Test
    public void setPeriodDayAndExpectListDay() {
        List<StatisticsPOJO> statisticsPOJOs = getStatisticsPOJOs("DAY");
        when(counterDao.getTeaCounterDay()).thenReturn(statisticsPOJOs);

        ActivityScenario<Statistics> statisticsActivityScenario = ActivityScenario.launch(Statistics.class);

        statisticsActivityScenario.onActivity(statistics -> {
            statistics.onOptionsItemSelected(new RoboMenuItem(R.id.action_statistics_period));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(DAY_ITEM);

            checkExpectedItems(statisticsPOJOs, statistics);
        });
    }

    private void checkExpectedItems(List<StatisticsPOJO> statisticsPOJOs, Statistics statistics) {
        HorizontalBar horizontalBar = statistics.findViewById(R.id.horizontal_bar_statistics);
        List<BarItem> items = horizontalBar.getItems();
        assertThat(items)
                .extracting(
                        BarItem::getDescription,
                        BarItem::getValue1,
                        BarItem::getColorBar1
                ).
                containsExactly(
                        Tuple.tuple(
                                statisticsPOJOs.get(0).teaname,
                                (double) statisticsPOJOs.get(0).counter,
                                statisticsPOJOs.get(0).teacolor
                        ),
                        Tuple.tuple(
                                statisticsPOJOs.get(1).teaname,
                                (double) statisticsPOJOs.get(1).counter,
                                statisticsPOJOs.get(1).teacolor
                        ),
                        Tuple.tuple(
                                statisticsPOJOs.get(2).teaname,
                                (double) statisticsPOJOs.get(2).counter,
                                statisticsPOJOs.get(2).teacolor
                        )
                );
    }

    private List<StatisticsPOJO> getStatisticsPOJOs(String name) {
        List<StatisticsPOJO> statisticsPOJOS = new ArrayList<>();

        for (int count = 1; count <= 3; count++) {
            StatisticsPOJO statistic = new StatisticsPOJO();
            statistic.teaname = name + count;
            statistic.teacolor = count;
            statistic.counter = count;
            statisticsPOJOS.add(statistic);
        }

        return statisticsPOJOS;
    }
}
