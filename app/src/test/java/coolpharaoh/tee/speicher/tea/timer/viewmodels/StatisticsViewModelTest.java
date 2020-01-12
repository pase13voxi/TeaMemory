package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsViewModelTest {

    private StatisticsViewModel statisticsViewModel;

    @Mock
    CounterDAO counterDAO;
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;

    @Before
    public void setUp() {
        when(teaMemoryDatabase.getCounterDAO()).thenReturn(counterDAO);

        statisticsViewModel = new StatisticsViewModel(teaMemoryDatabase);
    }

    @Test
    public void getStatisticsOverall() {
        List<StatisticsPOJO> counterOverallBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterOverallBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterOverallBefore.add(statisticsPOJO2);

        when(counterDAO.getTeaCounterOverall()).thenReturn(counterOverallBefore);

        List<StatisticsPOJO> counterOverallAfter = statisticsViewModel.getStatisticsOverall();

        assertEquals(counterOverallBefore, counterOverallAfter);
    }

    @Test
    public void getStatisticsMonth() {
        List<StatisticsPOJO> counterMonthBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterMonthBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterMonthBefore.add(statisticsPOJO2);

        when(counterDAO.getTeaCounterMonth()).thenReturn(counterMonthBefore);

        List<StatisticsPOJO> counterMonthAfter = statisticsViewModel.getStatisticsMonth();

        assertEquals(counterMonthBefore, counterMonthAfter);
    }

    @Test
    public void getStatisticsWeek() {
        List<StatisticsPOJO> counterWeekBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterWeekBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterWeekBefore.add(statisticsPOJO2);

        when(counterDAO.getTeaCounterWeek()).thenReturn(counterWeekBefore);

        List<StatisticsPOJO> counterWeekAfter = statisticsViewModel.getStatisticsWeek();

        assertEquals(counterWeekBefore, counterWeekAfter);
    }

    @Test
    public void getStatisticsDay() {
        List<StatisticsPOJO> counterDayBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterDayBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterDayBefore.add(statisticsPOJO2);

        when(counterDAO.getTeaCounterDay()).thenReturn(counterDayBefore);

        List<StatisticsPOJO> counterDayAfter = statisticsViewModel.getStatisticsDay();

        assertEquals(counterDayBefore, counterDayAfter);
    }

    @Test
    public void refreshAllCounter() {
        Instant now = Instant.now();
        Date today = Date.from(now);
        Date dayBefore = Date.from(now.minus(Duration.ofDays(1)));
        Date weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        Date monthBefore = Date.from(now.minus(Duration.ofDays(31)));

        List<Counter> countersBefore = new ArrayList<>();

        Counter noRefresh = new Counter(1L, 4, 7, 9, 15L, today, today, today);
        countersBefore.add(noRefresh);
        Counter refreshDay = new Counter(1L, 4, 7, 9, 15L, dayBefore, today, today);
        countersBefore.add(refreshDay);
        Counter refreshWeek = new Counter(1L, 4, 7, 9, 15L, today, weekBefore, today);
        countersBefore.add(refreshWeek);
        Counter refreshMonth = new Counter(1L, 4, 7, 9, 15L, today, today,monthBefore);
        countersBefore.add(refreshMonth);
        Counter refreshAll = new Counter(1L, 4, 7, 9, 15L, monthBefore, monthBefore, monthBefore);
        countersBefore.add(refreshAll);

        when(counterDAO.getCounters()).thenReturn(countersBefore);

        statisticsViewModel.refreshAllCounter();

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterDAO, times(5)).update(captor.capture());

        List<Counter> counterAfter = captor.getAllValues();

        assertEquals(noRefresh, counterAfter.get(0));

        assertEquals(0, counterAfter.get(1).getDay());
        assertEquals(7, counterAfter.get(1).getWeek());
        assertEquals(9, counterAfter.get(1).getMonth());
        assertEquals(15L, counterAfter.get(1).getOverall());

        assertEquals(4, counterAfter.get(2).getDay());
        assertEquals(0, counterAfter.get(2).getWeek());
        assertEquals(9, counterAfter.get(2).getMonth());
        assertEquals(15L, counterAfter.get(2).getOverall());

        assertEquals(4, counterAfter.get(3).getDay());
        assertEquals(7, counterAfter.get(3).getWeek());
        assertEquals(0, counterAfter.get(3).getMonth());
        assertEquals(15L, counterAfter.get(3).getOverall());

        assertEquals(0, counterAfter.get(4).getDay());
        assertEquals(0, counterAfter.get(4).getWeek());
        assertEquals(0, counterAfter.get(4).getMonth());
        assertEquals(15L, counterAfter.get(4).getOverall());
    }
}
